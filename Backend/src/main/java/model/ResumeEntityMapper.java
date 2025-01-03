package model;

import java.util.function.Function;

public class ResumeEntityMapper implements Function<ResumeDTO, Resume> {

	@Override
	public Resume apply(ResumeDTO t) {
		Resume resume = new Resume();
		resume.setId(t.id());
		resume.setContent(t.content());
		resume.setCreatedAt(t.createdAt());
		resume.setUpdatedAt(t.updatedAt());
		resume.setTemplateId(t.templateId());
		t.user().ifPresent(userDTO -> {
            User user = new UserEntityMapper().apply(userDTO);
            resume.setUser(user);
        });
		return resume;
	}

}
