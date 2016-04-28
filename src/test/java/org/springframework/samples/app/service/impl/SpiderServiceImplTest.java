package org.springframework.samples.app.service.impl;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.app.service.ISpiderService;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;
@RunWith(SpringJUnit4ClassRunner.class)
public class SpiderServiceImplTest extends AbstractContextControllerTests{
	
	@Autowired
	ISpiderService spiderService;
//	@Test
	public void testGet() {
		String result = spiderService.get("http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp?page=1");
		assertNotNull(result);
	}
//	@Test
	public void testGetProject(){
		StringBuffer projectList = spiderService.getProjectList("http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp?page=1");
		assertNotNull(projectList);
	}
	@Test
	public void testWriter(){
		spiderService.writer("this is a test", "e:\\testWriter.txt");
	}
	@Test
	public void testReader(){
		String reader = spiderService.reader("e:\\testWriter.txt").trim();
		assertEquals(reader, "this is a test");
	}
	
//	@Test
	public void testSpider(){
		spiderService.spider(1, 110);
	}
	
	@Test
	public void testConfict(){
		String reader = spiderService.reader("e:\\syfc\\housesList.txt");
		String[] split = reader.split("\n");
		Set<String> set = new HashSet<String>();
		for (String house : split) {
			set.add(house);
		}
		
		StringBuffer buffer = new StringBuffer();
		for (String house : set) {
			if (StringUtils.isEmpty(house)) {
				continue;
			}
			buffer.append(house).append("\n");
		}
		spiderService.writer(buffer.toString(), "e:\\syfc\\houses.txt");
	}
	
	

}
