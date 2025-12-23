package org.mql.genai.darijaatranslator.service;

import org.mql.genai.darijaatranslator.models.TranslationRequest;
import org.mql.genai.darijaatranslator.models.TranslationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiTranslationService implements TranslationService {


    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.prompt}")
    private String promptTemplate;

    @Value("${gemini.model.url}")
    private String modelUrl;

    @Value("${gemini.model.name}")
    private String modelName;

    private final RestTemplate restTemplate;

    public GeminiTranslationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TranslationResponse translate(TranslationRequest request) {
        try {
            String prompt = promptTemplate + request.getEnglishText();
            String translation = callGeminiDebug(prompt);

            return new TranslationResponse(
                    request.getEnglishText(),
                    translation,
                    "gemini-2.0-flash"
            );
        } catch (Exception e) {
            return new TranslationResponse(
                    request.getEnglishText(),
                    "سلام (Fallback) - " + request.getEnglishText(),
                    "fallback"
            );
        }
    }

    private String callGeminiDebug(String prompt) {
        String url = modelUrl + apiKey;

        String requestBody = """
        {
            "contents": [
                {
                    "parts": [
                        {
                            "text": "%s"
                        }
                    ]
                }
            ]
        }
        """.formatted(prompt.replace("\"", "\\\""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            System.out.println("=== DEBUG GEMINI RESPONSE ===");
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("=== END DEBUG ===");

            // Extraction améliorée
            return extractTranslationText(response.getBody());

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return "Translation error: " + e.getMessage();
        }
    }

    private String extractTranslationText(String jsonResponse) {
        try {
            // Méthode simple mais robuste pour extraire le texte
            int textIndex = jsonResponse.indexOf("\"text\": \"");
            if (textIndex == -1) {
                textIndex = jsonResponse.indexOf("\"text\":\"");
            }

            if (textIndex != -1) {
                int start = textIndex + (jsonResponse.contains("\"text\": \"") ? 9 : 8);
                int end = jsonResponse.indexOf("\"", start);
                if (end > start) {
                    String extracted = jsonResponse.substring(start, end);
                    // Nettoyer les sauts de ligne
                    return extracted.replace("\\n", "").replace("\n", "").trim();
                }
            }

            return "Could not extract translation";

        } catch (Exception e) {
            return "Error parsing response";
        }
    }
}