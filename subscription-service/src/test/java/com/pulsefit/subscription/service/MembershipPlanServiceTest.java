package com.pulsefit.subscription.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pulsefit.subscription.entity.MembershipPlan;
import com.pulsefit.subscription.repository.MembershipPlanRepository;

class MembershipPlanServiceTest {

    @Mock
    private MembershipPlanRepository planRepository;

    @InjectMocks
    private MembershipPlanService planService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPlan_shouldSavePlan() {

        MembershipPlan plan = new MembershipPlan();
        plan.setPlanName("Premium");
        plan.setDurationMonths(6);
        plan.setPrice(4999.0);
        plan.setAccessType("ALL_FACILITIES");
        plan.setStatus("ACTIVE");

        when(planRepository.existsByPlanName("Premium"))
                .thenReturn(false);

        when(planRepository.save(plan))
                .thenReturn(plan);

        MembershipPlan result =
                planService.createPlan(plan);

        assertNotNull(result);
        assertEquals("Premium", result.getPlanName());
        assertEquals(6, result.getDurationMonths());
        assertEquals(4999.0, result.getPrice());

        verify(planRepository).save(plan);
    }

    @Test
    void createPlan_shouldThrowExceptionWhenNameExists() {

        MembershipPlan plan = new MembershipPlan();
        plan.setPlanName("Premium");

        when(planRepository.existsByPlanName("Premium"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> planService.createPlan(plan));

        assertEquals("Plan name already exists",
                exception.getMessage());

        verify(planRepository, never()).save(plan);
    }

    @Test
    void getAllPlans_shouldReturnPlans() {

        MembershipPlan plan = new MembershipPlan();
        plan.setPlanName("Premium");

        when(planRepository.findAll())
                .thenReturn(List.of(plan));

        List<MembershipPlan> result =
                planService.getAllPlans();

        assertEquals(1, result.size());
        assertEquals("Premium", result.get(0).getPlanName());

        verify(planRepository).findAll();
    }

    @Test
    void getPlanById_shouldReturnPlan() {

        MembershipPlan plan = new MembershipPlan();
        plan.setPlanName("Premium");

        when(planRepository.findById(1L))
                .thenReturn(Optional.of(plan));

        MembershipPlan result =
                planService.getPlanById(1L);

        assertNotNull(result);
        assertEquals("Premium", result.getPlanName());

        verify(planRepository).findById(1L);
    }

    @Test
    void getPlanById_shouldThrowExceptionWhenNotFound() {

        when(planRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> planService.getPlanById(99L));

        assertEquals("Membership plan not found",
                exception.getMessage());

        verify(planRepository).findById(99L);
    }

    @Test
    void getPlansByStatus_shouldReturnPlans() {

        MembershipPlan plan = new MembershipPlan();
        plan.setPlanName("Premium");
        plan.setStatus("ACTIVE");

        when(planRepository.findByStatus("ACTIVE"))
                .thenReturn(List.of(plan));

        List<MembershipPlan> result =
                planService.getPlansByStatus("ACTIVE");

        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());

        verify(planRepository).findByStatus("ACTIVE");
    }

    @Test
    void updatePlan_shouldUpdatePlan() {

        MembershipPlan existingPlan = new MembershipPlan();
        existingPlan.setPlanName("Premium");
        existingPlan.setDurationMonths(3);
        existingPlan.setPrice(2999.0);
        existingPlan.setStatus("ACTIVE");

        MembershipPlan updatedPlan = new MembershipPlan();
        updatedPlan.setPlanName("Premium");
        updatedPlan.setDurationMonths(6);
        updatedPlan.setPrice(4999.0);
        updatedPlan.setAccessType("ALL_FACILITIES");
        updatedPlan.setDescription("Premium 6-month access");
        updatedPlan.setStatus("ACTIVE");

        when(planRepository.findById(1L))
                .thenReturn(Optional.of(existingPlan));

        when(planRepository.save(existingPlan))
                .thenReturn(existingPlan);

        MembershipPlan result =
                planService.updatePlan(1L, updatedPlan);

        assertEquals(6, result.getDurationMonths());
        assertEquals(4999.0, result.getPrice());
        assertEquals("ALL_FACILITIES", result.getAccessType());

        verify(planRepository).save(existingPlan);
    }

    @Test
    void deletePlan_shouldDeletePlan() {

        MembershipPlan plan = new MembershipPlan();

        when(planRepository.findById(1L))
                .thenReturn(Optional.of(plan));

        planService.deletePlan(1L);

        verify(planRepository).delete(plan);
    }
}