package interfaces;

import java.util.Optional;
import java.util.UUID;

import io.vertx.core.Future;
import model.ResumeDTO;
import model.ResumesList;
import services.Principal;

public interface IResumeService {
	//Future<ResumeDTO> updateResume(Principal principal, ResumeDTO Resume);

	//Future<Void> removeResume(Principal principal, UUID id);

	Future<ResumeDTO> createResume(ResumeDTO Resume);

	Future<Optional<ResumeDTO>> findResumeById(UUID id);

	Future<ResumesList> findResumeByUserId(UUID userId);

	Future<ResumeDTO> updateResume( ResumeDTO Resume);

	Future<Void> removeResume( UUID id);
}
