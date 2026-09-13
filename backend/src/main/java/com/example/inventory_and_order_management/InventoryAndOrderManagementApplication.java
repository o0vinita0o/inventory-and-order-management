package com.example.inventory_and_order_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class InventoryAndOrderManagementApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
		SpringApplication.run(InventoryAndOrderManagementApplication.class, args);
	}
}
