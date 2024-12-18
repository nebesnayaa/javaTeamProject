package javaTeamProject.starterjavaTeamProject;

import java.time.LocalDateTime;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import javaTeamProject.model.User;

@ExtendWith(VertxExtension.class)
class HibernateConfigurationTest {
	
	@Test
	void initializeHibernateWithCodeTest(Vertx vertx, VertxTestContext context) {
		//Creating properties with config data
		
		Properties hibernateProps = new Properties();
		
		//url
		hibernateProps.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/test");
		
		//credentials
		hibernateProps.put("hibernate.connection.username", "root");
		hibernateProps.put("hibernate.connection.password", "rootroot");
		
		//schema diagram
		hibernateProps.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		hibernateProps.put("hibernate.hbm2ddl.auto", "update");

		//dialect *
		hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		
		//Creating Hiberate configuration
		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.setProperties(hibernateProps);
		hibernateConfiguration.addAnnotatedClass(User.class);
		
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
		context.verify(() -> future.onFailure(err -> context.failNow(err)).onSuccess(r -> {
			System.out.println("Task ID after insertion: " + user.getId());
			context.completeNow();
		}));
	}
}
