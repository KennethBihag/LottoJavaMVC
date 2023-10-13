package com.kenneth.lotto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kenneth.lotto.model.*;
import com.kenneth.lotto.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController implements LottoController{
    @Autowired
    private AdminService adminService;

    @PostMapping
    public List<Winner.WinnerDto> setPrize(@RequestParam int prizePool){
        boolean success = adminService.setPrize(prizePool);
        if(success){
            var result = (List<Winner>)adminService.getAll(Winner.class);
            var resultDtos = result.stream().map(w->new Winner.WinnerDto(w)).toList();
            return resultDtos;
        }else{
            return null;
        }
    }

    @GetMapping("/models")
    public List<?> getAll(@RequestParam String modelType) {
        List<?> results = null;
        switch(modelType){
            case "entries":
                results = adminService.getAll(Client.class);
                break;
            case "winningnumbers":
                results = adminService.getAll(WinningNumber.class);
                break;
            case "winners":
                results = adminService.getAll(Winner.class);
                results = results.stream().map(w->new Winner.WinnerDto((Winner) w)).toList();
                break;
        }
        return results;
    }
}
