package com.kenneth.lotto.controller;

import java.util.List;

import ch.qos.logback.core.status.Status;
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
    public ResponseEntity<String> setPrize(@RequestParam int prizePool){
        return adminService.setPrize(prizePool) ? ResponseEntity.ok().body("OKAY")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("FAILED");
    }

    @Override
    @GetMapping
    public String getAll(Model model) {
        List<WinningNumber> allWins = adminService.getAll();
        model.addAttribute("allWins",allWins);
        return "forward:winnings.jsp";
    }
}
