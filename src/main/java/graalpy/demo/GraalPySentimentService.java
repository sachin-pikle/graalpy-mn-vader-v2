package graalpy.demo;

import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public final class GraalPySentimentService {
    private static final Logger LOG = LoggerFactory.getLogger(GraalPySentimentService.class);

    private final JsonMapper jsonMapper;
    private final SentimentModule sentimentModule;

    public GraalPySentimentService(JsonMapper jsonMapper, SentimentModule sentimentModule) {
        this.jsonMapper = jsonMapper;
        this.sentimentModule = sentimentModule;
    }

    public ReviewAnalysisView analyze(String fileName, byte[] fileBytes) {
        String reviewText = new String(fileBytes, StandardCharsets.UTF_8).trim();
        LOG.info("Running GraalPy VADER analysis for {}", fileName);
        String payload = sentimentModule.analyze_review_json(fileName, reviewText);
        try {
            return jsonMapper.readValue(payload.getBytes(StandardCharsets.UTF_8), ReviewAnalysisView.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse GraalPy sentiment response.", e);
        }
    }
}
