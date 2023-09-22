package com.kenneth.lotto.model;

public interface LottoModel {
    int maxPicks = 6;
    int minPick = 1;
    int maxPick = 45;

    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    int[] getPicks();

    void setPicks(int[] picks);
}
