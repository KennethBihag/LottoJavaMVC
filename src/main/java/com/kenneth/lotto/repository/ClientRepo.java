package com.kenneth.lotto.repository;

import java.util.ArrayList;
import java.util.Collection;

import com.kenneth.lotto.model.*;

public class ClientRepo implements LottoRepo{
    @Override
    public Collection<Client> getAllModels() {
        return new ArrayList<>();
    }
}
