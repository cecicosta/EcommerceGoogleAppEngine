package com.cloud.smartvendas.entities;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class UserDAOImpl implements UserDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public void addUser(User p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("User saved successfully, User Details="+p);
	}

	@Override
	public void updateUser(User p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(p);
		logger.info("User updated successfully, User Details="+p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> listUsers() {
		Session session = this.sessionFactory.getCurrentSession();
		List<User> UsersList = session.createQuery("from User").list();
		for(User p : UsersList){
			logger.info("User List::"+p);
		}
		return UsersList;
	}

	@Override
	public User getUserById(String login) {
		Session session = this.sessionFactory.getCurrentSession();		
		User p = null;
		try{
			p = (User) session.load(User.class, login);
			logger.info("User loaded successfully, User details="+p);
		}catch(Exception e){
			p = null;
			logger.info("Failed to load user");
		}
		return p;
	}

	@Override
	public void removeUser(String login) {
		Session session = this.sessionFactory.getCurrentSession();
		User p = (User) session.load(User.class, login);
		if(null != p){
			session.delete(p);
		}
		logger.info("User deleted successfully, User details="+p);
	}

}