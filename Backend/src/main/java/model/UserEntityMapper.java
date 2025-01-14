package model;

import java.util.function.Function;

public class UserEntityMapper implements Function<UserDTO, User> {

	@Override
	public User apply(UserDTO t) {
		User user = new User();
    user.setUserName(t.username());
		user.setId(t.id());
		user.setEmail(t.email());
		user.setPassword(t.password());
    user.setGender(t.gender());
    user.setPhone(t.phone());
    user.setAge(t.age());
		user.setUpdatedAt(t.updatedAt());
		user.setCreatedAt(t.createdAt());
		return user;
	}
}
