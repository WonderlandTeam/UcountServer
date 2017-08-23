package cn.edu.nju.wonderland.ucountserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UcountserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(UcountserverApplication.class, args);
	}
}
