package com.kenneth.lotto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kenneth.lotto.model.WinningNumber;
import com.kenneth.lotto.service.AdminService;
import com.kenneth.lotto.model.LottoModel;
import static com.kenneth.lotto.repository.LottoRepo.*;

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
        em.getTransaction().begin();
        em.persist(wn);
        em.getTransaction().commit();
        return ResponseEntity.ok().body("OKAY");
    }
    @Override
    public String getEntries(Model model) {
        return "";
    }
}
