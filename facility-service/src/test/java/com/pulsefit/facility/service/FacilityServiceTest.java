package com.pulsefit.facility.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.pulsefit.facility.entity.Facility;
import com.pulsefit.facility.exception.FacilityNotFoundException;
import com.pulsefit.facility.repository.FacilityRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private FacilityService facilityService;

    @Test
    void getFacilityById_shouldReturnFacility() {

        Facility facility = new Facility();
        facility.setFacilityId(1L);
        facility.setFacilityName("PulseFit Hyderabad");
        facility.setLocation("Hyderabad");

        when(facilityRepository.findById(1L))
                .thenReturn(Optional.of(facility));

        Facility result = facilityService.getFacilityById(1L);

        assertEquals("PulseFit Hyderabad", result.getFacilityName());
        assertEquals("Hyderabad", result.getLocation());

        verify(facilityRepository).findById(1L);
    }

    @Test
    void getFacilityById_shouldThrowExceptionWhenNotFound() {

        when(facilityRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                FacilityNotFoundException.class,
                () -> facilityService.getFacilityById(1L)
        );

        verify(facilityRepository).findById(1L);
    }
}