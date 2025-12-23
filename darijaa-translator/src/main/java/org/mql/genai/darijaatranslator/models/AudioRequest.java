package org.mql.genai.darijaatranslator.models;

public class AudioRequest {
    private String audioBase64;
    private String audioFormat;

    public AudioRequest() {}

    public AudioRequest(String audioBase64, String audioFormat) {
        this.audioBase64 = audioBase64;
        this.audioFormat = audioFormat;
    }

    public String getAudioBase64() { return audioBase64; }
    public void setAudioBase64(String audioBase64) { this.audioBase64 = audioBase64; }

    public String getAudioFormat() { return audioFormat; }
    public void setAudioFormat(String audioFormat) { this.audioFormat = audioFormat; }

}