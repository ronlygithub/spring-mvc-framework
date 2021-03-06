package org.springframework.samples.app.aop;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.app.service.IMDoorsService;
import org.springframework.samples.app.service.IUserService;
import org.springframework.samples.app.service.impl.MDoorsServiceImpl;
import org.springframework.samples.mvc.AbstractContextControllerTests;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
public class AopLogTest extends AbstractContextControllerTests{

	@Autowired
	IMDoorsService mDoorsService;
	@Resource
	IUserService userService;
	
	@Test
	@Transactional
	@Rollback(true)
	public void test() {
		mDoorsService.insertBook();
	}

}
