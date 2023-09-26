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
    @JoinColumn(referencedColumnName = "winning_number_id",name = "winning_number_id")
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

    @Override
    public boolean equals(Object o){
        return id == ((Winner)o).id;
    }
    @Override
    public String toString(){
        String format =
                "{winner_id:%d,client_id:%d,winning_number_id:%d,winner_individual_prize:%d}";
        return String.format(format,id,client.getId(),winningNumber.getId(),prize);
    }
    public static class WinnerDto{
        public int winner_id;
        public int client_id;
        public int winning_number_id;
        public int winner_individual_prize;
        public WinnerDto(Winner w){
            winner_id = w.id;
            client_id = w.client.getId();
            winning_number_id = w.winningNumber.getId();
            winner_individual_prize = w.prize;
        }
    }
}
