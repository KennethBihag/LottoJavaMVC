package com.kenneth.lotto.repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.kenneth.lotto.service.LottoService;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.service.LottoService.Prize;

@Repository
public class LottoRepoImpl implements LottoRepo{
    private final List<Client> clientCache = new ArrayList<>();
    private final List<WinningNumber> winningCache = new ArrayList<>();
    private final List<Winner> winnerCache = new ArrayList<>();

    @Override
    public List<? extends LottoModel> getAllObjects(Class<? extends LottoModel> modelClass){
        if(modelClass == Client.class) {
            if(clientCache.size()<1) {
                List<Client> clientsFromDb = (List<Client>) getAllFromDB(Client.class);
                clientCache.addAll(clientsFromDb);
            }
            return clientCache;
        }
        else if (modelClass == WinningNumber.class) {
            if (winningCache.size() < 1) {
                List<WinningNumber> winningsFromDb = (List<WinningNumber>) getAllFromDB(WinningNumber.class);
                winningCache.addAll(winningsFromDb);
            }
            return winningCache;
        }
        return null;
    }
    private List<? extends LottoModel> getAllFromDB(Class<? extends LottoModel> modelClass){
        String modelName = modelClass.getSimpleName();
        String jpql = String.format("SELECT m FROM %s m",modelName);
        Query query = em.createQuery(jpql, modelClass);
        return query.getResultList();
    }

    @Override
    public LottoModel getOne(Class<? extends LottoModel> modelClass,String key){
        LottoModel result = null;
        if(modelClass==Client.class){
            result = getFromCache(Client.class,key);
            if(result == null){
                result = getFromDb(Client.class,key);
                if(result != null)
                    clientCache.add((Client)result);
            }
        }else if (modelClass==WinningNumber.class){
            result = getFromCache(WinningNumber.class,key);
            if(result == null) {
                result = getFromDb(WinningNumber.class, key);
                if(result != null)
                    winningCache.add((WinningNumber)result);
            }
        }
        return result;
    }
    private LottoModel getFromCache(Class<? extends LottoModel> classModel,String key){
        LottoModel result = null;
        try {
            if (classModel == Client.class) {
                result = clientCache.stream()
                        .filter(c -> c.getName().equals(key))
                        .findFirst().get();
            } else if (classModel == WinningNumber.class) {
                result = winningCache.stream()
                        .filter(w -> w.getId() == Integer.parseInt(key))
                        .findFirst().get();
            }
        } catch (NoSuchElementException ignored){}
        return result;
    }
    private LottoModel getFromDb(Class<? extends LottoModel> classModel,String key){
        LottoModel result = null;
        String jpql = null;
        Query query = null;
        try {
            if (classModel == Client.class) {
                jpql = String.format("SELECT c FROM Client c WHERE c.name = '%s'", key);
                query = em.createQuery(jpql, Client.class);
                result = (Client) query.getSingleResult();
            } else if (classModel == WinningNumber.class) {
                int idKey = Integer.parseInt(key);
                jpql = String.format("SELECT w FROM WinningNumber w WHERE w.id = %d", idKey);
                query = em.createQuery(jpql, WinningNumber.class);
                result = (WinningNumber) query.getSingleResult();
            }
        } catch (NoResultException ignored){}

        return result;
    }

    @Override
    public int createModels(Object data,Class<? extends LottoModel> modelClass) {
        int result=0;
        if(modelClass == Client.class){
            Map<String,int[]> entries = (Map<String, int[]>) data;
            Iterator itr = entries.entrySet().iterator();
            while(itr.hasNext()){
                Map.Entry<String, int[]> e = (Map.Entry<String,int[]>)itr.next();
                Client found = (Client)getOne(Client.class,e.getKey());
                if(found != null){
                    itr.remove();
                    continue;
                }
                Client client = createClient(e.getKey(),e.getValue());
                if(client != null)
                    result++;
            }
        } else if (modelClass == WinningNumber.class){
            List<Map.Entry<Integer,int[]>> winnings = (List<Map.Entry<Integer,int[]>>)data;
        }
        return result;
    }

    public boolean createOne(Class<? extends LottoModel> modelClass,Object ... args){
        LottoModel existing = null;
        if(modelClass==Client.class){
            String name = (String) args[0];
            int[] picks = (int[]) args[1];
            existing = getFromCache(Client.class,name);
            if(existing == null) {
                existing = getFromDb(Client.class, name);
                if(existing == null){
                    existing = createClient(name,picks);
                    if(existing != null)
                        return true;
                }else{
                    clientCache.add((Client) existing);
                }
            }
        }else if (modelClass==WinningNumber.class){
            int prize = (int)args[0];
            int[] picks = (int[])args[1];
            existing = createWinningNumber(prize,picks);
            if(existing != null) return true;
        }
        return false;
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
            return null;
        }
    }
    private WinningNumber createWinningNumber(int prizePool,int[] picks){
        WinningNumber wn = new WinningNumber(prizePool,picks);
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
                default -> -1;
            };
            tempWinners.add(new Winner(e.getKey(),wn,cPrize));
        }
        try{
            et.begin();
            em.persist(wn);
            et.commit();
            winningCache.add((WinningNumber) wn);
            winnerCache.addAll(tempWinners);
            return wn;
        }catch(Exception ignored){}

        return null;
    }
}
