package com.gon.fitness.domain.zone;

import com.gon.fitness.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface ZoneRepository extends JpaRepository<Zone, Long> {


    Optional<Zone> findByCityAndProvince(String city, String province);
}
