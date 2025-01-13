package com.akechsalim.community_service_management_2.controller;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.model.TaskStatus;
import com.akechsalim.community_service_management_2.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO(null, "Test Task", "Description", TaskStatus.PENDING, 1L);
        TaskDTO createdTaskDTO = new TaskDTO(1L, "Test Task", "Description", TaskStatus.PENDING, 1L);

        when(taskService.createTask(taskDTO)).thenReturn(createdTaskDTO);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Task\",\"description\":\"Description\",\"status\":\"PENDING\",\"volunteerId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));

        verify(taskService, times(1)).createTask(any(TaskDTO.class));

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO(1L, "Updated Task", "Updated Description", TaskStatus.ASSIGNED, 1L);

        when(taskService.updateTask(1L, taskDTO)).thenReturn(taskDTO);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"status\":\"ASSIGNED\",\"volunteerId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskDTO.class));
    }

    @Test
    @WithMockUser(roles = {"VOLUNTEER"})
    void testGetTasksForVolunteer() throws Exception {
        TaskDTO task1 = new TaskDTO(1L, "Task 1", "Desc 1", TaskStatus.PENDING, 1L);
        TaskDTO task2 = new TaskDTO(2L, "Task 2", "Desc 2", TaskStatus.COMPLETED, 1L);
        List<TaskDTO> tasks = Arrays.asList(task1, task2);

        when(taskService.getTasksForVolunteer(1L)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/volunteer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"));

        verify(taskService, times(1)).getTasksForVolunteer(1L);
    }
}
