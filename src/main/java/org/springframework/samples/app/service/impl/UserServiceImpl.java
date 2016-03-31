package org.springframework.samples.app.service.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.app.dao.UserMapper;
import org.springframework.samples.app.service.IMDoorsService;
import org.springframework.samples.app.service.IUserService;
import org.springframework.samples.app.vo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements IUserService {
	private static String ADD_USER = "insert into user values(100024,'test','test')";
	@Resource
	private UserMapper userDao;
	@Resource
	IMDoorsService mDoorsService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private SqlSessionFactory sqlSessionFactory;
	@Transactional(readOnly=false,propagation = Propagation.NEVER)
	public User getUserById(int userId) {
		return this.userDao.selectByPrimaryKey(userId);
	}
	
	@Transactional(readOnly=false,propagation = Propagation.REQUIRED)
	public void insertUser(){
		jdbcTemplate.execute(ADD_USER);
		System.out.println("user added");
		mDoorsService.insertBook();
	}
}
