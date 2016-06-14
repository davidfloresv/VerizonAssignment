package com.verizon.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main app to run the boots
 * 
 * @author David Flores
 *
 */

@SpringBootApplication
@EnableTransactionManagement
public class App 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
