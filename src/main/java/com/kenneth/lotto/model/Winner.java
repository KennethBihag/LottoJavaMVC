package com.kenneth.lotto.model;

import jakarta.persistence.*;

@Entity
@Table(name="winner")
public class Winner{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="winner_id")
    private int id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "client_pick_id",name="client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(referencedColumnName = "winning_number_id",name = "winning_numer_id")
    private WinningNumber winningNumber;

    @Column(name="winner_individual_prize")
    private int prize;

    public Winner(){}
    public Winner(Client client,WinningNumber winningNumber,int prize){
        this.client = client;
        this.winningNumber = winningNumber;
        this.prize = prize;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public WinningNumber getWinningNumber() {
        return winningNumber;
    }
    public void setWinningNumber(WinningNumber winningNumber) {
        this.winningNumber = winningNumber;
    }

    public int getPrize() {
        return prize;
    }
    public void setPrize(int prize) {
        this.prize = prize;
    }
}
