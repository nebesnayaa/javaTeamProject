package javaTeamProject.model;

import java.util.function.Function;

public class ResumeDtoMapper implements Function<Resume, ResumeDTO> {

	@Override
	public ResumeDTO apply(Resume t) {
		return new ResumeDTO(t.getId(), t.getUserId(), t.getContent(), t.getTemplateId(), t.getCreatedAt(), t.getUpdatedAt());
	}

}
