package com.bgl.medilabosolutions.backnoteprocess.dto;

import com.bgl.medilabosolutions.backnoteprocess.enums.RiskState;

public record ProcessResponseDto(
    RiskState riskState
) {}
