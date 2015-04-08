package facebook.database.model;

import java.util.Date;

import facebook.database.dao.CommentDAO;;

public class Comment 
{
	private CommentDAO dao;
	private int commentId;
	private int userId;
	private int postId;
	private Date commentDate;
	private String commentContext;
	//private User user;
	//private Post post;
	
	/**
	 * 
	 * @param dao
	 * @param commentId
	 * @param commentDate
	 * @param commentContext
	 * @param user
	 * @param post
	 */
	public Comment(/*CommentDAO dao,*/ int commentId, int userId, int postId, Date commentDate, String commentContext /*, User user, Post post*/)
	{
		//this.dao = dao;
		this.commentId = commentId;
		this.userId = userId;
		this.postId = postId;
		this.commentDate = commentDate;
		this.commentContext = commentContext;
		//this.user = user;
		//this.post = post;
	}
	
	public int getCommentId()
	{
		return commentId;
	}
	
	public Date getCommentDate()
	{
		return commentDate;
	}
	
	public String getCommentContext()
	{
		return commentContext;
	}
	
	public void changeCommentContext(String newCommentContext)
	{
		int theCommentId = this.commentId;
		dao.changeCommentContext(theCommentId, newCommentContext);
	}
	
	public int getUserId()
	{
		return userId;
	}
	
	public int getPostId()
	{
		return postId;
	}
	
	/*public User getUser()
	{
		return user;
	}
	
	public Post getPost()
	{
		return post;
	}*/
}
