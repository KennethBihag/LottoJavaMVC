package com.kenneth.lotto.service;

import java.util.*;

import com.kenneth.lotto.repository.LottoRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.repository.LottoRepo;

@Service
public class AdminService implements LottoService {
    @Autowired
    LottoRepo repos;

    @Override
    public List<WinningNumber> getAll() {
        return (List<WinningNumber>)repos.getAllObjects(WinningNumber.class);
    }

    public boolean setPrize(int prizePool){
        WinningNumber wn = new WinningNumber();
        //int[] picks = new int[LottoModel.maxPicks];
        //randomize(picks,0);
        int[] picks = {1,2,3,4,5,6};
        return repos.createOne(WinningNumber.class,prizePool,picks);
    }
    public Prize checkPrizeTest(Client client, WinningNumber winning){
        repos = new LottoRepoImpl();
        return repos.checkPrize(client, winning);
    }
    public Map.Entry<WinningNumber,Map<Client, LottoService.Prize>> getWinnersFor1PicksTest(
            WinningNumber winningPick, List<Client> clients){
        repos = new LottoRepoImpl();
        return repos.getWinnersFor1Picks(winningPick,clients,null);
    }
}
