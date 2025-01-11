package interfaces;

import java.util.Optional;
import java.util.UUID;

import io.vertx.core.Future;
import model.ResumeDTO;
import model.ResumesList;


public interface IResumeRepository {

	Future<ResumeDTO> createResume(ResumeDTO resume);

	Future<ResumeDTO> updateResume(ResumeDTO resume);

	Future<Void> removeResume(UUID id);

	Future<Optional<ResumeDTO>> findResumeById (UUID id);

	Future<ResumesList> findResumeByUserId (UUID userId);
}
