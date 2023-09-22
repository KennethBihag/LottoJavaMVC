package com.kenneth.lotto.controller;

import com.kenneth.lotto.service.LottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientController implements LottoController {

    @Autowired
    private LottoService lottoService;

    @Override
    public ResponseEntity<String> updateEntriesFromString(String csvContent) {
        if(csvContent == null || csvContent.isBlank())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "No csv content in body found."
        );
        boolean updated = lottoService.updateEntriesFromString(csvContent);
        if(!updated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesful update.");
    }

    @Override
    @PostMapping("/client")
    public ResponseEntity<String> updateEntriesFromFile(
            @RequestParam(defaultValue = "") String entries, @RequestBody(required = false) String csvContent){
        if(entries.isBlank())
            return updateEntriesFromString(csvContent);
        boolean updated = lottoService.updateEntriesFromFile(entries);
        if(!updated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesful update.");
    }
}