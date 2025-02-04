package com.akechsalim.community_service_management_2.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TrainingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private User volunteer;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private TrainingModule module;

    private boolean completed;
    private LocalDateTime completedAt;
    private boolean certificateApproved;

    public TrainingProgress() {
    }

    public TrainingProgress(User volunteer, TrainingModule module, boolean completed, LocalDateTime completedAt, boolean certificateApproved) {
        this.volunteer = volunteer;
        this.module = module;
        this.completed = completed;
        this.completedAt = completedAt;
        this.certificateApproved = certificateApproved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }

    public TrainingModule getModule() {
        return module;
    }

    public void setModule(TrainingModule module) {
        this.module = module;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isCertificateApproved() {
        return certificateApproved;
    }

    public void setCertificateApproved(boolean certificateApproved) {
        this.certificateApproved = certificateApproved;
    }
}
