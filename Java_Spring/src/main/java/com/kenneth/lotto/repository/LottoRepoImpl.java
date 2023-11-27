package com.kenneth.lotto.repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.service.LottoService.Prize;

@Repository
public class LottoRepoImpl implements LottoRepo{
    private final List<Client> clientCache = (List<Client>)getAllFromDB(Client.class);
    private final List<WinningNumber> winningCache = (List<WinningNumber>)getAllFromDB(WinningNumber.class);
    private final List<Winner> winnerCache = (List<Winner>)getAllFromDB(Winner.class);

    @Override
    public List<?> getAllObjects(Class<?> modelClass){
        if(modelClass == Client.class)
            return clientCache;
        else if (modelClass == WinningNumber.class)
            return winningCache;
        else if (modelClass == Winner.class)
            return winnerCache;
        else return null;
    }

    private List<?> getAllFromDB(Class<?> modelClass){
        String modelName = modelClass.getSimpleName();
        String jpql = String.format("SELECT m FROM %s m",modelName);
        Query query = em.createQuery(jpql, modelClass);
        return query.getResultList();
    }

    private <T> void addToCache(T toAdd){
        switch (toAdd.getClass().getSimpleName()){
            case "Client":
                if(!clientCache.contains(toAdd))
                    clientCache.add((Client)toAdd);
                break;
            case "WinningNumber":
                if(!winningCache.contains(toAdd))
                    winningCache.add((WinningNumber) toAdd);
                break;
            case "Winner":
                if(!winnerCache.contains(toAdd))
                    winnerCache.add((Winner)toAdd);
                break;
        }
    }
    private boolean isEntryCached(String name, int[] entries){
        for(Client c : clientCache){
            if(c.getName().equals(name) & LottoRepo.areSameArrays(c.getPicks(),entries))
                return true;
        }
        return false;
    }

    @Override
    public int createClients(Map<String,List<int[]>> entryData) {
        int result=0;

        for(var nameAndEntries : entryData.entrySet()){
            String name = nameAndEntries.getKey();
            List<int[]> entriesList = nameAndEntries.getValue();
            for(int[] entries : entriesList){
                if(!isEntryCached(name,entries)) {
                    if(createClient(name, entries) != null)
                        result++;
                }
            }
        }

        return result;
    }

    private Client createClient(String name,int[] picks){
        try {
            Client client = new Client(name, picks);
            et.begin();
            em.persist(client);
            et.commit();
            clientCache.add(client);
            return client;
        }catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean createWinningNumber(int prizePool){
        WinningNumber wn = new WinningNumber(prizePool);
        AtomicInteger sharedPrize = new AtomicInteger(0);
        Map.Entry<WinningNumber,Map<Client, Prize>> winners =
                getWinnersFor1Picks(wn,clientCache,sharedPrize);
        List<Winner> tempWinners = new ArrayList<>();
        for(Map.Entry<Client,Prize> e : winners.getValue().entrySet()){
            Prize cPrizeType = e.getValue();
            int cPrize = switch (cPrizeType){
                case THIRD -> 20;
                case SECOND -> 500;
                case FIRST -> 20000;
                case GRAND -> sharedPrize.get();
                default -> throw new RuntimeException("Impossible case: Client must have a prize.");
            };
            tempWinners.add(new Winner(e.getKey(),wn,cPrize));
        }
        try{
            et.begin();
            em.persist(wn);
            winningCache.add((WinningNumber) wn);
            for(Winner w : tempWinners) {
                em.persist(w);
            }
            et.commit();
            winnerCache.addAll(tempWinners);
            return true;
        }catch(Exception ignored){}

        return false;
    }
}
