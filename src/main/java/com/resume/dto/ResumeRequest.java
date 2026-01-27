package com.resume.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String role;

    @Min(0)
    private int experience;

    @NotEmpty
    private List<String> skills;

    @NotEmpty
    private List<String> projects;

}
