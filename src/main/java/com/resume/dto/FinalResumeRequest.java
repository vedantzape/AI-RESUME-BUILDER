package com.resume.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class FinalResumeRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String role;

    @NotBlank
    private String summary;

    @Min(0)
    private int experience;

    @NotEmpty
    private List<String> skills;

//    @NotEmpty
    private List<String> projects;
}
