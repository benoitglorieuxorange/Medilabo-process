package com.bgl.medilabosolutions.backnoteprocess.controller;


import com.bgl.medilabosolutions.backnoteprocess.dto.ProcessRequestDto;
import com.bgl.medilabosolutions.backnoteprocess.dto.ProcessResponseDto;
import com.bgl.medilabosolutions.backnoteprocess.services.CaseClientRequest;
import com.bgl.medilabosolutions.backnoteprocess.services.ProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    private final ProcessService processService;
    private final CaseClientRequest caseRequest;

    public ProcessController(ProcessService processService, CaseClientRequest caseRequest) {
        this.processService = processService;
        this.caseRequest = caseRequest;
    }

    @PostMapping
    public ResponseEntity<ProcessResponseDto> getNote(@RequestBody ProcessRequestDto processRequestDto) {
        ProcessResponseDto response = processService.getNote(processRequestDto);
                return ResponseEntity.ok(response);
    }





}
