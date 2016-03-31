package org.springframework.samples.app.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.samples.app.vo.User;
import org.springframework.stereotype.Component;
@Component("userMapper")
public interface UserMapper {
	@Delete("delete from user where id = #{id}")
    int deleteByPrimaryKey(Integer id);

	@Insert("insert into user(id,name,addr) values(#{id},#{name},#{addr})")
    int insert(User user);

    
	@Select("select id id,name name, addr addr from user where id=#{id}")
    User selectByPrimaryKey(Integer id);  
}