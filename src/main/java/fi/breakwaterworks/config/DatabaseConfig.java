package fi.breakwaterworks.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Contains database configurations.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

	@Value("${hibernate.dialect}")
	private String databaseDialect;

	@Value("${hibernate.show_sql}")
	private String databaseShowDialect;

	@Value("${hibernate.hbm2ddl.auto}")
	private String databaseHBM2DLLAuto;

	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public static DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * Declare the JPA entity manager factory.
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource);

		entityManagerFactory.setPackagesToScan("fi.breakwaterworks");

		entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		// Hibernate properties
		Properties additionalProperties = new Properties();
		additionalProperties.put("hibernate.dialect", databaseDialect);
		additionalProperties.put("hibernate.show_sql", databaseShowDialect);
		additionalProperties.put("hibernate.hbm2ddl.auto", databaseHBM2DLLAuto);
		entityManagerFactory.setJpaProperties(additionalProperties);

		return entityManagerFactory;
	}

	/**
	 * Declare the transaction manager.
	 */
	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		return transactionManager;
	}

	/**
	 * PersistenceExceptionTranslationPostProcessor is a bean post processor which
	 * adds an advisor to any bean annotated with Repository so that any
	 * platform-specific exceptions are caught and then rethrown as one Spring's
	 * unchecked data access exceptions (i.e. a subclass of DataAccessException).
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;

} 