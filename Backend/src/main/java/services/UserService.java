package services;

import java.util.Objects;
import java.util.Optional;

import interfaces.IUserService;
import io.vertx.core.Future;
import model.UserDTO;
import repository.UserRepository;

public record UserService(UserRepository repository) implements IUserService {

	@Override
	public Future<UserDTO> updateUser(Principal principal, UserDTO userDTO) {
		Integer principalId = principal.Id();
		return repository.findUserById(principalId).compose(result -> {
			if(result.isEmpty()) {
				return Future.failedFuture(new RuntimeException());
			}
			UserDTO userPrincipal = result.get();
			if(Objects.equals(userPrincipal.id(), userDTO.id())) {
				return repository.updateUser(userDTO);
			}
			else {
				return Future.failedFuture(new NotOwnerException());
			}
		});
	}

	@Override
	public Future<Void> removeUser(Principal principal, Integer id) {
		Integer principalId = principal.Id();
		return repository.findUserById(principalId).compose(result -> {
			if(result.isEmpty()) {
				return Future.failedFuture(new RuntimeException());
			}
			UserDTO userPrincipal = result.get();
			if(Objects.equals(userPrincipal.id(), id)) {
				return repository.removeUser(id);
			}
			else {
				return Future.failedFuture(new NotOwnerException());
			}
		});
	}

	@Override
	public Future<UserDTO> createUser(UserDTO user) {
		return repository.createUser(user);
	}

	@Override
	public Future<Optional<UserDTO>> findUserById(Integer id) {
		// TODO Auto-generated method stub
		return repository.findUserById(id);
	}

	@Override
	public Future<UserDTO> updateUser(UserDTO user) {
		// TODO Auto-generated method stub
		return repository.updateUser(user);
	}

	@Override
	public Future<Void> removeUser(Integer id) {
		// TODO Auto-generated method stub
		return repository.removeUser(id);
	}

}
