package com.gon.fitness.web.study;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.domain.study.Study;
import com.gon.fitness.domain.study.StudyRepository;
import com.gon.fitness.web.account.AccountService;
import com.gon.fitness.web.account.CurrentUser;
import com.gon.fitness.web.study.form.StudyForm;
import com.gon.fitness.web.study.validator.StudyFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class StudyController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final StudyRepository studyRepository;
    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;


    @InitBinder("studyForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }


    @GetMapping("/new-study")
    public String newStudyForm(@CurrentUser Account account, Model model) {

        account = accountRepository.findByNickname("gon");

        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return "study/form";

    }

    @PostMapping("/new-study")
    public String newStudySubmit(@CurrentUser Account account, Model model,
                                 @Valid StudyForm studyForm, Errors errors) {

        account = accountRepository.findByNickname("gon");
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return "study/form";
        }

        Study study = modelMapper.map(studyForm, Study.class);
        studyService.createNewStudy(study, account);

        return "redirect:/study/" + getEncodePath(study);

    }

    @GetMapping("/study/{path}")
    public String getStudy(@CurrentUser Account account, Model model,
                                 @PathVariable String path) {
        account = accountRepository.findByNickname("gon");
        model.addAttribute(account);

//        Study study = studyRepository.findByPath(path).orElseThrow(() -> new IllegalArgumentException(path + "가 없습니다."));

        Study study = studyRepository.findStudyWithAllByPath(path).orElseThrow(() -> new IllegalArgumentException(path + "가 없습니다."));;

        model.addAttribute(study);

        accountService.login(account);

        return "study/view";
    }

    @GetMapping("/study/{path}/members")
    public String viewStudyMembers(@CurrentUser Account account, Model model,
                                   @PathVariable String path) {

        account = accountRepository.findByNickname("gon");
        Study study = studyRepository.findByPath(path).orElseThrow(IllegalArgumentException::new);
        model.addAttribute(account);
        model.addAttribute(study);

        return "study/members";

    }







    private static String getEncodePath(Study study) {
        return URLEncoder.encode(study.getPath(), StandardCharsets.UTF_8);
    }


}
