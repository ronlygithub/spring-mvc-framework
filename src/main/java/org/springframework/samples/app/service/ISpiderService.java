package org.springframework.samples.app.service;

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
	
	public void spider(int begin, int end);
	
	public void writer(String resultSet ,String path);
}
