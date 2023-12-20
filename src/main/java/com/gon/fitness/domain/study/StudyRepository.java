package com.gon.fitness.domain.study;

import com.gon.fitness.domain.tag.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {

    boolean existsByPath(String path);

    Optional<Study> findByPath(String path);

    @EntityGraph(attributePaths = {"managers","members","tags","zones"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Study> findStudyWithAllByPath(String path);


}
