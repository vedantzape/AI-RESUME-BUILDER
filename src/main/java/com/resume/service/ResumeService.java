package com.resume.service;

import com.resume.dto.*;

public interface ResumeService {

    ResumeDraftResponse analyzeSummary(SummaryRequest request);

    String generateFinalResume(FinalResumeRequest request);
}
