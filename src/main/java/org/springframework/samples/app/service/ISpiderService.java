package org.springframework.samples.app.service;

import java.io.IOException;

public interface ISpiderService {	

	public String get(String url);
	/**
	 * ��ȡ��Ŀ��Ϣ
	 * @param url
	 * @return
	 */
	public StringBuffer getProjectList(String url);
	/**
	 * ��ȡ¥��������Ϣ
	 * @param url
	 * @return
	 */
	public String getHouserList(String url);
	
	public void spider(int begin, int end) throws IOException;
	
	
	public void exec(String sql);
}
