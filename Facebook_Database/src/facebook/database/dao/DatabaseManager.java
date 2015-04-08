package facebook.database.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDriver;

import facebook.database.model.*;

public class DatabaseManager 
{
	private Driver driver;
	private Connection conn;
	@SuppressWarnings("unused")
	private CommentDAO commentDao;
	@SuppressWarnings("unused")
	private FriendDAO friendDao;
	@SuppressWarnings("unused")
	private LikeDAO likeDao;
	@SuppressWarnings("unused")
	private PostDAO postDao;
	@SuppressWarnings("unused")
	private UserDAO userDao;
	
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
		try 
		{
			conn = driver.connect(url, prop);
			conn.setAutoCommit(false);
		}
		catch(SQLException e) 
		{
			// database doesn't exist, so try creating it
			try 
			{
				prop.put("create", "true");
				conn = driver.connect(url, prop);
				conn.setAutoCommit(false);
				create(conn);
			}
			catch (SQLException e2) 
			{
				throw new RuntimeException("cannot connect to database", e2);
			}
		}
		
		commentDao = new CommentDAO(conn, this);
		friendDao = new FriendDAO(conn, this);
		likeDao = new LikeDAO(conn, this);
		postDao = new PostDAO(conn, this);
		userDao = new UserDAO(conn, this);
	}
	
	/**
	 * Initialize the tables and their constraints in a newly created database
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private void create(Connection conn) throws SQLException 
	{		
		// CREATE
		FriendDAO.create(conn);
		LikeDAO.create(conn);
		PostDAO.create(conn);
		UserDAO.create(conn);
				
		// CONSTRAINTS
		FriendDAO.addConstraints(conn);
		LikeDAO.addConstraints(conn);
		PostDAO.addConstraints(conn);
		
		conn.commit();
	}
	
	//***************************************************************
	// Data retrieval functions -- find a model object given its key
		
	public User findUser(int userId) 
	{
		return UserDAO.find(userId);
	}

	public Friend findFriend(int user1Id, int user2Id) 
	{
		return FriendDAO.find(user1Id, user2Id);
	}

	public Like findLike(int likeId) 
	{
		return LikeDAO.find(likeId);
	}
		
	public Post findPost(int postId, int userId) 
	{
		return PostDAO.find(postId, userId);
	}
	
	public Comment findComment(int commentId, int userId)
	{
		return CommentDAO.find(commentId);
	}
	
	// TODO implement methods to find objects by other attributes other than
	// via unique identifier (e.g. findPost by date)

	//***************************************************************
	// Data insertion functions -- create new model object from attributes
		
	public User insertUser(int userId, String username, String password, String email) 
	{
		return UserDAO.insert(userId, username, password, email);
	}
	
	public Comment insertComment(int commentId, int userId, int postId, Date date, String commentContext) 
	{
		return CommentDAO.insert(userId, userId, postId, date, commentContext);
	}

	public Friend insertFriend(int friend1, int friend2, Date date, boolean friendRequestPending, boolean friendRequestCancelled, boolean friendRequestComplete)
	{
		return FriendDAO.insert(friend1, friend2, date, friendRequestPending, friendRequestCancelled, friendRequestComplete);
	}
	
	public Like insertLike(int likeId, int userId, int postId, Date date)
	{
		return LikeDAO.insert(likeId, userId, postId, date);
	}
	
	public Post insertPost(int postId, int userId, Date date, String postText, String postImage, String postVideo)
	{
		return PostDAO.insert(postId, userId, date, postText, postImage, postVideo);
	}
	
	//***************************************************************
	// <Utility functions>
		
		/**
		 * Commit changes since last call to commit
		 */
		public void commit() 
		{
			try 
			{
				conn.commit();
			}
			catch(SQLException e) 
			{
				throw new RuntimeException("cannot commit database", e);
			}
		}

		/**
		 * Abort changes since last call to commit, then close connection
		 */
		public void cleanup() 
		{
			try 
			{
				conn.rollback();
				conn.close();
			}
			catch(SQLException e) 
			{
				System.out.println("fatal error: cannot cleanup connection");
			}
		}

		/**
		 * Close connection and shutdown database
		 */
		public void close() 
		{
			try 
			{
				conn.close();
			}
			catch(SQLException e) 
			{
				throw new RuntimeException("cannot close database connection", e);
			}
			
			// Now shutdown the embedded database system -- this is Derby-specific
			try 
			{
				Properties prop = new Properties();
				prop.put("shutdown", "true");
				conn = driver.connect(url, prop);
			} 
			catch (SQLException e) 
			{
				// This is supposed to throw an exception...
				System.out.println("Derby has shut down successfully");
			}
		}

		/**
		 * Clear out all data from database (but leave empty tables)
		 */
		public void clearTables() 
		{
			try 
			{
				// This is not as straightforward as it may seem, because
				// of the cyclic foreign keys -- I had to play with
				// "on delete set null" and "on delete cascade" for a bit
				
				FriendDAO.clear();
				LikeDAO.clear();
				PostDAO.clear();
				UserDAO.clear();
			} 
			catch (SQLException e) 
			{
				throw new RuntimeException("cannot clear tables", e);
			}
		}
		
		// </Utility functions>
		//***************************************************************
}