package com.kenneth.lotto.service;

import java.util.List;
import java.util.Random;

import com.kenneth.lotto.model.LottoModel;

public interface LottoService {
    public enum Prize {
        NONE(0),THIRD(3),SECOND(4),FIRST(5),GRAND(6);
        private int n;
        Prize(int n){
            this.n = n;
        }
    }
    List<? extends LottoModel> getLottoModels();
    default void randomize(int[] input,int start){
        for (int i=start; i < input.length; ++i) {
            Random random = new Random();
            input[i] = random.nextInt(45) + 1;
        }
    }
}
