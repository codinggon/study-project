package com.gon.fitness.web.account;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.domain.study.Study;
import com.gon.fitness.domain.tag.Tag;
import com.gon.fitness.domain.tag.TagRepository;
import com.gon.fitness.domain.zone.Zone;
import com.gon.fitness.web.account.form.SignUpForm;
import com.gon.fitness.web.account.security.UserAccount;
import com.gon.fitness.web.config.AppProperties;
import com.gon.fitness.web.mail.EmailMessage;
import com.gon.fitness.web.mail.EmailService;
import com.gon.fitness.web.settings.form.NotificationsForm;
import com.gon.fitness.web.settings.form.PasswordForm;
import com.gon.fitness.web.settings.form.TagsForm;
import com.gon.fitness.web.study.form.StudyDescForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService implements UserDetailsService {

    private final EmailService emailService;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TagRepository tagRepository;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    public Account processNewAccount(SignUpForm signUpForm) {
        Account savedAccount = saveNewAccount(signUpForm);
        savedAccount.generateEmailCheckToken();
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

        Context context = new Context();
        context.setVariable("nickname",savedAccount.getNickname());
        context.setVariable("link","/check-email-token?token=" + savedAccount.getEmailCheckToken() + "&email=" + savedAccount.getEmail());
        context.setVariable("host",appProperties.getHost());
        context.setVariable("message","링크를 클릭하세요");
        context.setVariable("linkName","이메일 인증");

        String process = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(savedAccount.getEmail())
                .subject("title.....")
                .message(process)
                .build();

        emailService.sendEmail(emailMessage);
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

    public void changePassword(PasswordForm passwordForm, Account account) {
        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
    }

    public void changeNotifications(NotificationsForm notificationsForm, Account account) {
        modelMapper.map(notificationsForm, account);
    }

    public void addTags(Account account, TagsForm tagsForm) {
        Tag tag = tagRepository.findByTitle(tagsForm.getTitle()).orElseGet(() -> tagRepository.save(Tag.builder().title(tagsForm.getTitle()).build()));
        if (!account.getTags().contains(tag)) {
            account.addTags(tag);
        }
    }

    public void removeTags(Account account, TagsForm tagsForm) {
        tagRepository.findByTitle(tagsForm.getTitle()).ifPresent(tag -> {
            tagRepository.delete(tag);
            account.getTags().remove(tag);
        });
    }

    public void addZones(Account account, Zone zone) {
        if (!account.getZones().contains(zone)) {
            account.addZones(zone);
        }
    }


    public void removeZones(Account account, Zone zone) {
        account.removeZones(zone);
    }

    public void changeStudyDesc(StudyDescForm studyDescForm, Study study) {
        study.changeStudyDesc(studyDescForm.getShortDescription(), studyDescForm.getFullDescription());
    }
}







































