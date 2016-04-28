package org.springframework.samples.app.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.samples.app.service.ISpiderService;
import org.springframework.samples.app.spider.task.SpiderHousesTask;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service("spiderService")
public class SpiderServiceImpl implements ISpiderService{

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
	
	public void spider(int begin, int end){
		long startTime = System.currentTimeMillis();
		String projectLisfFile = "e:\\syfc\\projectList.txt";
		String url = "http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp";
		StringBuffer resultSet = new StringBuffer();
		for (int i = begin; i < end; i++) {
			resultSet.append(getProjectList(url+"?page="+i)).append("\n");
			
			writer(resultSet.toString(), projectLisfFile);
			resultSet = new StringBuffer();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("spider completed time expensed: "+(endTime-startTime));
		
		String[] projects = reader(projectLisfFile).split("\\n");
		Set<String> set = new HashSet<String>();
		for (String project : projects) {
			set.add(project);
		}
		ExecutorService executor = Executors.newFixedThreadPool(4);
		StringBuffer result = new StringBuffer();
		int count = 0;
		for (String project : set) {
			count++;
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
				writer(url1, "e:\\syfc\\houses.error.txt");
			}
			
			if (count%50==0) {
				writer(result.toString(), "e:\\syfc\\housesList.txt");
				result = new StringBuffer();
			}
		}
		
	}
	
	public void writer(String resultSet ,String path){
		try {
			FileOutputStream out = new FileOutputStream(path,true);
			OutputStreamWriter writer = new OutputStreamWriter(out,"utf-8");
			
			writer.write(resultSet.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String reader(String path){
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[4096];
		try {
			FileInputStream in = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(in, "utf-8");
			while(reader.read(buffer)!=-1){
				result.append(buffer);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
		
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
