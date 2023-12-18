package com.gon.fitness.web.account;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.web.account.form.SignUpForm;
import com.gon.fitness.web.account.security.UserAccount;
import com.gon.fitness.web.account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService implements UserDetailsService {

    private final JavaMailSender javaMailSender;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account savedAccount = saveNewAccount(signUpForm);
        sendEmailCheckToken(savedAccount);
        return savedAccount;
    }

    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = modelMapper.map(signUpForm, Account.class);
        account.setPassword(passwordEncoder.encode(signUpForm.getPassword()));//todo encoding require
        Account savedAccount = accountRepository.save(account);
        return savedAccount;
    }

    private void sendEmailCheckToken(Account savedAccount) {
        savedAccount.generateEmailCheckToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();//update 진행
        mailMessage.setSubject("title....");
        mailMessage.setTo(savedAccount.getEmail());
        mailMessage.setText("/check-email-token?token="+ savedAccount.getEmailCheckToken() + "&email=" + savedAccount.getEmail());

        javaMailSender.send(mailMessage);
    }


    public void login(Account account) {
        //manager 역할을 대신
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);

    }

    public void completeSignUp(Account account) {
        account.verifiedEmail();
        login(account);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }
        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname+"가 잘못됨");
        }

        return new UserAccount(account);
    }
}







































