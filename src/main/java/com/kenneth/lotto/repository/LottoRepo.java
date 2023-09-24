package com.kenneth.lotto.repository;

import java.util.*;

import jakarta.persistence.*;

import com.kenneth.lotto.model.LottoModel;

public interface LottoRepo {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
            System.getProperty("persistenceUnitName") == null ?
                    "com.kenneth.lotto" : System.getProperty("persistenceUnitName")
    );
    EntityManager em = emf.createEntityManager();
    EntityTransaction et = em.getTransaction();
    List<? extends LottoModel> getAllObjects(Class<? extends LottoModel> modelClass);
    LottoModel getOne(Class<? extends LottoModel> modelClass,String key);
    boolean createOne(Class<? extends LottoModel> modelClass,Object ... args);
    int createModels(Object data,Class<? extends LottoModel> modelClass);
}
