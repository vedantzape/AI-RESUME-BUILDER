package com.resume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SummaryRequest {
    @NotBlank
    private String summary;
}
