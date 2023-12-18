package com.gon.fitness.web.main;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.web.account.AccountService;
import com.gon.fitness.web.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountService accountService;

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {

        if (account != null) {
         model.addAttribute(account);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@CurrentUser Account account, Model model) {
        return "login";
    }







}
