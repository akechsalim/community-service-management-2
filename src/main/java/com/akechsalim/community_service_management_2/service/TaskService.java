package com.akechsalim.community_service_management_2.service;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.model.Task;
import com.akechsalim.community_service_management_2.model.TaskStatus;
import com.akechsalim.community_service_management_2.model.User;
import com.akechsalim.community_service_management_2.repository.TaskRepository;
import com.akechsalim.community_service_management_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        User volunteer = userRepository.findById(taskDTO.getVolunteerId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Task task = new Task(taskDTO.getTitle(), taskDTO.getDescription(), volunteer);
        task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : TaskStatus.PENDING);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
    public List<Task> getTasksByVolunteerId(Long volunteerId) {
        return taskRepository.findByVolunteerId(volunteerId);
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        if (taskDTO.getVolunteerId() != null) {
            User volunteer = userRepository.findById(taskDTO.getVolunteerId())
                    .orElseThrow(() -> new RuntimeException("Volunteer not found"));
            task.setVolunteer(volunteer);
        }
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public List<TaskDTO> getTasksForVolunteer(Long volunteerId) {
        return taskRepository.findByVolunteerId(volunteerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public void completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setVolunteerId(task.getVolunteer().getId());
        return dto;
    }
}