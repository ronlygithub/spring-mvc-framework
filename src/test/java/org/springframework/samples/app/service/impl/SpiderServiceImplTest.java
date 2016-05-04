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
//	@Test
	public void testWriter(){
		spiderService.exec("create table houses_05_04 like houses");
//		spiderService.exec("load data infile 'e:\\syfc\\housesList.txt' into table houses_05_04 fields terminated by ',' lines terminated by '\n'");
//		spiderService.writer("this is a test", "e:\\testWriter.txt");
	}	
	
	@Test
	public void testSpider(){
		spiderService.spider(1, 111);
	}	

}
