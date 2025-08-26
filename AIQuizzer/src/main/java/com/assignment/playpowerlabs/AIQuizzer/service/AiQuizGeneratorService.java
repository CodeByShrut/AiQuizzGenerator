package com.assignment.playpowerlabs.AIQuizzer.service;

import com.assignment.playpowerlabs.AIQuizzer.dto.CreateQuizRequest;
import com.assignment.playpowerlabs.AIQuizzer.model.Question;
import com.assignment.playpowerlabs.AIQuizzer.model.Quiz;
import com.assignment.playpowerlabs.AIQuizzer.model.User;
import com.assignment.playpowerlabs.AIQuizzer.repository.QuizRepository;
import com.assignment.playpowerlabs.AIQuizzer.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiQuizGeneratorService {
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${openrouter.api.key}")
    private String openAiApiKey;
    @Value("${openrouter.model}")
    private String openAiModel;

    public AiQuizGeneratorService(QuizRepository quizRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.webClient = WebClient.builder().baseUrl("https://openrouter.ai/api/v1/chat/completions").build();
    }

    public Quiz generateQuiz(CreateQuizRequest request, String username) {
        if (request.getGrade() < 1 || request.getGrade() > 12) {
            throw new RuntimeException("Grade Must be Between 1-12");
        }
        String prompt = String.format("""
            You are a strict JSON quiz generator.

            Generate ONLY ONE quiz with the following format:
            {
              "quizId": "quiz_xyz123",
              "questions": [
                {
                  "questionId": "q1",
                  "question": "Sample question text?",
                  "options": ["Option A", "Option B", "Option C", "Option D"],
                  "correctAnswer": "Option A",
                  "lightHint": "Light hint text",
                  "strongHint": "Strong hint text"
                }
              ]
            }

            Rules:
            - Generate exactly %d questions.
            - Grade level: %d.
            - Subject: %s.
            - Difficulty: %s.
            - Each question MUST include 'lightHint' and 'strongHint'.
            - The response MUST be valid JSON. Start with `{` and end with `}`.
        """, request.getTotalQuestions(), request.getGrade(), request.getSubject(), request.getDifficulty());

        JsonNode response = (JsonNode) ((WebClient.RequestBodySpec) ((WebClient.RequestBodySpec) this.webClient.post().header("Authorization", new String[]{"Bearer " + this.openAiApiKey})).header("Content-Type", new String[]{"application/json"})).bodyValue(Map.of("model", this.openAiModel, "messages", List.of(Map.of("role", "user", "content", prompt)), "temperature", 0.3)).retrieve().bodyToMono(JsonNode.class).block();
        if (response != null && response.get("choices") != null) {
            String aiContent = response.get("choices").get(0).get("message").get("content").asText().trim();
            System.out.println("AI Raw:\n" + aiContent);
            String json = this.extractFirstJsonObject(aiContent);
            List<Question> questions = new ArrayList();
            String quizId = UUID.randomUUID().toString();

            try {
                JsonNode root = this.objectMapper.readTree(json);
                if (root.has("quizId")) {
                    quizId = root.get("quizId").asText();
                }

                if (!root.has("questions") || !root.get("questions").isArray()) {
                    throw new RuntimeException("JSON missing 'questions' array.");
                }

                for (JsonNode node : root.get("questions")) {
                    Question q = new Question();
                    q.setQuestionText(node.get("question").asText());
//                    List<String> opts = new ArrayList();
//
//                    for (JsonNode opt : node.get("options")) {
//                        opts.add(opt.asText());
//                    }

//                    q.setOptions(opts);
                    q.setCorrectAnswer(node.get("correctAnswer").asText());
                    q.setLightHint(node.has("lightHint") ? node.get("lightHint").asText() : null);
                    q.setStrongHint(node.has("strongHint") ? node.get("strongHint").asText() : null);
                    questions.add(q);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse AI response: " + e.getMessage());
            }

            User user = (User) this.userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            Quiz quiz = new Quiz();
            quiz.setGrade(request.getGrade());
            quiz.setSubject(request.getSubject());
            quiz.setTotalQuestions(request.getTotalQuestions());
            quiz.setMaxScore(request.getMaxScore());
            quiz.setDifficulty(request.getDifficulty());
            quiz.setGenerationDate(LocalDate.now());
            quiz.setQuestions(questions);
            quiz.setUser(user);
            for (Question question : questions) {
                quiz.addQuestion(question);
            }
            return (Quiz) this.quizRepository.save(quiz);
        } else {
            throw new RuntimeException("Invalid AI response: missing 'choices'.");
        }
    }

    private String extractFirstJsonObject(String input) {
        int start = input.indexOf(123);
        int braceCount = 0;
        boolean inside = false;

        for (int i = start; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c == '{') {
                ++braceCount;
                inside = true;
            } else if (c == '}') {
                --braceCount;
                if (braceCount == 0 && inside) {
                    return input.substring(start, i + 1);
                }
            }
        }

        throw new RuntimeException("No valid JSON object found in AI response.");
    }
}
