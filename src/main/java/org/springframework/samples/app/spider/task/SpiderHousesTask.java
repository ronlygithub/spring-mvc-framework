package org.springframework.samples.app.spider.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderHousesTask extends AbstractSpiderTask{

	private String url;
	
	public SpiderHousesTask(String url) {
		this.url = url;
	}
	
	public String getHouserList(String url) {
		String html = get(url);	
		String projectId = url.substring(url.indexOf("=")+1, url.indexOf("&"));
		StringBuffer resultSet = new StringBuffer();
		Document doc = Jsoup.parse(html.substring(html.indexOf("可查询该楼栋详细信息")));
		Element tbody = doc.select("tbody").get(1);
		Elements trs = tbody.select("tr");
		for (int i = 1; i < trs.size(); i++) {
			Elements tds = trs.get(i).select("td");
			for (Element td : tds) {
				if (td.equals(tds.get(0))) {
					Elements a = td.getElementsByTag("a");
					if (a ==null || a.size()==0) {
						continue;
					}
					String href = a.get(0).attr("href");					
					String houseId = href.substring(href.indexOf("=")+1, href.indexOf("&"));
					resultSet.append(projectId).append(",").append(houseId).append(",");
				}
				String text = td.text();
				resultSet.append(text).append(",");
			}
			resultSet.deleteCharAt(resultSet.length()-1).append("\n");
		}
		
		return resultSet.toString();
	}

	@Override
	public String call() throws Exception {
		return getHouserList(url);
	}
	
	

}
