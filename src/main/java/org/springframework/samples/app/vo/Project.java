package org.springframework.samples.app.vo;

import java.sql.Timestamp;

public class Project {
	private String id;
	private String name;
	private String location;
	private Timestamp openTime;
	private String componey;
	private String componeyAddr;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Timestamp getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}
	public String getComponey() {
		return componey;
	}
	public void setComponey(String componey) {
		this.componey = componey;
	}
	public String getComponeyAddr() {
		return componeyAddr;
	}
	public void setComponeyAddr(String componeyAddr) {
		this.componeyAddr = componeyAddr;
	}
	
}
