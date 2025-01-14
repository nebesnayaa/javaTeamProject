package model;

import java.time.LocalDateTime;
import java.util.function.Function;

public class UserDtoMapper implements Function<User, UserDTO> {

	@Override
	public UserDTO apply(User t) {
		return new UserDTO(t.getId(),t.getUsername(), t.getEmail(), t.getPassword(), t.getGender(), t.getPhone(), t.getAge(), t.getCreatedAt(), t.getUpdatedAt());
	}

}
