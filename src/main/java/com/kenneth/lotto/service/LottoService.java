package com.kenneth.lotto.service;

public interface LottoService {
    int fileSize = 1024;
    void validateInput(String csvInput);
    public boolean updateEntriesFromString(String csvInput);
    boolean updateEntriesFromFile(String filePath);
}
