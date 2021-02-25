package com.DB.FruitSalad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DialogFlowSpringApplication {
	public static void main(String[] args) {
		try {
			SpringApplication.run(DialogFlowSpringApplication.class, args);
		} catch (Exception e) {
			if (e.toString() != "org.springframework.boot.devtools.restart.SilentExitExceptionHandler$SilentExitException")
				e.printStackTrace();
		}
	}
}
