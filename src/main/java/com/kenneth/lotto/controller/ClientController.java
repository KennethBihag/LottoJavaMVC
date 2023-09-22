package com.kenneth.lotto.controller;

import java.util.*;

import com.kenneth.lotto.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kenneth.lotto.model.Client;

@Controller
public class ClientController implements LottoController {

    @Autowired
    private ClientService clientService;

    public ResponseEntity<String> updateEntriesFromString(String csvContent) {
        if(csvContent == null || csvContent.isBlank())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "No csv content in body found."
        );
        boolean updated = clientService.updateEntriesFromString(csvContent);
        if(!updated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesful update.");
    }

    @PostMapping("/client")
    public ResponseEntity<String> updateEntriesFromFile(
            @RequestParam(defaultValue = "") String entries, @RequestBody(required = false) String csvContent){
        if(entries.isBlank())
            return updateEntriesFromString(csvContent);
        boolean updated = clientService.updateEntriesFromFile(entries);
        if(!updated) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Failed to update. Either no new entries or invalid entries."
        );
        return ResponseEntity.ok("Succesful update.");
    }

    @Override
    @GetMapping("/client")
    public String getEntries(Model model){
        List<Client> allEntries = clientService.getLottoModels();
        model.addAttribute("allEntries",allEntries);
        return "forward:clientpicks.jsp";
    }
}