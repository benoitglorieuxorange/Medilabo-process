package com.bgl.medilabosolutions.backnoteprocess.dto;

public record ProcessRequestDto(
    Long patientId,
    Integer height,
    Integer weight,
    boolean isSmoker,
    double hemoglobin,
    double cholesterol,
    double microalbumin,
    String patientNote
) {}
