package javaTeamProject.model;

import java.util.function.Function;

public class ResumeEntityMapper implements Function<ResumeDTO, Resume> {

	@Override
	public Resume apply(ResumeDTO t) {
		Resume resume = new Resume();
		resume.setId(t.id());
		resume.setUserId(t.userId());
		resume.setContent(t.content());
		resume.setCreatedAt(t.createdAt());
		resume.setUpdatedAt(t.updatedAt());
		resume.setTemplateId(t.templateId());
		return resume;
	}

}
