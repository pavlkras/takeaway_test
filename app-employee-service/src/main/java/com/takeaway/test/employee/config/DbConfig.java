package com.takeaway.test.employee.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 20/02/2019
 */

@Slf4j
@Configuration
public class DbConfig {
    private static final String CONNECTION_TEST_QUERY = "SELECT 1";

    @Bean
    public NamedParameterJdbcTemplate postgresNamedParameterJdbcTemplate(HikariDataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }


    @Bean
    public HikariDataSource postgresHikariDataSource(@Value("${hikari.postgres.jdbc-url}") String jdbcUrl,
                                                     @Value("${hikari.postgres.username}") String username,
                                                     @Value("${hikari.postgres.password}") String password,
                                                     @Value("${hikari.postgres.pool.size}")int poolSize) {
        HikariDataSource hikariDataSource = dataSource(jdbcUrl, username, password, poolSize, "PostgreSQL-HikariPool", "org.postgresql.Driver");
        log.info("postgresHikariDataSource initialized - postgresJdbcUrl: {}, postgresPoolSize: {}", jdbcUrl, poolSize);
        return hikariDataSource;
    }

    private HikariDataSource dataSource(String jdbcUrl, String username, String password, int poolSize, String poolName, String driverClassName) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(jdbcUrl);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setMaximumPoolSize(poolSize);
        hikariDataSource.setConnectionTestQuery(CONNECTION_TEST_QUERY);
        hikariDataSource.setPoolName(poolName);
        if (driverClassName != null) {
            hikariDataSource.setDriverClassName(driverClassName);
        }
        return hikariDataSource;
    }

}
