package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.model.Quiz;
import com.akechsalim.community_service_management_2.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Quiz>> getQuizzesByModule(@PathVariable Long moduleId) {
        List<Quiz> quizzes = quizService.getQuizzesByModule(moduleId);
        return ResponseEntity.ok(quizzes);
    }

    @PostMapping("/submit")
    public ResponseEntity<Boolean> submitQuizAnswers(@RequestBody Map<Long, String> answers) {
        boolean result = quizService.submitQuizAnswers(answers);
        return ResponseEntity.ok(result);
    }
}
