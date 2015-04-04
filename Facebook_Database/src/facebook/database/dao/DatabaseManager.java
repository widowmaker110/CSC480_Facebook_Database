package facebook.database.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import facebook.database.model.*;

import org.apache.derby.jdbc.EmbeddedDriver;

public class DatabaseManager 
{
	private Driver driver;
	private Connection conn;
	private FriendDAO friendDao;
	
	private final String url = "jdbc:derby:collegedb";
	
	/**
	 * Constructor
	 */
	public DatabaseManager()
	{
		driver = new EmbeddedDriver();
		
		Properties prop = new Properties();
		prop.put("create", "false");
		
		// try to connect to an existing database
		try {
			conn = driver.connect(url, prop);
			conn.setAutoCommit(false);
		}
		catch(SQLException e) {
			// database doesn't exist, so try creating it
			try {
				prop.put("create", "true");
				conn = driver.connect(url, prop);
				conn.setAutoCommit(false);
				create(conn);
			}
			catch (SQLException e2) {
				throw new RuntimeException("cannot connect to database", e2);
			}
		}
		
		//TODO need to change these to the appropriate DAO's.
//		facultyDAO = new FacultyDAO(conn, this);
//		departmentDAO = new DepartmentDAO(conn, this);
//		courseDAO = new CourseDAO(conn, this);
	}
	
	/**
	 * Initialize the tables and their constraints in a newly created database
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void create(Connection conn) throws SQLException 
	{
		//TODO need to change these to the appropriate DAO's.
//		FacultyDAO.create(conn);
//		DepartmentDAO.create(conn);
//		CourseDAO.create(conn);
//		FacultyDAO.addConstraints(conn);
//		DepartmentDAO.addConstraints(conn);
//		CourseDAO.addConstraints(conn);
		conn.commit();
	}
}
