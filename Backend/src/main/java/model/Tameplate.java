package model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name ="template")
public class Tameplate {
	@Id @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;
	String name;
	String layout;

	@OneToMany(mappedBy="template")
	List<Resume> resumes;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLayout() {
		return this.layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
}
