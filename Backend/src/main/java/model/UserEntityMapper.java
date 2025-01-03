package model;

import java.util.function.Function;

public class UserEntityMapper implements Function<UserDTO, User> {

	@Override
	public User apply(UserDTO t) {
		User user = new User();
		user.setId(t.id());
		user.setEmail(t.email());
		user.setPassword(t.password());
		user.setUpdatedAt(t.updatedAt());
		user.setCreatedAt(t.createdAt());
		return user;
	}
}
