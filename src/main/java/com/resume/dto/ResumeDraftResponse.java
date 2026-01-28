package com.resume.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResumeDraftResponse {
    private String name;
    private String role;
    private String summary;
    private List<String> skills;
    private List<String> projects;
}
