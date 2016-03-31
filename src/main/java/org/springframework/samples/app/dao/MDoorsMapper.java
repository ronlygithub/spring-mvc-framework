package org.springframework.samples.app.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.samples.app.vo.MDoors;

public interface MDoorsMapper {

	@Select("select * from doors where id=#{id}")
	public MDoors getByPrimaryKey(String id);
	
	@Update("update doors set type=#{type},status=#{status} where id=#{id}")
	public void update(MDoors mDoor);
	
	@Delete("delete from doors where id=#{id}")
	public void delete(String id);
}
