package com.kenneth.lotto.repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.kenneth.lotto.model.Client;
import com.kenneth.lotto.model.WinningNumber;
import jakarta.persistence.*;

import com.kenneth.lotto.model.LottoModel;
import com.kenneth.lotto.service.LottoService.Prize;

public interface LottoRepo {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory(
            System.getProperty("persistenceUnitName") == null ?
                    "com.kenneth.lotto" : System.getProperty("persistenceUnitName")
    );
    EntityManager em = emf.createEntityManager();
    EntityTransaction et = em.getTransaction();

    static boolean areSameArrays(int[] a, int[] b){
        for(int i=0; i<LottoModel.maxPicks; ++i)
            if(a[i]!=b[i])
                return false;
        return true;
    };

    List<?> getAllObjects(Class<?> modelClass);

    int createClients(Map<String,List<int[]>> entryData);

    boolean createWinningNumber(int prizePool);

    default Map.Entry<WinningNumber,Map<Client, Prize>> getWinnersFor1Picks(
            WinningNumber winningPick, List<Client> clients, AtomicInteger sharedPrize) {
        Map<Client, Prize> clientPrizeMap = new HashMap<>();
        Map.Entry<WinningNumber,Map<Client, Prize>> result =
                new AbstractMap.SimpleEntry<>(winningPick, clientPrizeMap);
        try{
            int sharers=0;
            for(Client c : clients){
                Prize p = checkPrize(c,winningPick);
                if(p.ordinal() > Prize.NONE.ordinal()) {
                    result.getValue().put(c, p);
                    if(p == Prize.GRAND)
                        sharers++;
                    else{
                        int cPrize = switch (p){
                            case THIRD -> 20;
                            case SECOND -> 500;
                            case FIRST -> 20000;
                            default -> throw new RuntimeException("Impossible case: Client must have a prize!");
                        };
                        winningPick.setPrizePool(winningPick.getPrizePool()-cPrize);
                        if(winningPick.getPrizePool()<1)
                            winningPick.setPrizePool(0);
                    }
                }
            }
            if(sharedPrize!=null)
                sharedPrize.set(winningPick.getPrizePool()/sharers);
        } catch (Exception ignored){}
        return result;
    }

    default Prize checkPrize(Client client, WinningNumber winningNumber){
        int[] arr1 = client.getPicks();
        int[] arr2 = winningNumber.getPicks();
        List<Integer> matches = new LinkedList<>();
        List<Integer> searched = new LinkedList<>();
        for(int i=0;i<arr1.length;++i){
            if(searched.contains(arr1[i]))
                continue;
            searched.add(arr1[i]);
            for(int j=0;j<arr2.length;++j)
                if(arr1[i]==arr2[j]){
                    matches.add(arr2[j]);
                    break;
                }
        }
        switch (matches.size()){
            case 3:
                return Prize.THIRD;
            case 4:
                return Prize.SECOND;
            case 5:
                return Prize.FIRST;
            case 6:
                return Prize.GRAND;
            default:
                return Prize.NONE;
        }
    }

}
