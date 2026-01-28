package com.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    // 🔹 STEP 1: Analyze summary

    @Override
    public ResumeDraftResponse analyzeSummary(SummaryRequest request) {

        String prompt = """
    Extract resume information from the text below.

    Rules:
    - If a name is mentioned, extract it.
    - If name is not mentioned, return empty string.
    - Infer role from context.
    - Generate relevant skills based on the text.
    - Keep summary professional and concise.
    - Return ONLY valid JSON. No explanation.

    JSON format:
    {
      "name": "",
      "role": "",
      "summary": "",
      "skills": [],
      "projects": []
    }

    Text:
    %s
    """.formatted(request.getSummary());

        String aiResponse = callGemini(prompt);

        try {
            String jsonOnly = extractJson(aiResponse);
            return mapper.readValue(jsonOnly, ResumeDraftResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response", e);
        }
    }


    // 🔹 STEP 2: Generate final resume
    @Override
    public String generateFinalResume(FinalResumeRequest r) {

        String prompt = """
        Create a professional ATS-friendly resume.

        Name: %s
        Role: %s
        Experience: %d years

        Summary:
        %s

        Skills:
        %s

        Projects:
        %s

        Use clean bullet points and professional tone.
        """.formatted(
                r.getName(),
                r.getRole(),
                r.getExperience(),
                r.getSummary(),
                String.join(", ", r.getSkills()),
                String.join(", ", r.getProjects())
        );

        return callGemini(prompt);
    }

    // 🔹 Common Gemini call
    private String callGemini(String prompt) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                apiUrl + "?key=" + apiKey,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
        );

        Map<String, Object> content =
                (Map<String, Object>) response.getBody()
                        .getCandidates().get(0).get("content");

        List<Map<String, String>> parts =
                (List<Map<String, String>>) content.get("parts");

        return parts.get(0).get("text");
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");

        if (start == -1 || end == -1 || start >= end) {
            throw new RuntimeException("No valid JSON found in Gemini response");
        }

        return text.substring(start, end + 1);
    }

}
