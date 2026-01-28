package com.resume.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GeminiResponse {
    private List<Map<String, Object>> candidates;
}
