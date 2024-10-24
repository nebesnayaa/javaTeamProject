package javaTeamProject.starterjavaTeamProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;

public class DbOperations {
	private static final Dotenv dotenv = Dotenv.load();
	
	private static Connection getConnection() throws SQLException {
	      return DriverManager.getConnection(dotenv.get("DB_URL"),
								    		  dotenv.get("USER"),
								    		  dotenv.get("PASSWORD"));
	  }
	  
	  private static void executeUpdate(String sql, String successMessage) {
	      try (Connection conn = getConnection();
	          Statement stmt = conn.createStatement()) {      	
	          stmt.executeUpdate(sql);
	          System.out.println(successMessage);

	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	  }
	  
	  public static void insertData(String tableName, String[] fields, String[] params) {
		    if (fields.length != params.length) {
		        throw new IllegalArgumentException("Количество полей и значений должно совпадать.");
		    }

		    StringBuilder sql = new StringBuilder();
		    sql.append("INSERT INTO ")
		       .append(tableName)
		       .append(" (");

		    for (int i = 0; i < fields.length; i++) {
		        sql.append(fields[i]);
		        if (i < fields.length - 1) {
		            sql.append(", ");
		        }
		    }
		    
		    sql.append(") VALUES (");

		    for (int i = 0; i < params.length; i++) {
		        sql.append("'").append(params[i]).append("'");
		        if (i < params.length - 1) {
		            sql.append(", ");
		        }
		    }

		    sql.append(")");

		    executeUpdate(sql.toString(), "Данные успешно добавлены в таблицу.");
		}
}
