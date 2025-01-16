package services;

import java.util.Optional;
import java.util.UUID;

import interfaces.IResumeService;
import io.vertx.core.Future;
import model.ResumeDTO;
import model.ResumesList;
import repository.ResumeRepository;

/**
 * Service for managing resumes.
 * Implements the {@link IResumeService} interface and provides methods to interact with the resume repository.
 */
public record ResumeService(ResumeRepository repository) implements IResumeService {

    /**
     * Creates a new resume.
     *
     * @param resume {@link ResumeDTO} object containing the resume data.
     * @return {@link Future} containing the created resume.
     */
    @Override
    public Future<ResumeDTO> createResume(ResumeDTO resume) {
        return repository.createResume(resume);
    }

    /**
     * Finds a resume by its ID.
     *
     * @param id the unique identifier of the resume.
     * @return {@link Future} containing an {@link Optional} with the found resume or an empty value.
     */
    @Override
    public Future<Optional<ResumeDTO>> findResumeById(UUID id) {
        return repository.findResumeById(id);
    }

    /**
     * Updates an existing resume.
     *
     * @param resume {@link ResumeDTO} object containing the updated resume data.
     * @return {@link Future} containing the updated resume.
     */
    @Override
    public Future<ResumeDTO> updateResume(ResumeDTO resume) {
        return repository.updateResume(resume);
    }

    /**
     * Removes a resume by its ID.
     *
     * @param id the unique identifier of the resume.
     * @return {@link Future} indicating successful removal of the resume.
     */
    @Override
    public Future<Void> removeResume(UUID id) {
        return repository.removeResume(id);
    }

    /**
     * Finds all resumes associated with a user by their ID.
     *
     * @param userId the unique identifier of the user.
     * @return {@link Future} containing a list of resumes {@link ResumesList}.
     */
    @Override
    public Future<ResumesList> findResumeByUserId(UUID userId) {
        return repository.findResumeByUserId(userId);
    }
}
