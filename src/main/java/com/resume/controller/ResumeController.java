package com.resume.controller;

import com.resume.dto.*;
import com.resume.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // STEP 1
    @PostMapping("/analyze")
    public ResumeDraftResponse analyze(@Valid @RequestBody SummaryRequest request) {
        return resumeService.analyzeSummary(request);
    }

    // STEP 2
    @PostMapping("/generate")
    public String generate(@Valid @RequestBody FinalResumeRequest request) {
        return resumeService.generateFinalResume(request);
    }
}
