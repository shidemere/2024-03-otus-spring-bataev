package ru.otus.hw.configuration;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2TcpConfiguration {

    /**
     * Даёт возможность подключаться прямо из идеи
     * remote
     * host localhost port 9092
     * Database mem:testdb
     * URL - jdbc:h2:tcp://localhost:9092/mem:testdb
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp","-tcpAllowOthers","-tcpPort","9092");
    }

}
