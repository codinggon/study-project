package com.gon.fitness.web.study;

import com.gon.fitness.domain.account.Account;
import com.gon.fitness.domain.study.Study;
import com.gon.fitness.domain.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createNewStudy(Study study, Account account) {

        Study savedStudy = studyRepository.save(study);
        savedStudy.addManager(account);
        return savedStudy;
    }


    public void enableStudyBanner(Study study) {
        study.setUseBanner(true);
    }
}
