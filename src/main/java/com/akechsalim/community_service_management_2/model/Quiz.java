package com.akechsalim.community_service_management_2.model;

import jakarta.persistence.*;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private TrainingModule module;

    private String question;
    private String correctAnswer;

    public Quiz() {
    }

    public Quiz(TrainingModule module, String question, String correctAnswer) {
        this.module = module;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingModule getModule() {
        return module;
    }

    public void setModule(TrainingModule module) {
        this.module = module;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
