package model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User {

	@Id @GeneratedValue
	private Integer id;
	private String email;
	private String password;
	private Date createdAt;  // Замінили LocalDateTime на Date
	private Date updatedAt;  // Замінили LocalDateTime на Date
	@OneToMany(mappedBy ="user")
	private List<Resume> resumes;

	public User() {

	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != this.getClass()) return false;
		User user = (User)obj;
		return user.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, createdAt, updatedAt);
	}
}
