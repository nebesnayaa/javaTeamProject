package javaTeamProject.model;

import java.time.LocalDateTime;

public record UserDTO(Integer id, String email, String password, LocalDateTime createdAt,  LocalDateTime updatedAt) {
	
}
