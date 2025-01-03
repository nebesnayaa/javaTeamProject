package model;

import java.time.LocalDateTime;
import java.util.Date;

public record UserDTO(Integer id, String email, String password, Date createdAt,  Date updatedAt) {
	
}
