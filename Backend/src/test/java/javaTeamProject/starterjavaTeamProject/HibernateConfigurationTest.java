package javaTeamProject.starterjavaTeamProject;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import javaTeamProject.model.Resume;
import javaTeamProject.model.ResumeDTO;
import javaTeamProject.model.User;
import javaTeamProject.repository.ResumeRepository;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateConfigurationTest {
	
	final String USER = "root";
	final String PASSWORD = "rootroot";
	final String DB_NAME= "test";
	final String PORT = "3306";
	
	
	
	ResumeRepository resumeRepository;
	
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
		context.completeNow();
	}
	
	@Test
	void createResumeTest(Vertx vertx, VertxTestContext context) {
		ResumeDTO resumeDto = new ResumeDTO(null, 1, "some content", 1, LocalDateTime.now(),LocalDateTime.now());
		context.verify(()->{
			resumeRepository.createResume(resumeDto)
			.onFailure(err -> context.failNow(err))
			.onSuccess(result -> {
				Assertions.assertNotNull(result);
				Assertions.assertNotNull(result.id());
				Assertions.assertEquals(1, result.id());
				context.completeNow();
			});
		});
	}
	
	@Test
	void findResumeByIdDoesNotExistTest(Vertx vertx, VertxTestContext context) {
		context.verify(()-> {
			resumeRepository.findResumeById(1)
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
	void findResumeByIdExistsTest(Vertx vertx, VertxTestContext context) {
		ResumeDTO resumeDto = new ResumeDTO(null, 1, "some content", 1, LocalDateTime.now(),LocalDateTime.now());
		context.verify(()->{
			resumeRepository.createResume(resumeDto)
			.compose(r -> resumeRepository.findResumeById(r.id()))
			.onFailure(err -> context.failNow(err))
			.onSuccess(result -> {
				Assertions.assertTrue(result.isPresent());
				context.completeNow();
			});
		});
	}

}

/*@Test
void initializeHibernateWithCodeTest(Vertx vertx, VertxTestContext context) {
	//Creating properties with config data
	
	Properties hibernateProps = new Properties();
	
	//url
	hibernateProps.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/test");
	
	//credentials
	hibernateProps.put("hibernate.connection.username", USER);
	hibernateProps.put("hibernate.connection.password", PASSWORD);
	
	//schema diagram
	hibernateProps.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
	hibernateProps.put("hibernate.hbm2ddl.auto", "update");

	//dialect *
	hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	
	//Creating Hiberate configuration
	Configuration hibernateConfiguration = new Configuration();
	hibernateConfiguration.setProperties(hibernateProps);
	hibernateConfiguration.addAnnotatedClass(User.class);
	hibernateConfiguration.addAnnotatedClass(Resume.class);
	
	//Creating ServiceRegistry
	ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder()
										.applySettings(hibernateConfiguration.getProperties()).build();
	
	//Creating SessionFactory
	Stage.SessionFactory sessionFactory = hibernateConfiguration.buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);
	
	//Doing something with db
	User user = new User();
	//user.setId(1);
	user.setEmail("test@gmail.com");
	user.setPassword("passwordFrom18_12_2024");
	user.setCreatedAt(LocalDateTime.now());
	user.setUpdatedAt(LocalDateTime.now());
	
	System.out.println("Task ID before insertion: " + user.getId());
	
	var insertionResult = sessionFactory.withTransaction((s,t) -> s.persist(user));
	
	Future<Void> future = Future.fromCompletionStage(insertionResult);
	context.verify(() -> future
			.onFailure(err -> context.failNow(err))
			.onSuccess(r -> {
				System.out.println("Task ID after insertion: " + user.getId());
				context.completeNow();
			}));
}*/
