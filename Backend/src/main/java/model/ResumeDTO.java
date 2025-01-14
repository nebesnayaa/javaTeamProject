package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


public record ResumeDTO(UUID id,
                        Integer template,
                        Date createdAt,
                        Date updatedAt,
                        Optional<UserDTO> user,
                        String fullName,
                        String position,
                        String objective,
                        String education,
                        String workExperience,
                        String skillsAndAwards,
                        String languages,
                        String recommendations,
                        String hobbiesAndInterests) {

}
