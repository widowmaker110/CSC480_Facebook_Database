package facebook.database.model;

import java.util.Date;

import facebook.database.dao.PostDAO;;;

public class Post 
{
	@SuppressWarnings("unused")
	private PostDAO dao;
	private int postId;
	private int userId;
	private Date postDate;
	private String postText;
	private String postImage; // an url to the image
	private String postVideo; // an url to the video
	
	/**
	 * 
	 * @param dao
	 * @param postId
	 * @param postDate
	 * @param postText
	 * @param postImage
	 * @param postVideo
	 */
	public Post(PostDAO dao, int postId, int userId, Date postDate, String postText, String postImage, String postVideo)
	{
		this.dao = dao;
		this.setUserId(userId);
		this.postId = postId;
		this.postDate = postDate;
		this.postText = postText;
		this.postImage = postImage;
		this.postVideo = postVideo;
	}
	
	public int getPostId()
	{
		return postId;
	}
	
	public Date getPostDate()
	{
		return postDate;
	}
	
	public String getPostText()
	{
		return postText;
	}
	
	public void changePostText(String newPostText)
	{
		postText = newPostText;
		//dao.changePostText(...);
	}
	
	public String getPostImage()
	{
		return postImage;
	}
	
	public void changePostImage(String newPostImage)
	{
		postImage = newPostImage;
		//dao.changePostImage(...);
	}
	
	public String getPostVideo()
	{
		return postVideo;
	}
	
	public void changePostVideo(String newPostVideo)
	{
		postImage = newPostVideo;
		//dao.changePostVideo(...);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
