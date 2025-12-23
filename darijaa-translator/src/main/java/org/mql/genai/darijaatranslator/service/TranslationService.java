package org.mql.genai.darijaatranslator.service;

import org.mql.genai.darijaatranslator.models.TranslationRequest;
import org.mql.genai.darijaatranslator.models.TranslationResponse;

public interface TranslationService {
    TranslationResponse translate(TranslationRequest request);
}