package com.kenneth.lotto.service;

public interface LottoService {
    public enum Prize {
        NONE(0),THIRD(3),SECOND(4),FIRST(5),GRAND(6);
        private int n;
        Prize(int n){
            this.n = n;
        }
    }
}
