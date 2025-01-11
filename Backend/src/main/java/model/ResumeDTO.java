package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


public record ResumeDTO(UUID id, String content, Integer templateId, Date createdAt, Date updatedAt, Optional<UserDTO> user) {

}
