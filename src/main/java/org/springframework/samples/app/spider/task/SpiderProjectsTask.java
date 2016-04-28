package org.springframework.samples.app.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderProjectsTask extends AbstractSpiderTask{

	private String url;
	public SpiderProjectsTask(String url) {
		this.url = url;
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
	public String call() throws Exception {		
		return getProjectList(url).toString();
	}
}
