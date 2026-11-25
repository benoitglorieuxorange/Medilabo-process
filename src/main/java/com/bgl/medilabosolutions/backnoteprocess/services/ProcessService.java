package com.bgl.medilabosolutions.backnoteprocess.services;


import com.bgl.medilabosolutions.backnoteprocess.dto.CaseRequestDto;
import com.bgl.medilabosolutions.backnoteprocess.dto.ProcessRequestDto;
import com.bgl.medilabosolutions.backnoteprocess.dto.ProcessResponseDto;
import com.bgl.medilabosolutions.backnoteprocess.enums.RiskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;


@Service
public class ProcessService {

    private final CaseClientRequest caseRequest;
    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);
    private final static int MICRO_ALBUMINE_THRESHOLD_LOW = 30;
    private final static int MICRO_ALBUMINE_THRESHOLD_HIGH = 300;
    private final static int HEMOGLOBINE_THRESHOLD = 12;
    private final static int IMC_THRESHOLD_LOW = 18;
    private final static int IMC_THRESHOLD_MED = 25;
    private final static int IMC_THRESHOLD_HIGH = 30;

    private enum trigger {
        Anormal, Cholesterol, Vertiges, Fatigue, Palpitations, Douleurs, Essoufflement, PerteDePoids, SueursNocturnes, Rechute, Anticorps, Vaccination, Allergies, Anticoagulants, Antihypertenseurs, Antidiabetiques, Antidepressifs, Antipsychotiques, Antiviraux
    }


    public ProcessService(CaseClientRequest caseRequest) {
        this.caseRequest = caseRequest;
    }

    public ProcessResponseDto getNote(ProcessRequestDto processRequestDto) {

//      logger.info("Processing request: {}", processRequestDto);

        Optional<CaseRequestDto> caseValue = caseRequest.getCaseById(processRequestDto.patientId());

        int age = calculAge(caseValue.get().dateNaissance());

        String sexe = caseValue.get().sexe();

        int microAlbumineScore = calculMicroAlbumine(processRequestDto.microalbumin());
        int hemoglobineScore = calculHemoglobine(processRequestDto.hemoglobin());
        int imcScore = calculIMC(processRequestDto.weight(), processRequestDto.height());
        int smokerScore = calculIsSmoker(processRequestDto.isSmoker());
        int occurrenceTriggerScore = calculOccurrenceTrigger(processRequestDto.patientNote());

        int totalScore = microAlbumineScore + hemoglobineScore + imcScore + smokerScore + occurrenceTriggerScore;

        if (totalScore == 0){
            return new ProcessResponseDto(RiskState.NONE);
        }

        // Patient de plus de 30 ans
        if ( age >=30 ){
            if ( totalScore == 8) {
                return new ProcessResponseDto(RiskState.EARLY_ONSET);
            }
            if ( totalScore >= 6 ){
                return new ProcessResponseDto(RiskState.IN_DANGER);
            }
            if ( totalScore >= 2 ){
                return new ProcessResponseDto(RiskState.BORDERLINE);
            }
            return new ProcessResponseDto(RiskState.NONE);    // le cas de 1 trigger n'est pas demandé → NONE
        }

        // Patient de moins de 30 ans

        switch (sexe){
            case "M":
                if ( totalScore >= 5 ){
                    return new ProcessResponseDto(RiskState.EARLY_ONSET);
                }
                if ( totalScore >=3 ){
                    return new ProcessResponseDto(RiskState.IN_DANGER);
                }
                return new ProcessResponseDto(RiskState.NONE);
            case "F":
                if ( totalScore >= 7 ){
                    return new ProcessResponseDto(RiskState.EARLY_ONSET);
                }
                if ( totalScore >= 4 ) {
                    return new ProcessResponseDto(RiskState.BORDERLINE);
                }
                return new ProcessResponseDto(RiskState.NONE);
            default:
                throw new IllegalArgumentException("Sexe non géré : " + sexe);
        }

    }

    private Integer calculAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private Integer calculMicroAlbumine(Double microAlbumine) {
        if (microAlbumine < MICRO_ALBUMINE_THRESHOLD_LOW) {
            return 0;
        } else if (microAlbumine >= MICRO_ALBUMINE_THRESHOLD_LOW && microAlbumine <= MICRO_ALBUMINE_THRESHOLD_HIGH) {
            return 1;
        } else {
            return 2;
        }
    }
    private Integer calculHemoglobine(Double hemoglobine) {
        if (hemoglobine < HEMOGLOBINE_THRESHOLD) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer calculIMC(Integer poids, Integer taille) {
        int imc =  (poids / (taille * taille));
        if (imc < IMC_THRESHOLD_LOW) {
            return 1;
        } else if (imc < IMC_THRESHOLD_MED) {
            return 0;
        } else if (imc < IMC_THRESHOLD_HIGH) {
            return 1;
        } else {
            return 2;
        }
    }

    private Integer calculIsSmoker(Boolean isSmoker) {
        if (isSmoker) {
            return 1;
        } else {
            return 0;
        }
    }

    private Integer calculOccurrenceTrigger(String patientNote) {
        int occurrence = 0;
        for (trigger t : trigger.values()) {
            if (patientNote.toLowerCase().contains(t.name().toLowerCase())) {
                occurrence++;
            }
        }
        return occurrence;
    }


}
