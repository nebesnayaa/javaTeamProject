package model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User {

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
  private String username;
	private String email;
	private String password;
  private String gender;
  private String phone;
  private Integer age;
	private Date createdAt;  // Замінили LocalDateTime на Date
	private Date updatedAt;  // Замінили LocalDateTime на Date
	@OneToMany(mappedBy ="user")
	private List<Resume> resumes;

	public User() {

	}

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

  public String getUsername(){return this.username;}
  public void setUserName(String username){this.username = username;}

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

  public String getGender (){return this.gender;}
  public void setGender(String gender) {this.gender = gender;}

  public String getPhone(){return this.phone;}
  public void setPhone(String phone){this.phone = phone;}

  public Integer getAge(){return this.age;}
  public void setAge(Integer age){this.age = age;}

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
