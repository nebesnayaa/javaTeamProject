package javaTeamProject.model;

import java.time.LocalDateTime;
import java.util.function.Function;

public class UserDtoMapper implements Function<User, UserDTO> {

	@Override
	public UserDTO apply(User t) {
		return new UserDTO(t.getId(), t.getEmail(), t.getPassword(), t.getCreatedAt(), t.getUpdatedAt());
	}

}
