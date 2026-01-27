package com.resume.controller;


import com.resume.dto.ResumeRequest;
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

    @PostMapping("/generate")
    public String generate(@Valid @RequestBody ResumeRequest resumeRequest) {
        return resumeService.generateResume(resumeRequest);
    }

    @GetMapping("/test")
    public String test() {
        return "Controller working";
    }

}
