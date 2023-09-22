package com.kenneth.lotto.service;

import java.util.List;

import com.kenneth.lotto.model.LottoModel;

public interface LottoService {
    int fileSize = 1024;
    void validateInput(String csvInput);
    public boolean updateEntriesFromString(String csvInput);
    boolean updateEntriesFromFile(String filePath);
    List<? extends LottoModel> getLottoModels();
}
