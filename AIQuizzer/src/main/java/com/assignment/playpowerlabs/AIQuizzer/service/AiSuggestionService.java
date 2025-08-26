package com.assignment.playpowerlabs.AIQuizzer.service;

import com.assignment.playpowerlabs.AIQuizzer.model.AttemptResponse;
import com.assignment.playpowerlabs.AIQuizzer.model.Question;
import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class AiSuggestionService {

    @Value("${openrouter.api.key}")
    private String openAiApiKey;

    @Value("${openrouter.model}")
    private String openAiModel;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiSuggestionService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://openrouter.ai/api/v1/chat/completions")
                .build();
    }

    public String generateSuggestions(Quiz quiz, List<AttemptResponse> responses) {
        StringBuilder prompt = new StringBuilder("You are an expert tutor. Given the quiz questions and user's responses, generate 2 personalized improvement suggestions. Focus on understanding gaps and how the student can improve:\n\n");

        int i = 1;
        for (Question q : quiz.getQuestions()) {
            prompt.append("Q").append(i).append(": ").append(q.getQuestionText()).append("\n");
            prompt.append("Correct Answer: ").append(q.getCorrectAnswer()).append("\n");

            AttemptResponse match = responses.stream()
                    .filter(r -> r.getQuestionId().equals(q.getId()))
                    .findFirst().orElse(null);

            if (match != null) {
                prompt.append("User's Answer: ").append(match.getUserResponse()).append("\n");
            } else {
                prompt.append("User's Answer: Not answered\n");
            }
            prompt.append("\n");
            i++;
        }

        JsonNode response = webClient.post()
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "model", openAiModel,
                        "messages", List.of(Map.of("role", "user", "content", prompt.toString())),
                        "temperature", 0.3
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response != null && response.get("choices") != null) {
            return response.get("choices").get(0).get("message").get("content").asText().trim();
        } else {
            throw new RuntimeException("Failed to fetch suggestions from AI.");
        }
    }
}
