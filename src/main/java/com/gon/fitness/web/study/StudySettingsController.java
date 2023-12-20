package com.gon.fitness.web.study;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.domain.study.Study;
import com.gon.fitness.domain.study.StudyRepository;
import com.gon.fitness.web.account.AccountService;
import com.gon.fitness.web.account.CurrentUser;
import com.gon.fitness.web.study.form.StudyDescForm;
import com.gon.fitness.web.study.form.StudyForm;
import com.gon.fitness.web.study.validator.StudyFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/{path}/settings/")
public class StudySettingsController {

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


    @GetMapping("/description")
    public String updateDescription(@CurrentUser Account account, Model model, @PathVariable String path){

        account = accountRepository.findByNickname("gon");
        Study study = studyRepository.findByPath(path).orElseThrow(IllegalArgumentException::new);
        accountService.login(account);
        model.addAttribute(account);
        model.addAttribute(study);

        return "/study/description";
    }

    @PostMapping("/description")
    public String updateDescriptionSubmit(@Valid StudyDescForm studyDescForm, @CurrentUser Account account, Model model, @PathVariable String path,
                                          RedirectAttributes redirectAttributes){

        account = accountRepository.findByNickname("gon");
        Study study = studyRepository.findByPath(path).orElseThrow(IllegalArgumentException::new);

        accountService.changeStudyDesc(studyDescForm, study);
        redirectAttributes.addFlashAttribute("message","수정이 완료");
        return "redirect:/study/"+getEncodePath(study)+"/settings/description";
    }

    @GetMapping("/banner")
    public String banner(@CurrentUser Account account, Model model, @PathVariable String path){

        account = accountRepository.findByNickname("gon");
        Study study = studyRepository.findByPath(path).orElseThrow(IllegalArgumentException::new);
        accountService.login(account);
        model.addAttribute(account);
        model.addAttribute(study);

        return "/study/banner";
    }

    @PostMapping("/banner/enable")
    public String enableStudyBanner(@CurrentUser Account account, Model model, @PathVariable String path) {

        account = accountRepository.findByNickname("gon");
        Study study = studyRepository.findByPath(path).orElseThrow(IllegalArgumentException::new);
        accountService.login(account);
        studyService.enableStudyBanner(study);
        return "redirect:/study/" + getEncodePath(study) + "/settings/banner";
    }







    private static String getEncodePath(Study study) {
        return URLEncoder.encode(study.getPath(), StandardCharsets.UTF_8);
    }


}
