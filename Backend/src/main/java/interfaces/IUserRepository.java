package interfaces;

import java.util.Optional;
import java.util.UUID;

import io.vertx.core.Future;
import model.UserDTO;

public interface IUserRepository {

	Future<UserDTO> createUser(UserDTO user);

	Future<UserDTO> updateUser(UserDTO user);

	Future<Void> removeUser(UUID id);

	Future<Optional<UserDTO>> findUserById (UUID id);

}
