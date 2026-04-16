package graalpy.demo;

import io.micronaut.graal.graalpy.annotations.GraalPyModule;

@GraalPyModule("sentiment_app")
public interface SentimentModule {
    String analyze_review_json(String fileName, String reviewText);
}
