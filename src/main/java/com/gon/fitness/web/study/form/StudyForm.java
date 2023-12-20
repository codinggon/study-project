package com.gon.fitness.web.study.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class StudyForm {

    @NotBlank
    @Length(min = 2, max = 20)
    private String path;

    @NotBlank
    @Length(max = 50)
    private String title;

    private String shortDescription;

    private String fullDescription;





}











