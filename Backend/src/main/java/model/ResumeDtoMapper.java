package model;

import java.util.Optional;
import java.util.function.Function;

public class ResumeDtoMapper implements Function<Resume, ResumeDTO> {

	@Override
	public ResumeDTO apply(Resume t) {
		return new ResumeDTO(t.getId(), t.getContent(), t.getTemplateId(), t.getCreatedAt(), t.getUpdatedAt(), Optional.of(t.getUser()));
	}

}
