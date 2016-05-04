package org.springframework.samples.app.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.samples.app.vo.Project;
import org.springframework.stereotype.Component;

@Component("ProjectMapper")
public interface ProjectMapper {

	@Select("select * from projects where id=#{id}")
	public Project get(String id);
	
	@Insert("insert into projects values(#{id},#{name},#{location},#{openTime},#{componey},#{componeyAddr})")
	public void add(Project project);
}
