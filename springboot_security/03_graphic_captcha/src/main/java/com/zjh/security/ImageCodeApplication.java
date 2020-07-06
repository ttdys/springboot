package com.zjh.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

import javax.sql.DataSource;

/**
 * @author zjh on 2020/7/6
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ImageCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageCodeApplication.class);
    }

}
