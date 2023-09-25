package com.kenneth.lotto.service;

import java.util.List;

import com.kenneth.lotto.model.LottoModel;

public interface LottoService {
    public enum Prize {
        NONE(0),THIRD(3),SECOND(4),FIRST(5),GRAND(6);
        private int n;
        Prize(int n){
            this.n = n;
        }
    }

    List<? extends LottoModel> getAll();
}
