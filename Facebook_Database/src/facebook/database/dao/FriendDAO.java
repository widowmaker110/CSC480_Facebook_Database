package facebook.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import facebook.database.model.*;

/**
 * Data Access Object for the Friend table.
 * Encapsulates all of the relevant SQL commands.
 * 
 * @version 4/4/2015
 * @author Alexander Miller, Congshu Wang
 */
public class FriendDAO 
{
	
	private Connection conn;
	private DatabaseManager dbm;
	//private Map<DeptNumPair, Course> cache;

	public FriendDAO(Connection conn, DatabaseManager dbm) 
	{
		this.conn = conn;
		this.dbm = dbm;
		//this.cache = new HashMap<DeptNumPair, Course>();
	}
	
	/**
	 * Create the Course table via SQL
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void create(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		String s = "create table Friend("
				+ "friend1 int not null, "
				+ "friend2 int not null, "
				+ "friendSince date, "
				+ "friendRequestPending boolean not null, "
				+ "friendRequestCaneled boolean not null, "
				+ "friendRequestComplete boolean not null, "
				
				+ "primary key(friend1, friend2))";
		
		//TODO do we need the check(friend1 > 0), ?
		
		stmt.executeUpdate(s);
	}
	
	/**
	 * Modify the Friend table to add foreign key constraints
	 * (needs to happen after the other tables have been created)
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	static void addConstraints(Connection conn) throws SQLException 
	{
		Statement stmt = conn.createStatement();
		
		String s = "alter table COURSE add constraint fk_coursefac "
				+ "foreign key(FacSSN) references FACULTY on delete cascade";
		
		stmt.executeUpdate(s);
		
		s = "alter table COURSE add constraint fk_coursedept "
				+ "foreign key(Dept) references DEPARTMENT on delete cascade";
		stmt.executeUpdate(s);
	}
}