package com.cloud.smartvendas.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.cloud.smartvendas.aws.helpers.AwsProperties;
import com.cloud.smartvendas.controllers.LoginController;
import com.cloud.smartvendas.entities.UserDAOImpl;
import com.cloud.smartvendas.nosql.entities.Log;

@EnableTransactionManagement
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {LoginController.class, UserDAOImpl.class, Log.class})
public class SpringConfigMvc extends WebMvcConfigurerAdapter {
	
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
 
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
       LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
       sessionFactory.setDataSource(getDataSource());
       sessionFactory.setPackagesToScan(
         new String[] {"com.cloud.smartvendas.entities"});
       sessionFactory.setHibernateProperties(additionalProperties());
  
       return sessionFactory;
    }

   
    @Bean()
    public DataSource getDataSource()
    {
        DriverManagerDataSource ds = new DriverManagerDataSource();        
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl(AwsProperties.Properties.rdsUrl.getValue());
        ds.setUsername(AwsProperties.Properties.rdsUserName.getValue());
        ds.setPassword(AwsProperties.Properties.rdsPassword.getValue());           
        return ds;
    }
    
    Properties additionalProperties() {
	   Properties properties = new Properties();
	   properties.setProperty("hibernate.show_sql", "true");
	   properties.setProperty("hibernate.hbm2ddl.auto", "update");
	   properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
	   return properties;
	}
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        HibernateTransactionManager htm = new HibernateTransactionManager();
        htm.setSessionFactory(sessionFactory);
        return htm;
    }
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(3*1024*1024);
        return multipartResolver;
    }
    
    @Bean
    @Autowired
    public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory)
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
        return hibernateTemplate;
    }
        

}