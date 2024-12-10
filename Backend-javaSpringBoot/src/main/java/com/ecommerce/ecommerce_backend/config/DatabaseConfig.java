package com.ecommerce.ecommerce_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        // Cargar las variables de entorno desde el archivo .env
        Dotenv dotenv = Dotenv.load();
        
        return DataSourceBuilder.create()
            .url("jdbc:mysql://" + dotenv.get("DB_HOST") + ":" + 
                 dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME"))
            .username(dotenv.get("DB_USER"))
            .password(dotenv.get("DB_PASSWORD"))
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();
    }
}