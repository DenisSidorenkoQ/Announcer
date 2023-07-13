package com.exmple.task.integration.config;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfigIT {
    @Value("${test.container.port}")
    private int containerPort;
    @Value("${test.container.datasource.password}")
    private String password;
    @Value("${test.container.datasource.username}")
    private String username;
    @Value("${test.datasource.datasource.name}")
    private String databaseName;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public JdbcDatabaseContainer<?> jdbcDatabaseContainer() {


        return new PostgreSQLContainer<>("postgres:bullseye")
                .withExposedPorts(containerPort)
                .withUsername(username)
                .withPassword(password)
                .withDatabaseName(databaseName)
                .withReuse(true)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(containerPort), new ExposedPort(containerPort)))
                ))
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    public DataSource dataSource(JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcDatabaseContainer.getJdbcUrl());
        hikariConfig.setUsername(jdbcDatabaseContainer.getUsername());
        hikariConfig.setPassword(jdbcDatabaseContainer.getPassword());

        return new HikariDataSource(hikariConfig);
    }
}
