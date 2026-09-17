package com.pulsefit.facility.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pulsefit.facility.entity.Facility;
import com.pulsefit.facility.repository.FacilityRepository;

import com.pulsefit.facility.exception.FacilityNotFoundException;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public Facility createFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacilityById(Long facilityId) {
        return facilityRepository.findById(facilityId)
                .orElseThrow(() ->
                        new FacilityNotFoundException("Facility not found"));
    }
    public List<Facility> searchByLocation(String location) {
        return facilityRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Facility> getByStatus(String status) {
        return facilityRepository.findByStatus(status);
    }

    public Facility updateFacility(Long facilityId, Facility facility) {
        Facility existingFacility = getFacilityById(facilityId);

        existingFacility.setFacilityName(facility.getFacilityName());
        existingFacility.setLocation(facility.getLocation());
        existingFacility.setContact(facility.getContact());
        existingFacility.setOperatingHours(facility.getOperatingHours());
        existingFacility.setCapacity(facility.getCapacity());
        existingFacility.setStatus(facility.getStatus());

        return facilityRepository.save(existingFacility);
    }

    public void deleteFacility(Long facilityId) {
        Facility facility = getFacilityById(facilityId);
        facilityRepository.delete(facility);
    }
}