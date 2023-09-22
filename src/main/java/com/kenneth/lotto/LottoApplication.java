package com.kenneth.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LottoApplication {

	public static void main(String[] args) {
		var handlers = SpringApplication.getShutdownHandlers();
		handlers.add(new Runnable() {
			@Override
			public void run() {
				ServletInitializer.em.close();
				ServletInitializer.emf.close();
				System.out.println("EntityManager Closed");
			}
		});
		SpringApplication.run(LottoApplication.class, args);
	}

}
