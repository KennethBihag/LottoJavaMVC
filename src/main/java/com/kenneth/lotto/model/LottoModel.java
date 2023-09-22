package com.kenneth.lotto.model;

public interface LottoModel {
    int maxPicks = 6;
    int getId();

    void setId(int id);

    int[] getPicks();

    void setPicks(int[] picks);

    default String getPicksString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<maxPicks;++i) {
            sb.append(getPicks()[i]);
            if(i<maxPicks-1)
                sb.append('-');
        }
        return sb.toString();
    }
}
