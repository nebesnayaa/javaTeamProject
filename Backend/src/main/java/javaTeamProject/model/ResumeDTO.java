package javaTeamProject.model;

import java.time.LocalDateTime;
import java.util.Optional;

public record ResumeDTO(Integer id, String content, Integer templateId, LocalDateTime createdAt,  LocalDateTime updatedAt, Optional<UserDTO> user) {

}
