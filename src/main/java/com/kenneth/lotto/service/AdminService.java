package com.kenneth.lotto.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.repository.*;

@Service
public class AdminService implements LottoService {
    @Autowired
    LottoRepoImpl repos;

    public List<?> getAll(Class<?> modelClass) {
        return repos.getAllObjects(modelClass);
    }

    public boolean setPrize(int prizePool){
        return repos.createWinningNumber(prizePool);
    }
    public Prize checkPrizeTest(Client client, WinningNumber winning){
        return repos.checkPrize(client, winning);
    }
    public Map.Entry<WinningNumber,Map<Client, LottoService.Prize>> getWinnersFor1PicksTest(
            WinningNumber winningPick, List<Client> clients){
        repos = new LottoRepoImpl();
        return repos.getWinnersFor1Picks(winningPick,clients,null);
    }
}
