package com.gon.fitness.web.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.account.AccountRepository;
import com.gon.fitness.domain.tag.Tag;
import com.gon.fitness.domain.tag.TagRepository;
import com.gon.fitness.domain.zone.Zone;
import com.gon.fitness.domain.zone.ZoneRepository;
import com.gon.fitness.web.account.AccountService;
import com.gon.fitness.web.account.CurrentUser;
import com.gon.fitness.web.settings.form.*;
import com.gon.fitness.web.settings.validator.PasswordFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class SettingsController {

    private final AccountRepository accountRepository;
    private final ZoneRepository zoneRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {

        Account account1 = accountRepository.findByNickname("gon");

        model.addAttribute(account1);
        model.addAttribute("profile",new Profile(account1));
        return "settings/profile";
    }

    @GetMapping("/settings/password")
    public String profilePassword(@CurrentUser Account account, Model model) {

        Account account1 = accountRepository.findByNickname("gon");

        model.addAttribute(account1);
        model.addAttribute(new PasswordForm());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String profilePasswordSubmit(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
                                        RedirectAttributes redirectAttributes, Model model) {
         account = accountRepository.findByNickname("gon");
        if (errors.hasErrors()) {
            return "settings/password";
        }

        accountService.changePassword(passwordForm, account);
        redirectAttributes.addAttribute("message","비번 수정 완료");
        return "redirect:/settings/password";
    }

    @GetMapping("/settings/notifications")
    public String notifications(@CurrentUser Account account, Model model) {

        account = accountRepository.findByNickname("gon");

        model.addAttribute(account);
        NotificationsForm notificationsForm = modelMapper.map(account, NotificationsForm.class);
        model.addAttribute("notifications",notificationsForm);
        model.addAttribute(new PasswordForm());
        return "settings/notifications";
    }

    @PostMapping("/settings/notifications")
    public String notificationsSubmit(@CurrentUser Account account, NotificationsForm notificationsForm, Model model) {

        account = accountRepository.findByNickname("gon");

        accountService.changeNotifications(notificationsForm, account);

        return "redirect:/settings/notifications";
    }

    @GetMapping("/settings/tags")
    public String updateTags(@CurrentUser Account account, Model model) throws JsonProcessingException {

        account = accountRepository.findByNickname("gon");

        List<String> whitelist = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        List<String> tagList = account.getTags().stream().map(Tag::getTitle).collect(Collectors.toList());

        model.addAttribute(account);
        String attributeValue = objectMapper.writeValueAsString(whitelist);
        model.addAttribute("whitelist", attributeValue);
        model.addAttribute("tagList", tagList);

        return "settings/tags";
    }

    @GetMapping("/settings/zones")
    public String updateZones(@CurrentUser Account account, Model model) throws JsonProcessingException {

        account = accountRepository.findByNickname("gon");

        List<String> whitelist = zoneRepository.findAll().stream().map(ZonesForm::getZoneStr).collect(Collectors.toList());
        List<String> list = account.getZones().stream().map(ZonesForm::getZoneStr).collect(Collectors.toList());


        model.addAttribute("whitelist",objectMapper.writeValueAsString(whitelist));
        model.addAttribute("zones",list);
        return "settings/zones";
    }

    @PostMapping("/settings/zones/add")
    @ResponseBody
    public ResponseEntity addZones(@CurrentUser Account account, @RequestBody ZonesForm zonesForm) {

        account = accountRepository.findByNickname("gon");
        Zone zone = zoneRepository.findByCityAndProvince(zonesForm.getCity(), zonesForm.getProvince()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정"));

        accountService.addZones(account, zone);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/settings/zones/remove")
    @ResponseBody
    public ResponseEntity removeZones(@CurrentUser Account account, @RequestBody ZonesForm zonesForm) {

        account = accountRepository.findByNickname("gon");
        Zone zone = zoneRepository.findByCityAndProvince(zonesForm.getCity(), zonesForm.getProvince()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정"));

        accountService.removeZones(account, zone);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/settings/tags/add")
    @ResponseBody
    public ResponseEntity addTags(@CurrentUser Account account, @RequestBody TagsForm tagsForm) {

        account = accountRepository.findByNickname("gon");
        accountService.addTags(account, tagsForm);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/tags/remove")
    @ResponseBody
    public ResponseEntity removeTags(@CurrentUser Account account, @RequestBody TagsForm tagsForm) {

        account = accountRepository.findByNickname("gon");
        accountService.removeTags(account, tagsForm);
        return ResponseEntity.ok().build();
    }



}








