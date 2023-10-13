package com.kenneth.lotto.repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.service.LottoService.Prize;

@Repository
@Transactional
public class LottoRepoImpl implements LottoRepo {
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private WinningNumberRepo winningNumberRepo;
    @Autowired
    private WinnerRepo winnerRepo;

    private List<Client> clientCache;
    private List<WinningNumber> winningCache;
    private List<Winner> winnerCache;
    private void readyCache(){
        if(clientCache==null || winningCache==null || winnerCache==null) {
            clientCache = (List<Client>) getAllFromDB(Client.class);
            winningCache = (List<WinningNumber>) getAllFromDB(WinningNumber.class);
            winnerCache = (List<Winner>) getAllFromDB(Winner.class);
        }
    }

    @Override
    public List<?> getAllObjects(Class<?> modelClass){
        readyCache();
        if(modelClass == Client.class){
            return clientCache;
        }
        else if (modelClass == WinningNumber.class) {
            return winningCache;
        }
        else if (modelClass == Winner.class) {
            return winnerCache;
        }
        else return null;
    }

    private List<?> getAllFromDB(Class<?> modelClass){
        switch (modelClass.getSimpleName()){
            case "Client":
                return new ArrayList<>(StreamSupport
                        .stream(clientRepo.findAll().spliterator(),false)
                        .toList());
            case "WinningNumber":
                return new ArrayList<>(StreamSupport
                        .stream(winningNumberRepo.findAll().spliterator(),false)
                        .toList());
            case "Winner":
                return new ArrayList<>(StreamSupport
                        .stream(winnerRepo.findAll().spliterator(),false)
                        .toList());
            default:
                return null;
        }
    }

    private boolean isEntryCached(String name, int[] entries){
        readyCache();
        for(Client c : clientCache){
            if(c.getName().equals(name) & LottoRepo.areSameArrays(c.getPicks(),entries))
                return true;
        }
        return false;
    }

    @Override
    public int createClients(Map<String,List<int[]>> entryData) {
        readyCache();
        int result=0;
        for(var nameAndEntries : entryData.entrySet()){
            String name = nameAndEntries.getKey();
            List<int[]> entriesList = nameAndEntries.getValue();
            for(int[] entries : entriesList){
                if(!isEntryCached(name,entries)) {
                    Client c = createClient(name, entries);
                    if( c != null ) {
                        clientCache.add(c);
                        result++;
                    }
                }
            }
        }

        return result;
    }

    private Client createClient(String name,int[] picks){
        try {
            Client client = new Client(name, picks);
            clientRepo.save(client);
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
        readyCache();
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
            winningCache.add(winningNumberRepo.save(wn));
            for(Winner w : tempWinners) {
                winnerRepo.save(w);
            }
            winnerCache.addAll(tempWinners);
            return true;
        }catch(Exception ignored){}

        return false;
    }
}
