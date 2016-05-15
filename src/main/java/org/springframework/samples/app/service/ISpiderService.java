package org.springframework.samples.app.service;

import java.io.IOException;

public interface ISpiderService {	

	public String get(String url);
	/**
	 * 获取项目信息
	 * @param url
	 * @return
	 */
	public StringBuffer getProjectList(String url);
	/**
	 * 获取楼栋销售信息
	 * @param url
	 * @return
	 */
	public String getHouserList(String url);
	
	public void spider(int begin, int end) throws IOException;
	
	
	public void exec(String sql);
}
