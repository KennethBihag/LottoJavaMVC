package com.kenneth.lotto.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.repository.LottoRepoImpl;

public class AdminService implements LottoService {

    LottoRepoImpl repos;
    public void setLottoRepoImpl(LottoRepoImpl r){
        repos=r;
    }

    public List<?> getAll(Class<?> modelClass) {
        return repos.getAllObjects(modelClass);
    }

    public boolean setPrize(int prizePool){
        return repos.createWinningNumber(prizePool);
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
