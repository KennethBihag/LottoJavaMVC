package com.kenneth.lotto.repository;

import java.util.*;

import com.kenneth.lotto.model.LottoModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public interface LottoRepo {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
            System.getProperty("persistenceUnitName") == null ?
                    "com.kenneth.lotto" : System.getProperty("persistenceUnitName")
    );
    EntityManager em = emf.createEntityManager();
    Collection<? extends LottoModel> getAllModels();
}
