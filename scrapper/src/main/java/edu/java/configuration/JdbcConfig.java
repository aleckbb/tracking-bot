package edu.java.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SuppressWarnings("MultipleStringLiterals")
@Configuration
public class JdbcConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(/*"${spring.datasource.driver-class-name}"*/ "org.postgresql.Driver");
        dataSource.setUsername("postgres"/*"${spring.datasource.username}"*/);
        dataSource.setPassword(/*"${spring.datasource.password}"*/ "postgres");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/scrapper"/*"${spring.datasource.url}"*/);

        return dataSource;
    }

    @Bean
    public JdbcClient jdbcClient(@Autowired DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }
}
