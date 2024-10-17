package ru.otus.hw.config;

import org.h2.tools.Server;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Позволяет запустить H2 сервер до Spring Shell. 
 */
@Configuration
public class H2TcpConfiguration implements SmartLifecycle {

    /**
     * Даёт возможность подключаться прямо из идеи
     * remote
     * host localhost port 9092
     * Database mem:testdb
     * URL - jdbc:h2:tcp://localhost:9092/mem:testdb
     */
    private Server server;

    private boolean running = false;

    @Override
    public void start() {
        try {
            server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
            running = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
