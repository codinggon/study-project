package com.gon.fitness.web.study.form;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;

@Data
public class StudyDescForm {

    private String shortDescription;


    private String fullDescription;



}
