package com.kenneth.lotto.controller;

import org.springframework.http.ResponseEntity;

public interface LottoController {
    ResponseEntity<String> updateEntriesFromString(String csvContent);
    ResponseEntity<String> updateEntriesFromFile(String entries, String csvContent);
}
