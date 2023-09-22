package com.kenneth.lotto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface LottoController {
    ResponseEntity<String> updateEntriesFromString(String csvContent);
    ResponseEntity<String> updateEntriesFromFile(String entries, String csvContent);
    String getEntries(Model model);
}
