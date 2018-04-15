package com.cloud.smartvendas.entities;

import java.util.List;

public interface UserDAO {

	public void addUser(User p);
	public void updateUser(User p);
	public List<User> listUsers();
	public User getUserById(String id);
	public void removeUser(String id);
}