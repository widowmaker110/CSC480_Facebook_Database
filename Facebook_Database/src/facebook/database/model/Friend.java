package facebook.database.model;

import java.util.Date;//for date type

import java.util.Collection;

import facebook.database.dao.FriendDAO;

public class Friend
{
	private FriendDAO dao;
	private int friend1; //friend request sender
	private int friend2; //friend request recipient
	private Date friendSince;
	private boolean friendRequestPending;
	private boolean friendRequestCancelled;
	private boolean friendRequestComplete;
	private User user;
	private String status;
	
	public Friend(int friend1, int friend2, Date friendSince, boolean friendRequestPending, boolean friendRequestCancelled, boolean friendRequestComplete, User user)
	{
		this.friend1 = friend1;
		this.friend2 = friend2;
		this.friendSince = friendSince;
		this.friendRequestPending = friendRequestPending;
		this.friendRequestCancelled = friendRequestCancelled;
		this.friendRequestComplete = friendRequestComplete;
		this.user = user;
	}
	
	public int getRequestSender()
	{
		return friend1;
	}
	
	public int getRequestRecipient()
	{
		return friend2;
	}
	
	public Date getFriendSince()
	{
		return friendSince;
	}
	
	public String getFriendRequestStatus()
	{
		if(friendRequestPending == true)
			status = "Friend Request Pending.";
		else if(friendRequestCancelled == true)
			status = "Friend Request Cancelled.";
		else if(friendRequestComplete = true)
			status = "Friend Request Complete.";
		else
			status = "Error Status!";
		return status;
	}
	
	public void changeFriendRequestStatus(String newStatus)
	{
		status = newStatus;
		//dao.changeFriendRequestStatus(...);
	}
}
