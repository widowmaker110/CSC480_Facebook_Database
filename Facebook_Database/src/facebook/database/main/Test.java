package facebook.database.main;

import java.util.ArrayList;
import java.util.Calendar;

import facebook.database.dao.DatabaseManager;
import facebook.database.model.*;

/**
 * Simple client that inserts sample data then runs a query.
 * 
 * @version 4/7/2015
 * @author Alexander Miller, Congshu Wang
 */
public class Test { 

	// http://alvinalexander.com/java/java-current-date-example-now
	private static java.sql.Date getCurrentDate()
	{
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentDate = calendar.getTime();
		java.sql.Date date = new java.sql.Date(currentDate.getTime());
		return date;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		DatabaseManager dbm = new DatabaseManager();
		
		dbm.clearTables();
		
		// 1. create some dummy variables such as 4 users with a post each.
		// USERS
		User user1 = new User(1, "alex", "password", "alex@gmail.com");
		User user2 = new User(2, "Congshu", "password", "congshu@gmail.com");
		User user3 = new User(3, "Mark", "coolPassword", "facebook@gmail.com");
		User user4 = new User(4, "Stephen", "myPassword", "cambridge@gmail.com");
		
		// POST
		Post post1 = new Post(1, user1.getUserId(), getCurrentDate(), "This post is totally cool", "", "");
		Post post2 = new Post(2, user3.getUserId(), getCurrentDate(), "I should stop coding so much", "", "");
		Post post3 = new Post(3, user4.getUserId(), getCurrentDate(), "Did you hear about this new song?", "", "");
		
		System.out.println(getCurrentDate());
		// COMMENTS
		Comment comment1 = new Comment(1, user1.getUserId(), post1.getPostId(), getCurrentDate(), "Go home Alex, you're drunk");
		Comment comment2 = new Comment(2, user3.getUserId(), post2.getPostId(), getCurrentDate(), "Come over to my house then");
		Comment comment3 = new Comment(3, user2.getUserId(), post3.getPostId(), getCurrentDate(), "weeeeee");
		
		// LIKES
		//int likeId, int userId, int postId, Date likeDate
		Like like1 = new Like(1, user1.getUserId(), post1.getPostId(), getCurrentDate());
		Like like2 = new Like(2, user4.getUserId(), post1.getPostId(), getCurrentDate());
		
		// FRIENDS
		Friend oneThree = new Friend(user1.getUserId(), user3.getUserId(), null, true, false, false);
		Friend twoFour = new Friend(user1.getUserId(), user4.getUserId(), getCurrentDate(), false, false, true);
		
		// 2. insert the data into the appropriate tables
		
		// insert users
		dbm.insertUser(user1.getUserId(), user1.getUserName(), user1.getPassword(), user1.getEmail());
		dbm.insertUser(user2.getUserId(), user2.getUserName(), user2.getPassword(), user2.getEmail());
		dbm.insertUser(user3.getUserId(), user3.getUserName(), user3.getPassword(), user3.getEmail());
		dbm.insertUser(user4.getUserId(), user4.getUserName(), user4.getPassword(), user4.getEmail());
		
		// insert posts
		dbm.insertPost(post1.getPostId(), post1.getUserId(), post1.getPostDate(), post1.getPostText(), post1.getPostImage(), post1.getPostVideo());
		dbm.insertPost(post2.getPostId(), post2.getUserId(), post2.getPostDate(), post2.getPostText(), post2.getPostImage(), post2.getPostVideo());
		dbm.insertPost(post3.getPostId(), post3.getUserId(), post3.getPostDate(), post3.getPostText(), post3.getPostImage(), post3.getPostVideo());
		
		// insert comments
		dbm.insertComment(comment1.getCommentId(), comment1.getUserId(), comment1.getPostId(), comment1.getCommentDate(), comment1.getCommentContext());
		dbm.insertComment(comment2.getCommentId(), comment2.getUserId(), comment2.getPostId(), comment2.getCommentDate(), comment2.getCommentContext());
		dbm.insertComment(comment3.getCommentId(), comment3.getUserId(), comment3.getPostId(), comment3.getCommentDate(), comment3.getCommentContext());
		
		// insert likes
		dbm.insertLike(like1.getLikeId(), like1.getUserId(), like1.getPostId(), like1.getLikeDate());
		dbm.insertLike(like2.getLikeId(), like2.getUserId(), like2.getPostId(), like2.getLikeDate());
		
		// insert friends
		dbm.insertFriend(oneThree.getRequestSender(), oneThree.getRequestRecipient(), oneThree.getFriendSince(), oneThree.isFriendRequestPending(), oneThree.isFriendRequestCancelled(), oneThree.isFriendRequestComplete());
		dbm.insertFriend(twoFour.getRequestSender(), twoFour.getRequestRecipient(), twoFour.getFriendSince(), twoFour.isFriendRequestPending(), twoFour.isFriendRequestCancelled(), twoFour.isFriendRequestComplete());
		
		// 3. commit the changes
		dbm.commit();
		
		//retrieve

		ArrayList<User> userArray = dbm.getAllUsers();
		
		for(int i = 0; i < userArray.size(); i++)
		{
			System.out.println("-------------USER-------------");
			System.out.println("id: " + userArray.get(i).getUserId());
			System.out.println("name: " + userArray.get(i).getUserName());
			System.out.println("password: " + userArray.get(i).getPassword());
			System.out.println("email: " + userArray.get(i).getEmail());
			System.out.println("");
		}
		
		ArrayList<Comment> commentArray = dbm.getAllComments();
		
		for(int i = 0; i < commentArray.size(); i++)
		{
			System.out.println("-------------COMMENT-------------");
			System.out.println("commentId: " + commentArray.get(i).getCommentId());
			System.out.println("userId: " + commentArray.get(i).getUserId());
			System.out.println("postId: " + commentArray.get(i).getPostId());
			System.out.println("commentDate: " + commentArray.get(i).getCommentDate());
			System.out.println("commentText: " + commentArray.get(i).getCommentContext());
			System.out.println("");
		}
		
		ArrayList<Post> postArray = dbm.getAllPosts();
		
		for(int i = 0; i < postArray.size(); i++)
		{
			System.out.println("-------------POST-------------");
			System.out.println("postId: " + postArray.get(i).getPostId());
			System.out.println("userId: " + postArray.get(i).getUserId());
			System.out.println("postDate: " + postArray.get(i).getPostDate());
			System.out.println("postText: " + postArray.get(i).getPostText());
			System.out.println("");
		}
		
		ArrayList<Like> likeArray = dbm.getAllLikes();
		
		for(int i = 0; i < likeArray.size(); i++)
		{
			System.out.println("-------------LIKE-------------");
			System.out.println("likeId: " + likeArray.get(i).getLikeId());
			System.out.println("userId: " + likeArray.get(i).getUserId());
			System.out.println("postId: " + likeArray.get(i).getPostId());
			System.out.println("likeDate: " + likeArray.get(i).getLikeDate());
			System.out.println("");
		}
		
		// 5. commit again
		dbm.commit();
		
		// close the connection
		dbm.close();
		
		System.out.println("Done");
	}
}