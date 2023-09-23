package com.kenneth.lotto.service;

import java.util.*;

import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import com.kenneth.lotto.model.*;
import static com.kenneth.lotto.repository.LottoRepo.*;

@Service
public class AdminService implements LottoService {
    private Collection<WinningNumber> winningNumbers;
    @Override
    public List<WinningNumber> getLottoModels() {
        Query query = em.createQuery(
                "SELECT wn FROM WinningNumber wn",WinningNumber.class);
        return (List<WinningNumber>)query.getResultList();
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
