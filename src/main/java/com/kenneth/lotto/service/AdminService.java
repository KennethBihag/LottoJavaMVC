package com.kenneth.lotto.service;

import java.util.*;

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
        int[] picks = new int[LottoModel.maxPicks];
        randomize(picks,0);
        return repos.createOne(WinningNumber.class,prizePool,picks);
    }

    public Prize checkPrize(Client client,WinningNumber winningNumber){
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
