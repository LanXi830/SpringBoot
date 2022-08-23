package com.qf.CJDX_MANAGER;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.qf.CJDX_MANAGER.mapper")
@SpringBootApplication
public class CjdxManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CjdxManagerApplication.class, args);
	}

}
