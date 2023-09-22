package com.kenneth.lotto.service;

import java.util.List;
import java.util.Random;

import com.kenneth.lotto.ServletInitializer;
import com.kenneth.lotto.model.Client;
import com.kenneth.lotto.model.LottoModel;
import jakarta.persistence.EntityManager;

public interface LottoService {
    EntityManager em = ServletInitializer.em;
    List<? extends LottoModel> getLottoModels();
    default void randomize(int[] input,int start){
        for (int i=start; i < input.length; ++i) {
            Random random = new Random();
            input[i] = random.nextInt(45) + 1;
        }
    }
}
