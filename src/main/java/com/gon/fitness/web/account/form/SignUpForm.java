package com.gon.fitness.web.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpForm {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 2, max = 20)
    private String nickname;

    @NotBlank
    private String password;


}
