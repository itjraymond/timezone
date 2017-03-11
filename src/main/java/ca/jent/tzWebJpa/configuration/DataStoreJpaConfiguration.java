package ca.jent.tzWebJpa.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages="ca.jent.tzWebJpa")
@EnableJpaRepositories(basePackages="ca.jent.tzWebJpa.repositories")
@EnableTransactionManagement
public class DataStoreJpaConfiguration {
	
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setName("h2test")
				.setType(EmbeddedDatabaseType.H2)
				.setScriptEncoding("UTF-8")
				.ignoreFailedDrops(true)
				.addScript("h2.schema.sql")
				.addScript("h2.data.sql")
				.build();
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabase(Database.H2);
		adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return adapter;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		
		Properties props = new Properties();
		props.setProperty("hibernate.format_sql", String.valueOf(true));
		
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setJpaVendorAdapter(jpaVendorAdapter);
		emf.setPackagesToScan("ca.jent.tzWebJpa.entities");
		emf.setJpaProperties(props);
		
		return emf;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	@Bean
	public BeanPostProcessor persistenceTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

}
