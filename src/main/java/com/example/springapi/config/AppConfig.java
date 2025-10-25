package com.example.springapi.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages="com.example.springapi")
@EnableTransactionManagement

@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        // Enables resolution of ${...} placeholders in @Value annotations
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Value("${spring.datasource.url:jdbc:postgresql://db:5432/spring_db}")
    private String dbUrl;
    
    @Value("${spring.datasource.username:user}")
    private String dbUsername;
    
    @Value("${spring.datasource.password:password}")
    private String dbPassword;
    
    @Value("${spring.jpa.hibernate.ddl-auto:update}")
    private String hibernateDdlAuto;
    
    @Value("${spring.jpa.show-sql:false}")
    private boolean showSql;
    
    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(dbUrl);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);
        
        // Configuration du pool de connexions
        ds.setInitialSize(5);
        ds.setMaxTotal(20);
        ds.setMaxIdle(10);
        ds.setMinIdle(5);
        ds.setMaxWaitMillis(60000);
        ds.setValidationQuery("SELECT 1");
        ds.setTestOnBorrow(true);
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(30000);
        
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.springapi.model");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", hibernateDdlAuto);
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.show_sql", showSql);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.use_sql_comments", true);
        jpaProperties.put("hibernate.jdbc.batch_size", 20);
        jpaProperties.put("hibernate.order_inserts", true);
        jpaProperties.put("hibernate.order_updates", true);
        jpaProperties.put("hibernate.jdbc.batch_versioned_data", true);
        
        emf.setJpaProperties(jpaProperties);
        
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}