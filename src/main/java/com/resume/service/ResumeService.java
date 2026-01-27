package com.resume.service;


import com.resume.dto.ResumeRequest;

public interface ResumeService {
    String generateResume(ResumeRequest resumeRequest);
}