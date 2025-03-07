package com.akechsalim.community_service_management_2.util;

import com.akechsalim.community_service_management_2.dto.TaskDTO;
import com.akechsalim.community_service_management_2.dto.UserDTO;
import com.akechsalim.community_service_management_2.dto.UserWithTasksDTO;
import com.akechsalim.community_service_management_2.model.User;

import java.util.List;

public class DtoMapper {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }

    public static UserWithTasksDTO toUserWithTasksDTO(User user, List<TaskDTO> tasks) {
        UserWithTasksDTO dto = new UserWithTasksDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setTasks(tasks);
        return dto;
    }
}