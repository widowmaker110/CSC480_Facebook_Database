package facebook.database.model;

import java.util.Date;

import facebook.database.dao.LikeDAO;;

public class Like 
{
	private LikeDAO dao;
	private int likeId;
	private Date likeDate;
	private User user;
	private Post post;
	
	/**
	 * 
	 * @param dao
	 * @param likeId
	 * @param likeDate
	 * @param user
	 * @param post
	 */
	public Like(LikeDAO dao, int likeId, Date likeDate, User user, Post post)
	{
		this.dao = dao;
		this.likeId = likeId;
		this.likeDate = likeDate;
		this.user = user;
		this.post = post;
	}
	
	public int getLikeId()
	{
		return likeId;
	}
	
	public Date getLikeDate()
	{
		return likeDate;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public Post getPost()
	{
		return post;
	}
}
