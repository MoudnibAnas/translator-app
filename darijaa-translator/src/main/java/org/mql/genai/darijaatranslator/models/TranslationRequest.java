package org.mql.genai.darijaatranslator.models;

public class TranslationRequest {
    private String englishText;

    public TranslationRequest() {}

    public TranslationRequest(String englishText) {
        this.englishText = englishText;
    }

    public String getEnglishText() {
        return englishText;
    }

    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }
}