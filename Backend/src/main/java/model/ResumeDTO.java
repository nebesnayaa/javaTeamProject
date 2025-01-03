package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public record ResumeDTO(Integer id, String content, Integer templateId, Date createdAt,  Date updatedAt, Optional<UserDTO> user) {

}
