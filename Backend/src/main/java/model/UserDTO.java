package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record UserDTO(UUID id, String email, String password, String gender, String phone, Integer age, Date createdAt, Date updatedAt) {

}
