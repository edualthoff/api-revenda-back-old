package br.ml.auth.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableJpaRepositories(basePackages = {"br.ml.auth.*"}, entityManagerFactoryRef = "primaryDSEmFactory", transactionManagerRef = "primaryDSTransactionManager")
@EnableTransactionManagement
@Order(0)
public class PrimaryDataSource {
	//private static final Logger log = LogManager.getLogger(PrimaryDataSource.class);

	@Primary
	@Bean
	@ConfigurationProperties("primary.datasource")
	public DataSourceProperties primaryDSProperties() {
		return new DataSourceProperties();
	}
	
	@Primary
	@Bean
	public HikariDataSource primaryDS(@Qualifier("primaryDSProperties") DataSourceProperties primaryDSProperties) {
		return primaryDSProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean primaryDSEmFactory(@Qualifier("primaryDS") DataSource primaryDS, EntityManagerFactoryBuilder builder) {
		return builder.dataSource(primaryDS).packages(new String[] {"br.ml.auth.*"}).build();
	}
	
	@Primary
	@Bean
	public PlatformTransactionManager primaryDSTransactionManager(EntityManagerFactory primaryDSEmFactory) {
		return new JpaTransactionManager(primaryDSEmFactory);
	}
}
