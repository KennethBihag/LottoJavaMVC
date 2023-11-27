package com.kenneth.lotto.controller;

import java.util.List;

import com.kenneth.lotto.model.Client;
import com.kenneth.lotto.model.Winner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kenneth.lotto.model.WinningNumber;
import com.kenneth.lotto.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController implements LottoController{
    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<List<Winner.WinnerDto>> setPrize(@RequestParam int prizePool){
        boolean success = adminService.setPrize(prizePool);
        if(success){
            var result = (List<Winner>)adminService.getAll(Winner.class);
            var resultDtos = result.stream().map(w->new Winner.WinnerDto(w)).toList();
            return ResponseEntity.ok(resultDtos);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/models")
    public String getAll(@RequestParam String modelType, Model model) {
        String jspPath = null;

        switch(modelType){
            case "entries":
                var allEntries =
                        (List<Client>)adminService.getAll(Client.class);
                model.addAttribute("allEntries",allEntries);
                jspPath = "clientpicks";
                break;
            case "winningnumbers":
                var allWins =
                        (List<WinningNumber>)adminService.getAll(WinningNumber.class);
                model.addAttribute("allWins",allWins);
                jspPath = "winnings";
                break;
            case "winners":
                var allWinners =
                        (List<Winner>)adminService.getAll(Winner.class);
                model.addAttribute("allWinners",allWinners);
                jspPath = "winners";
                break;
        }

        return jspPath;
    }
}
