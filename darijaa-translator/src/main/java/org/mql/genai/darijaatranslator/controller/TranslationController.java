package org.mql.genai.darijaatranslator.controller;

import org.mql.genai.darijaatranslator.models.TranslationRequest;
import org.mql.genai.darijaatranslator.models.TranslationResponse;
import org.mql.genai.darijaatranslator.models.AudioRequest;
import org.mql.genai.darijaatranslator.models.AudioTranslationResponse;
import org.mql.genai.darijaatranslator.service.TranslationService;
import org.mql.genai.darijaatranslator.service.AudioTranslationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TranslationController {

    private final TranslationService translationService;
    private final AudioTranslationService audioTranslationService;

    public TranslationController(TranslationService translationService,
                                 AudioTranslationService audioTranslationService) {
        this.translationService = translationService;
        this.audioTranslationService = audioTranslationService;
    }

    // ========== TEXT-TO-TEXT ==========

    @PostMapping(
            value = "/translate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> translateText(@RequestBody TranslationRequest request) {
        try {
            System.out.println(" Received text translation: " + request.getEnglishText());
            TranslationResponse response = translationService.translate(request);
            System.out.println(" Text translation result: " + response.getTranslatedText());

            // Return consistent format
            Map<String, String> result = new HashMap<>();
            result.put("originalText", response.getOriginalText());
            result.put("translatedText", response.getTranslatedText());
            result.put("modelUsed", response.getModelUsed());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Text translation error: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("originalText", request.getEnglishText());
            errorResponse.put("translatedText", "Translation service error: " + e.getMessage());
            errorResponse.put("modelUsed", "error");
            return ResponseEntity.ok(errorResponse);
        }
    }

    @GetMapping(value = "/translate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> translateTextGet(@RequestParam String text) {
        try {
            TranslationRequest request = new TranslationRequest(text);
            TranslationResponse response = translationService.translate(request);

            Map<String, String> result = new HashMap<>();
            result.put("originalText", response.getOriginalText());
            result.put("translatedText", response.getTranslatedText());
            result.put("modelUsed", response.getModelUsed());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("originalText", text);
            errorResponse.put("translatedText", "Translation service error");
            errorResponse.put("modelUsed", "error");
            return ResponseEntity.ok(errorResponse);
        }
    }

    // ========== AUDIO-TO-TEXT  ==========

    @PostMapping(
            value = "/audio/translate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> translateAudio(@RequestBody AudioRequest request) {
        try {
            System.out.println(" Received audio translation request");
            System.out.println(" Audio format: " + request.getAudioFormat());
            System.out.println(" Audio data length: " + (request.getAudioBase64() != null ? request.getAudioBase64().length() : 0));

            AudioTranslationResponse response = audioTranslationService.translate(request);


            System.out.println("=== DEBUGING ===");
            System.out.println("Transcribed text: " + response.getTranscribedText());
            System.out.println("Translated text: " + response.getTranslatedText());
            System.out.println("Model used: " + response.getModelUsed());

            // Return consistent format that frontend expects
            Map<String, String> result = new HashMap<>();
            result.put("transcribedText", response.getTranscribedText());
            result.put("translatedText", response.getTranslatedText());
            result.put("modelUsed", response.getModelUsed());

            System.out.println("Audio translation successful ");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println(" Audio translation error: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("transcribedText", "Audio transcription failed: " + e.getMessage());
            errorResponse.put("translatedText", "Audio translation failed");
            errorResponse.put("modelUsed", "error");
            return ResponseEntity.ok(errorResponse);
        }
    }

    @PostMapping(
            value = "/audio/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> uploadAndTranslateAudio(
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Received audio file: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize() + " bytes");
            System.out.println("Content type: " + file.getContentType());

            // Check if file is empty
            if (file.isEmpty()) {
                throw new IOException("Uploaded file is empty");
            }

            // Convert file to Base64
            byte[] audioBytes = file.getBytes();
            String audioBase64 = Base64.getEncoder().encodeToString(audioBytes);

            // Determine format
            String format = getFileFormat(file.getOriginalFilename());
            System.out.println(" Detected format: " + format);

            // Create request
            AudioRequest audioRequest = new AudioRequest(audioBase64, format);

            // Translate
            AudioTranslationResponse response = audioTranslationService.translate(audioRequest);

            // Return consistent format
            Map<String, String> result = new HashMap<>();
            result.put("transcribedText", response.getTranscribedText());
            result.put("translatedText", response.getTranslatedText());
            result.put("modelUsed", response.getModelUsed());

            return ResponseEntity.ok(result);

        } catch (IOException e) {
            System.out.println(" File upload error: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("transcribedText", "File upload failed: " + e.getMessage());
            errorResponse.put("translatedText", "Audio upload failed");
            errorResponse.put("modelUsed", "error");
            return ResponseEntity.ok(errorResponse);
        }
    }


    private String getFileFormat(String filename) {
        if (filename == null) return "webm";
        String lowerName = filename.toLowerCase();
        if (lowerName.endsWith(".mp3")) return "mp3";
        if (lowerName.endsWith(".wav")) return "wav";
        if (lowerName.endsWith(".ogg")) return "ogg";
        if (lowerName.endsWith(".webm")) return "webm";
        return "webm"; // Default to webm since that's what recorder uses
    }
}