package com.quiniela;

import org.springframework.boot.SpringApplication;

public class TestWorldCupQuinielaApplication {

	public static void main(String[] args) {
		SpringApplication.from(WorldCupQuinielaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
