package com.pulsefit.subscription.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @Column(nullable = false, unique = true)
    private String planName;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false)
    private Double price;

    private String accessType;

    private String description;

    @Column(nullable = false)
    private String status;

    public MembershipPlan() {
    }

    public MembershipPlan(String planName, Integer durationMonths,
                          Double price, String accessType,
                          String description, String status) {
        this.planName = planName;
        this.durationMonths = durationMonths;
        this.price = price;
        this.accessType = accessType;
        this.description = description;
        this.status = status;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}