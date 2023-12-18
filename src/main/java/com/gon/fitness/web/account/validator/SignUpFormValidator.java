package com.gon.fitness.web.account.validator;

import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.web.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //todo email, nickname 중복이 있는지
        SignUpForm signUpForm = (SignUpForm) target;
        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname","wrong.nickname","닉네임이 중복");
        }
        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email","wrong.email","email이 중복");
        }

    }
}
