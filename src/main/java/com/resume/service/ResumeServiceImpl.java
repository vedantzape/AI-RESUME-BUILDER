package com.resume.service;

import com.resume.dto.ResumeRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;



import java.util.List;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService{

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateResume(ResumeRequest dto) {

        String prompt = """
                Create a professional ATS-friendly resume.

                Name: %s
                Role: %s
                Experience: %d years
                Skills: %s
                Projects: %s

                Format:
                - Professional Summary
                - Key Skills
                - Projects
                """.formatted(
                dto.getName(),
                dto.getRole(),
                dto.getExperience(),
                String.join(", ", dto.getSkills()),
                String.join(", ", dto.getProjects())
        );

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

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "?key=" + apiKey,
                HttpMethod.POST,
                entity,
                Map.class
        );

        return extractText(response.getBody());
    }

    private String extractText(Map body) {
        List<Map> candidates = (List<Map>) body.get("candidates");
        Map content = (Map) candidates.get(0).get("content");
        List<Map> parts = (List<Map>) content.get("parts");
        return parts.get(0).get("text").toString();
    }


}
