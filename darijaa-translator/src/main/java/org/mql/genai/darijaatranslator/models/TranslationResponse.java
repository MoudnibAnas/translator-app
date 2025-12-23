package org.mql.genai.darijaatranslator.models;

public class TranslationResponse {
    private String originalText;
    private String translatedText;
    private String modelUsed;

    public TranslationResponse() {}

    public TranslationResponse(String originalText, String translatedText, String modelUsed) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.modelUsed = modelUsed;
    }

    // Getters et setters
    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }
}