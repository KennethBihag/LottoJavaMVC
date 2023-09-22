package com.kenneth.lotto.service;

import com.kenneth.lotto.model.WinningNumber;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AdminService implements LottoService {
    private Collection<WinningNumber> winningNumbers;
    @Override
    public List<WinningNumber> getLottoModels() {
        Query query = em.createQuery(
                "SELECT wn FROM WinningNumber wn",WinningNumber.class);
        return (List<WinningNumber>)query.getResultList();

    }
}
