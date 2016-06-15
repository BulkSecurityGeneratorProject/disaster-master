package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MissionStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MissionStatus entity.
 */
@SuppressWarnings("unused")
public interface MissionStatusRepository extends JpaRepository<MissionStatus,Long> {

}
