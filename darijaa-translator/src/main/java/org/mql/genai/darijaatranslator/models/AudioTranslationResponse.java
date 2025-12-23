package org.mql.genai.darijaatranslator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AudioTranslationResponse {

    // Remove audioBase64 entirely or keep it as transient
    @JsonIgnore
    private String audioBase64;

    private String transcribedText;
    private String translatedText;
    private String modelUsed;

    // Default constructor (required for Jackson)
    public AudioTranslationResponse() {
    }

    // Constructor without audioBase64 (clean version)
    public AudioTranslationResponse(String transcribedText, String translatedText, String modelUsed) {
        this.transcribedText = transcribedText;
        this.translatedText = translatedText;
        this.modelUsed = modelUsed;
    }

    // Constructor with audioBase64 (if you need it internally)
    public AudioTranslationResponse(String audioBase64, String transcribedText, String translatedText, String modelUsed) {
        this.audioBase64 = audioBase64;
        this.transcribedText = transcribedText;
        this.translatedText = translatedText;
        this.modelUsed = modelUsed;
    }

    // Getters and Setters
    @JsonIgnore
    public String getAudioBase64() {
        return audioBase64;
    }

    @JsonIgnore
    public void setAudioBase64(String audioBase64) {
        this.audioBase64 = audioBase64;
    }

    @JsonProperty("transcribedText")
    public String getTranscribedText() {
        return transcribedText;
    }

    @JsonProperty("transcribedText")
    public void setTranscribedText(String transcribedText) {
        this.transcribedText = transcribedText;
    }

    @JsonProperty("translatedText")
    public String getTranslatedText() {
        return translatedText;
    }

    @JsonProperty("translatedText")
    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    @JsonProperty("modelUsed")
    public String getModelUsed() {
        return modelUsed;
    }

    @JsonProperty("modelUsed")
    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    // Optional: toString for debugging
    @Override
    public String toString() {
        return "AudioTranslationResponse{" +
                "transcribedText='" + transcribedText + '\'' +
                ", translatedText='" + translatedText + '\'' +
                ", modelUsed='" + modelUsed + '\'' +
                '}';
    }
}