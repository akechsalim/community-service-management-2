package com.akechsalim.community_service_management_2.dto;

import java.time.LocalDateTime;

public class TrainingModuleDTO {
    private String title;
    private String description;
    private String content;
    private String resourceUrl;
    private String videoUrl;

    public TrainingModuleDTO() {
    }

    public TrainingModuleDTO(String title, String description, String content, String resourceUrl, String videoUrl) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.resourceUrl = resourceUrl;
        this.videoUrl = videoUrl;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getResourceUrl() { return resourceUrl; }
    public void setResourceUrl(String resourceUrl) { this.resourceUrl = resourceUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
}
