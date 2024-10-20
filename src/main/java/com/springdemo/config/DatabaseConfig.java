package com.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.springdemo.repository")
@EnableTransactionManagement
@PropertySource({"classpath:database.properties", "classpath:hibernate.properties"})
public class DatabaseConfig {

  @Autowired
  Environment env;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
    dataSource.setUsername(env.getRequiredProperty("spring.datasource.username"));
    dataSource.setPassword(env.getRequiredProperty("spring.datasource.password"));
    dataSource.setUrl(env.getRequiredProperty("spring.datasource.url"));
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setGenerateDdl(true);
    adapter.setShowSql(true);

    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setDataSource(dataSource());
    entityManagerFactoryBean.setPackagesToScan("com.springdemo.entity");

    entityManagerFactoryBean.setJpaVendorAdapter(adapter);
    entityManagerFactoryBean.setJpaProperties(getHibernateProperties());
    return entityManagerFactoryBean;
  }

  private Properties getHibernateProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
    properties.setProperty("hibernate.hbm2ddl.auto",
        env.getRequiredProperty("hibernate.hbm2ddl.auto"));
    return properties;
  }


  @Bean
  public PlatformTransactionManager transactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    return transactionManager;
  }

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

}
