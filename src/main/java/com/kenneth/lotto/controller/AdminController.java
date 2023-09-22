package com.kenneth.lotto.controller;

import com.kenneth.lotto.ServletInitializer;
import com.kenneth.lotto.model.LottoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kenneth.lotto.model.WinningNumber;
import com.kenneth.lotto.service.AdminService;

@Controller
public class AdminController implements LottoController{
    @Autowired
    private AdminService adminService;
    @PostMapping("/admin")
    public ResponseEntity<String> setPrize(@RequestParam int prizePool){
        WinningNumber wn = new WinningNumber();
        int[] winningNums = new int[LottoModel.maxPicks];
        adminService.randomize(winningNums,0);
        wn.setPicks(winningNums);
        wn.setPrizePool(prizePool);
        ServletInitializer.em.getTransaction().begin();
        ServletInitializer.em.persist(wn);
        ServletInitializer.em.getTransaction().commit();
        return ResponseEntity.ok().body("OKAY");
    }
    @Override
    public String getEntries(Model model) {
        return "";
    }
}
