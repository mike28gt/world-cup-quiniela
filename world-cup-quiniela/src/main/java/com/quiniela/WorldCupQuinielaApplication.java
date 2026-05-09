package com.quiniela;

import com.quiniela.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class WorldCupQuinielaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldCupQuinielaApplication.class, args);
	}

}
