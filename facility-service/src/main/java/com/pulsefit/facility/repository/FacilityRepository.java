package com.pulsefit.facility.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulsefit.facility.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findByLocationContainingIgnoreCase(String location);

    List<Facility> findByStatus(String status);
}