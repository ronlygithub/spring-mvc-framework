package org.springframework.samples.app.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
import org.springframework.stereotype.Service;
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
		System.out.println();
		String url = "http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp";
		StringBuffer resultSet = new StringBuffer();
		for (int i = begin; i < end; i++) {
			resultSet.append(getProjectList(url+"?page="+i)).append("\n");
			writer(resultSet.toString(), "e:\\projectList.txt");
		}
		long endTime = System.currentTimeMillis();
		System.out.println("spider completed time expensed: "+(endTime-startTime));
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
