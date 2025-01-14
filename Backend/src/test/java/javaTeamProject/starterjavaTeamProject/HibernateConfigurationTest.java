package javaTeamProject.starterjavaTeamProject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import model.Resume;
import model.ResumeDTO;
import model.User;
import model.UserDTO;
import repository.ResumeRepository;
import repository.UserRepository;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateConfigurationTest {

	final String USER = "root";
	final String PASSWORD = "rootroot";
	final String DB_NAME= "test";
	final String PORT = "3306";



	ResumeRepository resumeRepository;
	UserRepository userRepository;

	@BeforeEach
	void setup(Vertx vertx, VertxTestContext context) {
		System.out.println("Running setup...");
		Properties hibernateProps = new Properties();
		String url = "jdbc:mysql://localhost:" + PORT + "/" + DB_NAME + "?useSSL=false";
		hibernateProps.put("hibernate.connection.url", url);
		hibernateProps.put("hibernate.connection.username", USER);
		hibernateProps.put("hibernate.connection.password", PASSWORD);
		hibernateProps.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		//hibernateProps.put("hibernate.show_sql", "true");
		//hibernateProps.put("hibernate.format_sql", "true");
		//hibernateProps.put("hibernate.use_sql_comments", "true");
		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.addProperties(hibernateProps);
		hibernateConfiguration.addAnnotatedClass(User.class);
		hibernateConfiguration.addAnnotatedClass(Resume.class);

		ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder().applySettings(hibernateConfiguration.getProperties()).build();
		Stage.SessionFactory sessionFactory = hibernateConfiguration.buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);
		resumeRepository = new ResumeRepository(sessionFactory);
		userRepository = new UserRepository(sessionFactory);
		context.completeNow();
	}

	@Test
	void createUserTest(Vertx vertx, VertxTestContext context) {
		UserDTO userDto = new UserDTO(null,"username", "userEmail@gmail.com", "userPassword","gender", "phone", 21,new Date(),new Date());
		context.verify(()->{
			userRepository.createUser(userDto).
			onFailure(err -> context.failNow(err))
			.onSuccess(r->{
				Assertions.assertNotNull(r);
				Assertions.assertNotNull(r.id());
        System.out.println(r.id());
				context.completeNow();
			});
		});
	}

  @Test
  void createResumeTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null, "username", "userEmail@gmail.com", "userPassword", "gender", "phone", 21, new Date(), new Date());
    context.verify(() -> {
      userRepository.createUser(userDto).compose(user -> {
          Assertions.assertNotNull(user.id());
          ResumeDTO resumeDto = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(user), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
          return resumeRepository.createResume(resumeDto);
        })
        .onSuccess(result -> {
          Assertions.assertNotNull(result);
          Assertions.assertNotNull(result.id());
          context.completeNow();
        })
        .onFailure(err -> context.failNow(err));
    });
  }

  @Test
  void userResumeRelationTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null, "username", "userEmail@gmail.com", "userPassword", "gender", "phone", 21, new Date(), new Date());
    context.verify(() -> {
      userRepository.createUser(userDto).compose(user -> {
          Assertions.assertNotNull(user);
          Assertions.assertNotNull(user.id());
          ResumeDTO resumeDto = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(user), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
          return resumeRepository.createResume(resumeDto);
        })
        .onSuccess(result -> {
          Assertions.assertTrue(result.user().isPresent());
          context.completeNow();
        })
        .onFailure(err -> context.failNow(err));
    });
  }

	@Test
	void findUserByIdDoesNotExist(Vertx vertx, VertxTestContext context) {
		context.verify(()-> {
			userRepository.findUserById(UUID.randomUUID())
			.onSuccess(r -> {
				Assertions.assertTrue(r.isEmpty());
				context.completeNow();
			})
			.onFailure(err -> {
				context.failNow(err);
			});
		});
	}

	@Test
	void findUserByIdDoesExistTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null,"username", "userEmail@gmail.com", "userPassword","gender", "phone", 21,new Date(),new Date());
		context.verify(()->{
			userRepository.createUser(userDto)
			.compose(u->{
				return userRepository.findUserById(u.id());
			})
			.onFailure(err -> context.failNow(err))
			.onSuccess(result->{
				Assertions.assertTrue(result.isPresent());
				context.completeNow();
			});
		});
	}

  @Test
  void findUserByEmailTest(Vertx vertx, VertxTestContext context){
    UserDTO userDto = new UserDTO(null, "username","userEmail@gmail.com", "userPassword","gender", "phone", 21,new Date(),new Date());
    context.verify(()-> {
      userRepository.createUser(userDto)
        .compose( u-> {
            return userRepository.findUserByEmail(u.email());
          }
        )
        .onFailure(err -> context.failNow(err))
        .onSuccess(result-> {
          Assertions.assertTrue(result.isPresent());
          context.completeNow();
        });
    });
  }

	@Test
	void removeUserTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null,"username", "userEmail@gmail.com", "userPassword","gender", "phone", 21,new Date(),new Date());
		context.verify(()->{
			userRepository.createUser(userDto)
			.compose(u -> {
        UUID userId = u.id();
				return userRepository.removeUser(u.id()).map(v -> userId);
			})
			.compose(u -> userRepository.findUserById(u)
			)
			.onFailure(err -> context.failNow(err))
			.onSuccess(u ->{
				Assertions.assertTrue(u.isEmpty());
				context.completeNow();
			});
		});
	}

	@Test
	void updateUserTest(Vertx vertx, VertxTestContext context){
    UserDTO userDto = new UserDTO(null, "username","userEmail@gmail.com", "userPassword","gender", "phone", 21,new Date(),new Date());
		context.verify(()-> {
				userRepository.createUser(userDto).compose( u-> {
				UserDTO updatedResume = new UserDTO(u.id(), "username","newUserEmail@gmail.com",
						"newUserPassword", "new gender", "new phone", 0, userDto.createdAt(), userDto.updatedAt());
				return userRepository.updateUser(updatedResume);
			}).compose(r -> {
				Assertions.assertEquals("newUserEmail@gmail.com",r.email());
				Assertions.assertEquals("newUserPassword",r.password());
        Assertions.assertEquals("new gender",r.gender());
        Assertions.assertEquals("new phone",r.phone());
        Assertions.assertEquals(0,r.age());
				return userRepository.findUserById(r.id());
			})
			.onFailure(err -> context.failNow(err))
			.onSuccess(r -> {
				Assertions.assertTrue(r.isPresent());
				UserDTO result = r.get();
        Assertions.assertEquals("newUserEmail@gmail.com",result.email());
        Assertions.assertEquals("newUserPassword",result.password());
        Assertions.assertEquals("new gender",result.gender());
        Assertions.assertEquals("new phone",result.phone());
        Assertions.assertEquals(0,result.age());
				context.completeNow();
			});
		});
	}

	@Test
	void findResumeByIdDoesNotExistTest(Vertx vertx, VertxTestContext context) {
		context.verify(()->{
			resumeRepository.findResumeById(UUID.randomUUID())
			.onSuccess(result ->{
				Assertions.assertTrue(result.isEmpty());
				context.completeNow();
			})
			.onFailure(err -> context.failNow(err));
		});
	}

  @Test
  void findResumeByIdExistTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null, "username", "userEmail@gmail.com", "userPassword", "gender", "phone", 21, new Date(), new Date());
    context.verify(() -> {
      userRepository.createUser(userDto).compose(user -> {
          ResumeDTO resumeDto = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(user), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
          return resumeRepository.createResume(resumeDto);
        })
        .compose(resume -> resumeRepository.findResumeById(resume.id()))
        .onSuccess(result -> {
          Assertions.assertTrue(result.isPresent());
          Assertions.assertNotNull(result);
          context.completeNow();
        })
        .onFailure(err -> context.failNow(err));
    });
  }

  @Test
  void removeResumeTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null, "username", "userEmail@gmail.com", "userPassword", "gender", "phone", 21, new Date(), new Date());
    context.verify(() -> {
      userRepository.createUser(userDto).compose(user -> {
          ResumeDTO resumeDto = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(user), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
          return resumeRepository.createResume(resumeDto);
        })
        .compose(resume -> resumeRepository.removeResume(resume.id()).map(v -> resume.id()))
        .compose(result -> resumeRepository.findResumeById(result))
        .onSuccess(r -> {
          Assertions.assertTrue(r.isEmpty());
          context.completeNow();
        })
        .onFailure(err -> context.failNow(err));
    });
  }

  @Test
  void updateResumeTest(Vertx vertx, VertxTestContext context) {
    UserDTO userDto = new UserDTO(null, "username", "userEmail@gmail.com", "userPassword", "gender", "phone", 21, new Date(), new Date());
    context.verify(() -> {
      userRepository.createUser(userDto).compose(user -> {
          ResumeDTO resumeDto = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(user), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
          return resumeRepository.createResume(resumeDto);
        })
        .compose(r -> {
          ResumeDTO newResume = new ResumeDTO(r.id(), 1, r.updatedAt(), r.createdAt(), r.user(), r.fullName(), r.position(), r.objective(), r.education(), r.workExperience(), r.skillsAndAwards(), r.languages(), r.recommendations(), r.hobbiesAndInterests());
          return resumeRepository.updateResume(newResume);
        })
        .compose(result -> {
          Assertions.assertEquals(1, result.template());
          return resumeRepository.findResumeById(result.id());
        })
        .onFailure(err -> context.failNow(err))
        .onSuccess(r -> {
          Assertions.assertTrue(r.isPresent());
          Assertions.assertEquals(1, r.get().template());
          context.completeNow();
        });
    });
  }

  @Test
  void findResumeByUserId(Vertx vertx, VertxTestContext context) {
    UserDTO userDto1 = new UserDTO(null, "username", "userEmail1@gmail.com", "userPassword1", "gender1", "phone1", 21, new Date(), new Date());
    UserDTO userDto2 = new UserDTO(null, "username", "userEmail2@gmail.com", "userPassword2", "gender2", "phone2", 22, new Date(), new Date());

    CompositeFuture usersCreations = CompositeFuture.join(
      userRepository.createUser(userDto1),
      userRepository.createUser(userDto2)
    );

    usersCreations.onSuccess(users -> {
      Assertions.assertTrue(users.succeeded());
      Assertions.assertTrue(users.isComplete());

      UserDTO createdUser1 = (UserDTO) users.list().get(0);
      UserDTO createdUser2 = (UserDTO) users.list().get(1);

      ResumeDTO resumeDto1 = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(createdUser1), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
      ResumeDTO resumeDto2 = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(createdUser1), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");
      ResumeDTO resumeDto3 = new ResumeDTO(null, 1, new Date(), new Date(), Optional.of(createdUser2), "Full Name", "Position", "Objective", "Education", "Work Experience", "Skills and Awards", "Languages", "Recommendations", "Hobbies and Interests");

      CompositeFuture resumesCreations = CompositeFuture.join(
        resumeRepository.createResume(resumeDto1),
        resumeRepository.createResume(resumeDto2),
        resumeRepository.createResume(resumeDto3)
      );

      resumesCreations.onSuccess(resumes -> {
        Assertions.assertTrue(resumes.succeeded());
        Assertions.assertTrue(resumes.isComplete());

        resumeRepository.findResumeByUserId(createdUser1.id())
          .onSuccess(resumesList -> {
            Assertions.assertNotNull(resumesList);
            Assertions.assertEquals(2, resumesList.resumes().size());
            context.completeNow();
          })
          .onFailure(err -> context.failNow(err));
      }).onFailure(err -> context.failNow(err));
    }).onFailure(err -> context.failNow(err));
  }
}
