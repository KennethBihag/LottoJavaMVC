package com.kenneth.lotto.model;

public class Winner{
    private int id;
    private Client client;
    private WinningNumber winningNumber;
    private int prize;

    public Winner(Client client, WinningNumber winningNumber, int prize){
        this.client = client;
        this.winningNumber = winningNumber;
        this.prize = prize;
    }
}
