package facebook.database.model;

import facebook.database.dao.UserDAO;

public class User 
{
	private UserDAO dao;
	private int userId;
	private String userName;
	private String password;
	private String email;
	
	/**
	 * 
	 * @param dao
	 * @param userId
	 * @param userName
	 * @param password
	 * @param email
	 */
	public User(int userId, String userName, String password, String email)
	{
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.email = email;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void changeUserName(String newUserName)
	{
		userName = newUserName;
		//dao.changeUserName(...);
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void changePassword(String newPassword)
	{
		password = newPassword;
		//dao.changePassword(...);
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void changeEmail(String newEmail)
	{
		email = newEmail;
		dao.changeEmail(userId, email);
	}
}
