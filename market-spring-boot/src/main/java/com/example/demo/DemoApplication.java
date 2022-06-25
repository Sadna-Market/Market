package com.example.demo;

import com.example.demo.DataAccess.Entity.DataProductType;
import com.example.demo.DataAccess.Services.ProductTypeService;
import com.example.demo.Domain.Market.Market;
import com.example.demo.Service.Facade;
import com.example.demo.Service.IMarket;
import com.example.demo.api.apiObjects.bridge.proxy;
import com.example.demo.configuration.JsonInit;
import com.example.demo.configuration.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.Map;
import java.util.Properties;


@SpringBootApplication
@EnableScheduling
public class DemoApplication {
    static Logger logger = Logger.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        Properties props = createProps();
        if (props != null) {
            new SpringApplicationBuilder(DemoApplication.class)
                    .properties(props).run(args);

        }
    }

    private static Properties createProps() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            config c = config.get_instance();
            Properties props = new Properties();
            props.put("server.port", c.getJsonInit().server_port);
            props.put("spring.datasource.url", c.getJsonInit().spring_datasource_url);
            props.put("spring.datasource.username", c.getJsonInit().spring_datasource_username);
            props.put("spring.datasource.password", c.getJsonInit().spring_datasource_password);
            props.put("spring.jpa.hibernate.ddl-auto", c.getJsonInit().spring_jpa_hibernate_ddl_auto);
            props.put("spring.jpa.show-sql", c.getJsonInit().spring_jpa_show_sql);
            props.put("spring.jpa.properties.hibernate.dialect", c.getJsonInit().spring_jpa_properties_hibernate_dialect);
            props.put("spring.jpa.properties.hibernate.format.sql", c.getJsonInit().spring_jpa_properties_hibernate_format_sql);
            return props;
        } catch (Exception e) {
            logger.warn("error in init file");
            return null;
        }
    }

}
