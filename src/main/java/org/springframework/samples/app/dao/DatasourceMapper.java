package org.springframework.samples.app.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.samples.app.vo.Datasource;
import org.springframework.stereotype.Component;

@Component("DatasourceMapper")
public interface DatasourceMapper {
	
	@Select("select id, url,user,password,driver from syfc_ds where id='001'")
	public Datasource get();
}
