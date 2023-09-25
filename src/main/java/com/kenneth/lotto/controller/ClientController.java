package com.kenneth.lotto.controller;

import com.kenneth.lotto.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientController implements LottoController {
    @Autowired
    private ClientService clientService;

    public ResponseEntity<String> updateEntriesFromString(String csvContent) {
        if(csvContent == null || csvContent.isBlank())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "No csv content in body found."
        );
        int updated = clientService.updateEntriesFromString(csvContent);
        if(updated<1) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesfully updated "+updated+" entries.");
    }

    @PostMapping
    public ResponseEntity<String> updateEntriesFromFile(
            @RequestParam(defaultValue = "") String entries, @RequestBody(required = false) String csvContent){
        if(entries.isBlank())
            return updateEntriesFromString(csvContent);
        int updated = clientService.updateEntriesFromFile(entries);
        if(updated<0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Error opening csv file."
            );
        else if(updated<1) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        else return ResponseEntity.ok("Succesfuly updated "+updated+" entries");
    }
}
