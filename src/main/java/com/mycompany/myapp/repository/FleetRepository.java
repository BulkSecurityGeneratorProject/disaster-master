package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Fleet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fleet entity.
 */
@SuppressWarnings("unused")
public interface FleetRepository extends JpaRepository<Fleet,Long> {

}
