package org.springframework.samples.app.service;

import org.springframework.samples.app.vo.User;

public interface IUserService {
	public User getUserById(int userId);
	public void insertUser();
}
