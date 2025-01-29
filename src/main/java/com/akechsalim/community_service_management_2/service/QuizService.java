package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.model.Quiz;
import com.akechsalim.community_service_management_2.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public List<Quiz> getQuizzesByModule(Long moduleId) {
        return quizRepository.findByModuleId(moduleId);
    }

    public boolean submitQuizAnswers(Map<Long, String> answers) {
        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            Quiz quiz = quizRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Quiz not found"));
            if (!quiz.getCorrectAnswer().equals(entry.getValue())) {
                return false; // Incorrect answer
            }
        }
        return true; // All answers correct
    }
}

