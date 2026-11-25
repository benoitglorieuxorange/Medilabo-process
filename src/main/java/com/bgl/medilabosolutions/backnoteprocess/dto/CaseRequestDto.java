package com.bgl.medilabosolutions.backnoteprocess.dto;

import java.time.LocalDate;

public record CaseRequestDto(
        String sexe,
        LocalDate dateNaissance
) {}
