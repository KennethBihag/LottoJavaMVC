package com.kenneth.lotto.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
@Table(name="client")
public class Client implements LottoModel {
    @Id
    @Column(name="client_pick_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="client_name",columnDefinition = "VARCHAR(45)")
    private String name;

    @Column(name = "client_picks",columnDefinition = "VARCHAR(45)")
    @Convert(converter =  IntArrToJsonConverter.class)
    private int[] picks;

    public Client(){}
    public Client(String name,int[] picks) throws IllegalArgumentException{
        this.name = name;
        if(picks == null || picks.length != maxPicks)
            throw new IllegalArgumentException(
                    String.format("Picks must only be of %d numbers.",maxPicks));

        if(Arrays.stream(picks).asLongStream().anyMatch(pick ->
            (pick<minPick || pick>maxPick)))
                throw new IllegalArgumentException(
                    String.format("Picks must be in the range of %d-%d only.",minPick,maxPick));

        this.picks = picks;
        if(hasDuplicates())
            throw new IllegalArgumentException(
                    String.format("%s has duplicates.",getPicksString()));

        if(name.isBlank())
            throw new IllegalArgumentException("Client name can't be empty.");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int[] getPicks() {
        return picks;
    }

    public void setPicks(int[] picks) {
        this.picks = picks;
    }

    @Override
    public boolean equals(Object o){
        return id == ((Client)o).id;
    }

    @Override
    public String toString(){
        return String.format(
                "%s with ID %d picked %s",
                name,id,getPicksString()
        );
    }
}
