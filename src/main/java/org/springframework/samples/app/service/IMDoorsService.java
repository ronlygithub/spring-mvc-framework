package org.springframework.samples.app.service;

import org.springframework.samples.app.vo.MDoors;

public interface IMDoorsService {
	
	public MDoors getMDoorByPrimaryKey(String id);
	public void update(MDoors mDoor);
	public void delete(String id);		
	public void insertBook() ;
	public void exception(String id) throws Exception;
}
