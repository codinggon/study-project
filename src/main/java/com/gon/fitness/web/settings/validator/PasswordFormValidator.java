package com.gon.fitness.web.settings.validator;

import com.gon.fitness.web.settings.form.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;
        if (!isNotConfirmPassword(passwordForm)) {
            errors.rejectValue("newPassword","wrong.newPassword","패스워드가 다릅니다.");
        }

    }

    private static boolean isNotConfirmPassword(PasswordForm passwordForm) {
        return passwordForm.getPassword().equals(passwordForm.getNewPassword());
    }
}
