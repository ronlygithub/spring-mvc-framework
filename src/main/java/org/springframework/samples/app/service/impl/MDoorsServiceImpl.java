package org.springframework.samples.app.service.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.app.dao.MDoorsMapper;
import org.springframework.samples.app.dao.UserMapper;
import org.springframework.samples.app.service.IMDoorsService;
import org.springframework.samples.app.vo.MDoors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("mDoorsService")
//@Transactional(readOnly=false,propagation = Propagation.REQUIRED)
public class MDoorsServiceImpl implements IMDoorsService{

	@Resource
	private MDoorsMapper mDoorsMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	
	private static String ADD_BOOK = "insert into book values(100023,'test')";
	
	@Override
//	@Transactional(readOnly=false,propagation = Propagation.NEVER)
	public MDoors getMDoorByPrimaryKey(String id) {
		return mDoorsMapper.getByPrimaryKey(id);
	}
	

//	@Transactional(readOnly=false,propagation = Propagation.REQUIRES_NEW)
	public void insertBook(){
		System.out.println("book added");
		jdbcTemplate.execute(ADD_BOOK);
//		throw new RuntimeException("ts");
		
		
	}

	@Override
	
	public void update(MDoors mDoor) {
		 mDoorsMapper.update(mDoor);
	}

	@Override
	@Transactional(readOnly=false,propagation = Propagation.REQUIRED)
	public void delete(String id)  {
		
		
		
	}
	
	@Transactional(readOnly=false,propagation=Propagation.NEVER)
	public void exception(String id) throws RuntimeException{
		mDoorsMapper.delete(id);
		throw new RuntimeException("denied");
	}

}
