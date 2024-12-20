package javaTeamProject.model;

import java.time.LocalDateTime;

public record ResumeDTO(Integer id, Integer userId, String content, Integer templateId, LocalDateTime createdAt,  LocalDateTime updatedAt) {

}
