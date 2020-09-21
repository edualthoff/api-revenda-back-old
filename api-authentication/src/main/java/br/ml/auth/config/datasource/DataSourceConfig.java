package br.ml.auth.config.datasource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties
public class DataSourceConfig {
	private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

	
	@Bean
	@ConfigurationProperties(prefix="oauth.datasource")
	public DataSourceProperties secondDataSourceProperties() {
	    return new DataSourceProperties();
	}
	
	@Bean(name="oauthJdbcData")
	//@ConfigurationProperties(prefix="oauth.datasource")
    public HikariDataSource dataSourceOauth() {
		log.debug("DataSource create - segundo banco de dados");
		return this.secondDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    
    @Bean(name="oauthJdbcTemplate")
    public JdbcTemplate oauthJdbcTemplate(@Qualifier("oauthJdbcData") DataSource dataSourceOauth){
    	return new JdbcTemplate(dataSourceOauth);
    }
}
