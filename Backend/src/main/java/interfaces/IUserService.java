package interfaces;

import java.util.Optional;

import io.vertx.core.Future;
import javaTeamProject.model.UserDTO;
import services.Principal;

public interface IUserService {
	Future<UserDTO> updateUser(Principal principal, UserDTO user);
	
	Future<Void> removeUser(Principal principal, Integer id);
	
}
