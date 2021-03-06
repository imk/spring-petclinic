package org.springframework.samples.petclinic;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {
	@Value("${spring.datasource.username}")
	private String user;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.url}")
	private String dataSourceUrl;

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	@Autowired
	private MetricRegistry registry;

	@Bean
	public DataSource primaryDataSource() {
		Properties dsProps = new Properties();
		dsProps.setProperty("url", dataSourceUrl);
		dsProps.setProperty("user", user);
		dsProps.setProperty("password", password);

		Properties configProps = new Properties();
		configProps.setProperty("driverClassName", driverClassName);
		configProps.setProperty("jdbcUrl", dataSourceUrl);

		HikariConfig hc = new HikariConfig(configProps);
		hc.setDataSourceProperties(dsProps);
		hc.setMetricRegistry(registry);
		return new HikariDataSource(hc);
	}

}
