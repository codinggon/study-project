package com.gon.fitness.web.account;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.web.account.form.SignUpForm;
import com.gon.fitness.web.account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    private final SignUpFormValidator signUpFormValidator;


    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm",new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";
    }



    ///check-email-token?token=d0eb9437-3671-4502-b77d-8e96726506a3&email=gon@email.com
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error","wrong.email");
            return "account/checked-email";
        }

        if (!account.isValidToken(token)) {
            model.addAttribute("error","wrong.token");
            return "account/checked-email";
        }

        account.verifiedEmail();
        accountService.login(account);
        model.addAttribute("numberOfUser",accountRepository.count());
        model.addAttribute("nickname",account.getNickname());
        return "account/checked-email";
    }





}







