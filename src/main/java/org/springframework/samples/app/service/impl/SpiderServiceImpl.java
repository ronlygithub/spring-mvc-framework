package org.springframework.samples.app.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.samples.app.dao.DatasourceMapper;
import org.springframework.samples.app.dao.ProjectMapper;
import org.springframework.samples.app.service.ISpiderService;
import org.springframework.samples.app.spider.task.SpiderHousesTask;
import org.springframework.samples.app.utils.MFileUtils;
import org.springframework.samples.app.vo.Datasource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service("spiderService")
public class SpiderServiceImpl implements ISpiderService{
	@Resource
	private DatasourceMapper datasourceMapper;
	@Resource
	private ProjectMapper projectMapper;
	private String currentDate;
	private String projectLisfFile = "e:\\syfc\\projectList.txt.";

	public String getCurrentDate() {
		if (currentDate == null) {
			Date d = new Date();         
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");  
	        currentDate = sdf.format(d);  
		}
		return currentDate;
	}

	@Override
	public String get(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		StringBuffer result = new StringBuffer();
		try {
			httpClient.executeMethod(getMethod);
			InputStream response = getMethod.getResponseBodyAsStream();
			byte[] buffer = new byte[4096];
			int n;
			while ((n = response.read(buffer))!=-1) {
				result.append(new String(buffer,0,n));
			}
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public void spider(int begin, int end) throws IOException{
		spiderProjectInfo(begin, end);		
		spiderHouseInfo();
		load("houses", "E:\\syfc\\housesList.txt");
	}
	
	public void load(String tableName,String fileName) throws IOException{
		preProcess(fileName);
		dropTable(tableName);
		createTable(tableName);
		loadData(tableName, fileName);
	}
	
	public void dropTable(String tableName){
		String dropSQL = "drop table if exists "+ tableName+getCurrentDate();
		exec(dropSQL);
	}
	
	public void createTable(String tableName){
		String createSQL = "create table "+tableName+getCurrentDate()+ " like "+tableName;
		exec(createSQL);
	}
	
	public void loadData(String tableName,String fileName){
		
		String loadSQL = "load data local infile '"
				+ fileName.replace("\\", "\\\\")
				+ "' into table "
				+ tableName+getCurrentDate()
				+ " character set 'utf8' fields terminated by ',' lines terminated by '\n'";
		exec(loadSQL);
	}
	
	public void preProcess(String fileName) throws IOException{
		String reader = MFileUtils.reader(fileName+getCurrentDate());
		String[] split = reader.split("\n");
		Set<String> set = new HashSet<String>();
		for (String item : split) {
			set.add(item);
		}
		
		StringBuffer buffer = new StringBuffer();
		for (String item : set) {
			if (StringUtils.isEmpty(item)) {
				continue;
			}
			String[] property = item.split(",");
			if (property == null || property.length<=1) {
				continue;
			}
			buffer.append(item).append("\n");
		}
		MFileUtils.writer(buffer.toString(), fileName);
	}
	
	
	

	private void spiderProjectInfo(int begin, int end) {
		long startTime = System.currentTimeMillis();		
		String url = "http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp";
		StringBuffer resultSet = new StringBuffer();
		for (int i = begin; i < end; i++) {
			resultSet.append(getProjectList(url+"?page="+i)).append("\n");			
			MFileUtils.writer(resultSet.toString(), projectLisfFile+getCurrentDate());
			resultSet = new StringBuffer();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("spider completed time expensed: "+(endTime-startTime));
	}

	private void spiderHouseInfo() throws IOException {
		String[] projects = MFileUtils.reader(projectLisfFile+getCurrentDate()).split("\\n");
		Set<String> set = new HashSet<String>();
		for (String project : projects) {
			set.add(project);
		}
		ExecutorService executor = Executors.newFixedThreadPool(4);
		StringBuffer result = new StringBuffer();
		for (String project : set) {
			String[] infos = project.split(",");
			if (infos == null || infos.length == 0) {
				continue;
			}
			String url1 = "http://www.syfc.com.cn/" + infos[0];
			Future<String> submit = executor.submit(new SpiderHousesTask(url1));
			try {
				String houseInfo = submit.get();
				result.append(houseInfo);
			} catch (Exception e) {
				MFileUtils.writer(url1, "e:\\syfc\\houses.error.txt."+getCurrentDate());
			}
			
			if (result.length()>100000) {
				MFileUtils.writer(result.toString(), "e:\\syfc\\housesList.txt"+getCurrentDate());
				result = new StringBuffer();
			}
		}
		
		MFileUtils.writer(result.toString(), "e:\\syfc\\housesList.txt"+getCurrentDate());
	}
	
	public void exec(String sql){
		Datasource datasource = datasourceMapper.get();
		StringBuffer url = new StringBuffer();
		url.append(datasource.getUrl())
		.append("user=")
		.append(datasource.getUser())
		.append("&password=")
		.append(datasource.getPassword());
		try {
			Class.forName(datasource.getDriver());
			Connection connection = DriverManager.getConnection(url.toString());			
			PreparedStatement statement = connection.prepareStatement(sql);			
			statement.execute();
//			connection.commit();
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {			
			e.printStackTrace();
		}		
	}
	
	
	public StringBuffer getProjectList(String url){
		System.out.println("processing: "+url);
		long start = System.currentTimeMillis();
		String html = get(url);
		long end = System.currentTimeMillis();
		System.out.println("get html expensed time: "+ (end - start));
		String result = html.substring(html.indexOf("新建楼盘列表开始"),html.indexOf("新建楼盘列表结束"));
		Document doc = Jsoup.parse(result);
		Element tbody = doc.select("tbody").get(1);
		Elements projectList = tbody.select("tr");
		StringBuffer resultSet = new StringBuffer();
		for (Element project : projectList) {			
			Elements td = project.select("td");
			if (td.size()!=6) {
				continue;
			}
			Element projectName = td.get(0).select("a").get(0);
			resultSet.append(projectName.attr("href")).append(",").append(projectName.text());
			Element projectAddr = td.get(1).select("a").get(0);
			resultSet.append(",").append(projectAddr.text());
			Element componey = td.get(3).select("a").get(0);
			resultSet.append(",").append(componey.text());
			Element openTime = td.get(4);
			resultSet.append(",").append(openTime.text());
			Element location = td.get(5);
			resultSet.append(",").append(location.text());
			resultSet.append("\n");
		}
		return resultSet;		
	}

	@Override
	public String getHouserList(String url) {
		String html = get(url);		
		StringBuffer resultSet = new StringBuffer();
		Document doc = Jsoup.parse(html.substring(html.indexOf("可查询该楼栋详细信息")));
		Element tbody = doc.select("tbody").get(1);
		Elements trs = tbody.select("tr");
		for (int i = 1; i < trs.size(); i++) {
			Elements tds = trs.get(i).select("td");
			for (Element td : tds) {
				String text = td.text();
				resultSet.append(text).append(",");
			}
			resultSet.deleteCharAt(resultSet.length()-1).append("\n");
		}
		
		return resultSet.toString();
	}

}
