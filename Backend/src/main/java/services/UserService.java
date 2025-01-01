package services;

import java.util.Objects;

import interfaces.IUserService;
import io.vertx.core.Future;
import javaTeamProject.model.UserDTO;
import javaTeamProject.repository.UserRepository;

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

}
