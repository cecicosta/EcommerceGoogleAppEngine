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
public class ProductDAOImpl implements ProductDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf){
		this.sessionFactory = sf;
	}

	@Override
	public void addProduct(Product p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(p);
		logger.info("Product saved successfully, Product Details="+p);
	}

	@Override
	public void updateProduct(Product p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.merge(p);
		logger.info("Product updated successfully, Product Details="+p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> listProducts() {
		Session session = this.sessionFactory.getCurrentSession();
		List<Product> ProductsList = session.createQuery("from Product").list();
		for(Product p : ProductsList){
			logger.info("Product List::"+p);
		}
		return ProductsList;
	}

	@Override
	public Product getProductById(String name) {
		Session session = this.sessionFactory.getCurrentSession();		
		Product p = null;
		try{
			p = (Product) session.load(Product.class, name);
			logger.info("Product loaded successfully, Product details="+p);
		}catch(Exception e){
			p = null;
			logger.info("Failed to add product - message: " + e.getMessage());
		}
		return p;
	}

	@Override
	public void removeProduct(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		Product p = (Product) session.load(Product.class, name);
		if(null != p){
			session.delete(p);
		}
		logger.info("Product deleted successfully, Product details="+p);
	}

}