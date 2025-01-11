package services;

import java.util.Optional;
import java.util.UUID;

import interfaces.IResumeService;
import io.vertx.core.Future;
import model.ResumeDTO;
import model.ResumesList;
import repository.ResumeRepository;
import model.ResumesList;

public record ResumeService(ResumeRepository repository) implements IResumeService {

	/*@Override
	public Future<ResumeDTO> updateResume(Principal principal, ResumeDTO Resume) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Void> removeResume(Principal principal, UUID id) {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public Future<ResumeDTO> createResume(ResumeDTO Resume) {
		// TODO Auto-generated method stub
		return repository.createResume(Resume);
	}

	@Override
	public Future<Optional<ResumeDTO>> findResumeById(UUID id) {
		// TODO Auto-generated method stub
		return repository.findResumeById(id);
	}

	@Override
	public Future<ResumeDTO> updateResume(ResumeDTO Resume) {
		// TODO Auto-generated method stub
		return repository.updateResume(Resume);
	}

	@Override
	public Future<Void> removeResume(UUID id) {
		// TODO Auto-generated method stub
		return repository.removeResume(id);
	}

	@Override
	public Future<ResumesList> findResumeByUserId(UUID userId) {
		// TODO Auto-generated method stub
		return repository.findResumeByUserId(userId);
	}

}
