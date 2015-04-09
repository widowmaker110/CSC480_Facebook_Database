package facebook.database.model;

import java.util.Date;//for date type

import facebook.database.dao.FriendDAO;

public class Friend
{
	@SuppressWarnings("unused")
	private FriendDAO dao;
	private int friend1; //friend request sender
	private int friend2; //friend request recipient
	private Date friendSince;
	private boolean friendRequestPending;
	private boolean friendRequestCancelled;
	private boolean friendRequestComplete;
	private String status;
	
	/**
	 * 
	 * @param friend1
	 * @param friend2
	 * @param friendSince
	 * @param friendRequestPending
	 * @param friendRequestCancelled
	 * @param friendRequestComplete
	 * @param user
	 */
	public Friend(int friend1, int friend2, Date friendSince, boolean friendRequestPending, boolean friendRequestCancelled, boolean friendRequestComplete)
	{
		this.friend1 = friend1;
		this.friend2 = friend2;
		this.friendSince = friendSince;
		this.setFriendRequestPending(friendRequestPending);
		this.setFriendRequestCancelled(friendRequestCancelled);
		this.setFriendRequestComplete(friendRequestComplete);
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
		if(isFriendRequestPending() == true)
			status = "Friend Request Pending.";
		else if(isFriendRequestCancelled() == true)
			status = "Friend Request Cancelled.";
		else if(setFriendRequestComplete(true))
			status = "Friend Request Complete.";
		else
			status = "Error!";
		return status;
	}
	
	public void changeFriendRequestStatus(String newStatus)
	{
		status = newStatus;
		//dao.changeFriendRequestStatus(...);
	}

	public boolean isFriendRequestPending() {
		return friendRequestPending;
	}

	public void setFriendRequestPending(boolean friendRequestPending) {
		this.friendRequestPending = friendRequestPending;
	}

	public boolean isFriendRequestCancelled() {
		return friendRequestCancelled;
	}

	public void setFriendRequestCancelled(boolean friendRequestCancelled) {
		this.friendRequestCancelled = friendRequestCancelled;
	}

	public boolean isFriendRequestComplete() {
		return friendRequestComplete;
	}

	public boolean setFriendRequestComplete(boolean friendRequestComplete) {
		this.friendRequestComplete = friendRequestComplete;
		return friendRequestComplete;
	}
}
