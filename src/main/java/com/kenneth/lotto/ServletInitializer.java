package com.kenneth.lotto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	public static final String database = "world";
	public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(
			"com.kenneth."+database);
	public static final EntityManager em = emf.createEntityManager();

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LottoApplication.class);
	}

}
