package com.javaproject.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }) // (exclude = {
																		// DataSourceAutoConfiguration.class }): bỏ auto
																		// tạo database khi chạy. nếu có data base r thì
																		// bỏ đoạn code này đi
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

	}

}
