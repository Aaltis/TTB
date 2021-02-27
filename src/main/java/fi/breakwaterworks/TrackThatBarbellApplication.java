package fi.breakwaterworks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("fi.breakwaterworks")
public class TrackThatBarbellApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TrackThatBarbellApplication.class, args);
	}
 
}
