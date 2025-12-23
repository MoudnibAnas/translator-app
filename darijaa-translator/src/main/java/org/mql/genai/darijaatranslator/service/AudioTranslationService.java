package org.mql.genai.darijaatranslator.service;

import org.mql.genai.darijaatranslator.models.AudioRequest;
import org.mql.genai.darijaatranslator.models.AudioTranslationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AudioTranslationService {



    @Value("${gemini.model.name}")
    private String modelName;

    @Value("${gemini.model.url}")
    private String modelUrl;

    @Value("${gemini.audio.prompt}")
    private String audioPrompt;

    private final RestTemplate restTemplate;

    public AudioTranslationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AudioTranslationResponse translate(AudioRequest request) {
        try {
            String translatedText = transcribeAndTranslateWithGemini(request);

            if (translatedText == null || translatedText.isBlank()
                    || translatedText.contains("Could not")
                    || translatedText.contains("Audio translation failed")
                    || translatedText.contains("Not English")) {

                return new AudioTranslationResponse(
                        "Translation failed",
                        "Could not translate audio",
                        modelName + " (error)"
                );
            }

            return new AudioTranslationResponse(
                    "Audio processed",
                    translatedText,
                    modelName
            );

        } catch (Exception e) {
            return new AudioTranslationResponse(
                    "Error: " + e.getMessage(),
                    "Audio processing failed",
                    "error"
            );
        }
    }

    private String transcribeAndTranslateWithGemini(AudioRequest request) {

        // Request body (configured)
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", audioPrompt),
                                Map.of("inline_data", Map.of(
                                        "mime_type", getMimeType(request.getAudioFormat()),
                                        "data", request.getAudioBase64()
                                ))
                        ))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.exchange(modelUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

                List<Map<String, Object>> candidates =
                        (List<Map<String, Object>>) response.getBody().get("candidates");

                if (candidates != null && !candidates.isEmpty()) {

                    Map<String, Object> contentMap =
                            (Map<String, Object>) candidates.get(0).get("content");

                    List<Map<String, Object>> parts =
                            (List<Map<String, Object>>) contentMap.get("parts");

                    if (parts != null && !parts.isEmpty()) {
                        String raw = (String) parts.get(0).get("text");
                        return cleanResponse(raw);
                    }
                }

                return "Could not translate audio: Empty response";
            }

            if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS)
                return "Rate limited - try again later";

            return "Could not translate audio: API error " + response.getStatusCode();

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("429"))
                return "Rate limited - try again later";

            return "Audio translation failed: " + e.getMessage();
        }
    }

    private String cleanResponse(String raw) {
        if (raw == null) return "";

        String cleaned = raw.trim();

        // Remove generic labels
        cleaned = cleaned
                .replace("Here is the Moroccan Arabic (Darija) translation:", "")
                .replace("Translation:", "")
                .replace("Transcription:", "")
                .trim();

        return cleaned;
    }

    private String getMimeType(String format) {
        return switch (format.toLowerCase()) {
            case "wav", "wave" -> "audio/wav";
            case "mp3" -> "audio/mpeg";
            case "ogg" -> "audio/ogg";
            case "webm" -> "audio/webm";
            default -> "audio/*";
        };
    }
}
