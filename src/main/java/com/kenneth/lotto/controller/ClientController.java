package com.kenneth.lotto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kenneth.lotto.service.ClientService;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    private ResponseEntity<String> updateClients(@RequestBody String csvContent) {
        boolean updated = clientService.updateClients(csvContent);
        if(!updated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesful update.");
    }

    @PostMapping("/client")
    public ResponseEntity<String> uploadEntries(
            @RequestParam(defaultValue = "") String entries, @RequestBody String csvContent){
        if(entries.isBlank())
            return updateClients(csvContent);
        boolean updated = clientService.updateClientsFromFile(entries);
        if(!updated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesful update.");
    }
}