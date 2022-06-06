package com.example.demo;

import com.example.demo.Service.Facade;
import com.example.demo.Service.IMarket;
import com.example.demo.api.apiObjects.bridge.proxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//		ConfigurableApplicationContext x = SpringApplication.run(DemoApplication.class, args);
//		IMarket facade = x.getBean(Facade.class);
//		IMarket proxy = x.getBean(com.example.demo.api.apiObjects.bridge.proxy.class);
	}



}
