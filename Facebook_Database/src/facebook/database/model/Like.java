package facebook.database.model;

import java.util.Date;

import facebook.database.dao.LikeDAO;;

public class Like 
{
	private LikeDAO dao;
	private int likeId;
	private int userId;
	private int postId;
	private Date likeDate;
	//private User user;
	//private Post post;
	
	/**
	 * 
	 * @param dao
	 * @param likeId
	 * @param likeDate
	 * @param user
	 * @param post
	 */
	public Like(LikeDAO dao, int likeId, int userId, int postId, Date likeDate)
	{
		this.dao = dao;
		this.likeId = likeId;
		this.likeDate = likeDate;
		this.userId = userId;
		this.postId = postId;
	}
	
	public int getLikeId()
	{
		return likeId;
	}
	
	public Date getLikeDate()
	{
		return likeDate;
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public int getPostId()
	{
		return postId;
	}
}
