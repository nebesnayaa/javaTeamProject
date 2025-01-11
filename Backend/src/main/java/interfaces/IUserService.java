package interfaces;

import java.util.Optional;
import java.util.UUID;

import io.vertx.core.Future;
import model.UserDTO;
import services.Principal;

public interface IUserService {
	Future<UserDTO> updateUser(Principal principal, UserDTO user);

	Future<Void> removeUser(Principal principal, UUID id);

	Future<UserDTO> createUser(UserDTO user);

	Future<Optional<UserDTO>> findUserById(UUID id);

	Future<UserDTO> updateUser( UserDTO user);

	Future<Void> removeUser( UUID id);

}
