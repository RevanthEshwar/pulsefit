package com.pulsefit.facility.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulsefit.facility.entity.Facility;
import com.pulsefit.facility.service.FacilityService;

import org.springframework.http.HttpStatus;
import com.pulsefit.facility.exception.FacilityNotFoundException;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping
    public ResponseEntity<Facility> createFacility(
            @RequestBody Facility facility) {

        return ResponseEntity.ok(
                facilityService.createFacility(facility));
    }

    @GetMapping
    public ResponseEntity<List<Facility>> getAllFacilities() {

        return ResponseEntity.ok(
                facilityService.getAllFacilities());
    }

    @GetMapping("/{facilityId}")
    public ResponseEntity<Facility> getFacilityById(
            @PathVariable Long facilityId) {

        return ResponseEntity.ok(
                facilityService.getFacilityById(facilityId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Facility>> searchByLocation(
            @RequestParam String location) {

        return ResponseEntity.ok(
                facilityService.searchByLocation(location));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Facility>> getByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                facilityService.getByStatus(status));
    }

    @PutMapping("/{facilityId}")
    public ResponseEntity<Facility> updateFacility(
            @PathVariable Long facilityId,
            @RequestBody Facility facility) {

        return ResponseEntity.ok(
                facilityService.updateFacility(facilityId, facility));
    }

    @DeleteMapping("/{facilityId}")
    public ResponseEntity<String> deleteFacility(
            @PathVariable Long facilityId) {

        facilityService.deleteFacility(facilityId);

        return ResponseEntity.ok("Facility deleted successfully");
    }
    
    @ExceptionHandler(FacilityNotFoundException.class)
    public ResponseEntity<String> handleFacilityNotFound(
            FacilityNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}