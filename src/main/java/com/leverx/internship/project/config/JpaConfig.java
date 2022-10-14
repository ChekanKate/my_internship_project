package com.leverx.internship.project.config;

import com.leverx.internship.project.auditor.impl.AuditorAwareImpl;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.leverx.internship.project")
@EnableJpaRepositories(basePackages = "com.leverx.internship.project")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@PropertySource("classpath:application.properties")
public class JpaConfig {

  @Value("${scan.package}")
  private String scanPackage;

  @Value("${datasource.driver-class-name}")
  private String jdbDriverName;

  @Value("${datasource.url}")
  private String jdbcUrl;

  @Value("${datasource.username}")
  private String jdbcUsername;

  @Value("${datasource.password}")
  private String jdbcPassword;

  @Value("${hibernate.dialect}")
  private String hibernateDialect;

  @Value("${hibernate.show_sql}")
  private String hibernateShowSql;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(jdbDriverName);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(jdbcUsername);
    dataSource.setPassword(jdbcPassword);

    return dataSource;
  }

  @Bean
  public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setPackagesToScan("com.leverx.internship.project");
    sessionFactory.setHibernateProperties(hibernateProperties());
    return sessionFactory;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan(scanPackage);
    JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(hibernateProperties());
    return em;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }

  @Bean
  AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

  private Properties hibernateProperties() {
    Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.dialect", hibernateDialect);
    hibernateProperties.setProperty("show_sql", hibernateShowSql);
    return hibernateProperties;
  }
}
