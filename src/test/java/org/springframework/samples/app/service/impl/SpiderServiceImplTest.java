package org.springframework.samples.app.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.app.service.ISpiderService;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
public class SpiderServiceImplTest extends AbstractContextControllerTests{
	
	@Autowired
	ISpiderService spiderService;
	@Test
	public void testGet() {
		String result = spiderService.get("http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp?page=1");
		assertNotNull(result);
	}
//	@Test
	public void testGetProject(){
		StringBuffer projectList = spiderService.getProjectList("http://www.syfc.com.cn/work/xjlp/new_buildingcx.jsp?page=1");
		assertNotNull(projectList);
	}
//	@Test
	public void testWriter(){
		spiderService.writer("this is a test", "e:\\testWriter.txt");
	}
	
	@Test
	public void testSpider(){
		spiderService.spider(1, 2);
	}
	
	

}
