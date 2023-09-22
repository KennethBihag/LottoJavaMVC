package com.kenneth.lotto.model;

import java.util.Arrays;

import jakarta.persistence.*;

@Entity
@Table(name = "winning_number")
public class WinningNumber implements LottoModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "winning_number_id")
    private int id;

    @Column(name = "winning_number",columnDefinition = "VARCHAR(45)")
    @Convert(converter = IntArrToJsonConverter.class)
    private int[] picks;

    @Column(name = "winning_number_prize")
    private int prizePool;
    @Override
    public boolean equals(Object o){
        return id == ((WinningNumber)o).id;
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int[] getPicks() {
        return Arrays.copyOf(picks,picks.length);
    }

    @Override
    public void setPicks(int[] picks) {
        this.picks = picks;
    }
    public int getPrizePool(){ return prizePool;};
    public void setPrizePool(int prizePool){this.prizePool = prizePool;}
}
