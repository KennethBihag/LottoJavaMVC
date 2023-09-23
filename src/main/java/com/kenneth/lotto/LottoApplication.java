package com.kenneth.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.kenneth.lotto.repository.LottoRepo.*;

@SpringBootApplication
public class LottoApplication {

	public static void main(String[] args) {

		var handlers = SpringApplication.getShutdownHandlers();
		handlers.add(new Runnable() {
			@Override
			public void run() {
				em.close();
				emf.close();
				System.out.println("EntityManager Closed");
			}
		});

		SpringApplication.run(LottoApplication.class, args);

	}

}
