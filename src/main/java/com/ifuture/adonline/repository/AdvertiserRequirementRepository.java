package com.ifuture.adonline.repository;

import com.ifuture.adonline.domain.AdvertiserRequirement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AdvertiserRequirement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdvertiserRequirementRepository extends JpaRepository<AdvertiserRequirement, Long> {

    void deleteByMasterId(Long id);
}
