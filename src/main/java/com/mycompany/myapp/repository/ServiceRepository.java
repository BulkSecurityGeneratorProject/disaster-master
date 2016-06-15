package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Service;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Service entity.
 */
@SuppressWarnings("unused")
public interface ServiceRepository extends JpaRepository<Service,Long> {

}
