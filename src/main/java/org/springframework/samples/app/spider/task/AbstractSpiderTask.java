package org.springframework.samples.app.spider.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public abstract class AbstractSpiderTask implements Callable<String>{

//	private String url;
	
	
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
	
	

}
