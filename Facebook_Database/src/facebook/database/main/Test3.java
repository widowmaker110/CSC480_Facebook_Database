package facebook.database.main;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import facebook.database.dao.DatabaseManager;
import facebook.database.model.Comment;
import facebook.database.model.Friend;
import facebook.database.model.Like;
import facebook.database.model.Post;
import facebook.database.model.User;

/**
 * This class is a little something extra Congshu and Alex thought 
 * about implementing. This program will run in the console until 
 * the user enters 'Quit'. It takes a series of commands and interacts
 * with the database in real time. Every 15 second, all of the user's 
 * changes will be committed to the database via multithreading if there
 * is any new information to be committed.
 * 
 * @version 4/9/2015
 * @author Alexander Miller, Congshu Wang
 */
public class Test3 extends TimerTask {
	
	private static Scanner scanner;
	private static String command, help;
	private static ArrayList<User> users;
	private static ArrayList<Friend> friends;
	private static ArrayList<Like> likes;
	private static ArrayList<Post> posts;
	private static ArrayList<Comment> comments;
	private static DatabaseManager dbm;
	private boolean changed;
	
	public static void main(String[] args) 
	{
		// set up the variables
		initialize();
		scanner = new Scanner(System.in);
		command = "";
		help = "------- COMMANDS -------\n"
			+ "1. Quit - stops the program\n"
			+ "2. Help - lists all of the commands\n"
			+ "3. Insert - gives you a list of options for inserting objects into the database\n"
			+ "4. Find - gives you a list of tables to search in and then asks for an existing ID number\n";
		
		
		System.out.println("Please wait patiently while the database loads");
		
		dbm = new DatabaseManager();
		dbm.clearTables();
		
		System.out.println("Database loaded. Please enter your command. Enter 'help' for possible commands");
        
		initiateTimer();
		
		while(!command.equalsIgnoreCase("quit"))
		{
			System.out.print(">>>");
			command = scanner.next();
			if(command.trim().equalsIgnoreCase("quit"))
			{
				System.out.println("Closing");
				dbm.commit();
				dbm.close();
				System.out.println("Bye.");
				break;
			}
			else if(command.trim().equalsIgnoreCase("help"))
			{
				System.out.println(help);
			}
			else if(command.trim().contains("insert"))
			{
				System.out.println("Choose which table to insert into: \n"
						+ "(1) Users\n"
						+ "(2) Friend\n"
						+ "(3) Like\n"
						+ "(4) Post\n"
						+ "(5) Comment\n"
						+ "(6) Leave this command");
				System.out.print(">>>");
				int choice = scanner.nextInt();
				switch(choice)
				{
					default:
					case 1:
						// users
						// TODO
						User user;
						System.out.println("Give the proper information");
						
						System.out.print("new unique ID number: ");
						int id = scanner.nextInt();
						
						System.out.print("new username: ");
						String username = scanner.next();
						
						System.out.print("new password: ");
						String password = scanner.next();
						
						System.out.print("new email: ");
						String email = scanner.next();
						
						user = new User(id, username, password, email);
						users.add(user);
						break;
						
					case 2:
						// friend
						// TODO
						Friend friend;
						System.out.println("Give the proper information");
						
						System.out.print("ID number of friend1: ");
						int friend1Id = scanner.nextInt();
						
						System.out.print("ID number of friend2: ");
						int friend2Id = scanner.nextInt();
						
						// skipping the whole date thing
						Date date = getCurrentDate();
												
						System.out.print("FriendRequestPending? (true or false): ");
						boolean pending = scanner.nextBoolean();
						
						System.out.print("FriendRequestCancelled? (true or false): ");
						boolean cancelled = scanner.nextBoolean();
						
						System.out.print("FriendRequestComplete? (true or false): ");
						boolean complete = scanner.nextBoolean();
						
						friend = new Friend(friend1Id, friend2Id, date, pending, cancelled, complete);
						friends.add(friend);
						break;
					case 3:
						// like
						// TODO
						Like like;
						System.out.println("Give the proper information");
						
						System.out.print("ID number of like: ");
						int likeId = scanner.nextInt();
						
						System.out.print("ID number of user: ");
						int userId = scanner.nextInt();
						
						System.out.print("ID number of post: ");
						int postId = scanner.nextInt();
						
						// skipping the whole date thing
						Date likedate = getCurrentDate();
											
						like = new Like(likeId, userId, postId, likedate);
						likes.add(like);
						break;
					case 4:
						// post
						// TODO
						Post post;
						System.out.println("Give the proper information");
						
						System.out.print("ID number of post: ");
						int POSTpostId = scanner.nextInt();
						
						System.out.print("ID number of user: ");
						int POSTuserId = scanner.nextInt();
						
						System.out.print("postText: ");
						String postText = scanner.next();
						
						// skipping the whole date thing
						Date postDate = getCurrentDate();
						
						System.out.print("PostImageURL: ");
						String postImageURL = scanner.next();
						
						System.out.print("PostVideoURL: ");
						String postVideURL = scanner.next();
											
						post = new Post(POSTpostId, POSTuserId, postDate, postText, postImageURL, postVideURL);
						posts.add(post);
						break;
					case 5:
						// comment
						// TODO
						Comment comment;
						System.out.println("Give the proper information");
						
						System.out.print("ID number of comment: ");
						int commentId = scanner.nextInt();
						
						System.out.print("ID number of user: ");
						int CommentuserId = scanner.nextInt();
						
						System.out.print("ID number of post: ");
						int commentPostId = scanner.nextInt();
						
						// skipping the whole date thing
						Date commentDate = getCurrentDate();
						
						System.out.print("Comment Text: ");
						String commentText = scanner.next();
											
						comment = new Comment(commentId, CommentuserId, commentPostId, commentDate, commentText);
						comments.add(comment);
						break;
					case 6:
						// back out
						continue;		
				}
			}
			else if(command.trim().equalsIgnoreCase("find"))
			{
				System.out.println("Choose which table to find: \n"
						+ "(1) Users\n"
						+ "(2) Friend\n"
						+ "(3) Like\n"
						+ "(4) Post\n"
						+ "(5) Comment\n"
						+ "(6) Leave this command");
				System.out.print(">>>");
				int choice = scanner.nextInt();
				switch(choice)
				{
					default:
					case 1:
						// TODO users
						User user;
						System.out.print("UserId: ");
						int userId = scanner.nextInt();
						user = dbm.findUser(userId);
						if(user == null)
						{
							System.out.println("User not found");
						}
						else
						{
							System.out.println("USER:\n"
									+ "ID: " + user.getUserId() + "\n"
									+ "username: " + user.getUserName() + "\n"
									+ "password: " + user.getPassword() + "\n"
									+ "email: " + user.getEmail() + "\n");
						}
						break;
					case 2:
						// TODO friends
						Friend friend;
						System.out.print("friend1ID: ");
						int friend1ID = scanner.nextInt();
						System.out.print("friend2ID: ");
						int friend2ID = scanner.nextInt();
						friend = dbm.findFriend(friend1ID, friend2ID);
						if(friend == null)
						{
							System.out.println("Friend not found");
						}
						else
						{
							System.out.println("Friend:\n"
									+ "ID1: " + friend.getRequestSender() + "\n"
									+ "ID2: " + friend.getRequestRecipient() + "\n"
									+ "Friend since: " + friend.getFriendSince() + "\n"
									+ "Friendship status: " + friend.getFriendRequestStatus() + "\n");
						}
						break;
					case 3:
						// TODO likes
						Like like;
						System.out.print("like ID: ");
						int likeId = scanner.nextInt();
						like = dbm.findLike(likeId);
						if(like == null)
						{
							System.out.println("Like not found");
						}
						else
						{
							System.out.println("Like:\n"
									+ "Like ID: " + like.getLikeId() + "\n"
									+ "Post ID: " + like.getPostId() + "\n"
									+ "User ID: " + like.getUserId() + "\n"
									+ "Like date: " + like.getLikeDate() + "\n");
						}
						break;
					case 4:
						// TODO posts
						Post post;
						System.out.print("Post ID: ");
						int postId = scanner.nextInt();
						System.out.print("User ID: ");
						int postUserId = scanner.nextInt();
						post = dbm.findPost(postId, postUserId);
						if(post == null)
						{
							System.out.println("Post not found");
						}
						else
						{
							System.out.println("Like:\n"
									+ "ID: " + post.getPostId() + "\n"
									+ "User ID: " + post.getUserId() + "\n"
									+ "Post date: " + post.getPostDate() + "\n"
									+ "Post Image: " + post.getPostImage() + "\n"
									+ "Post Video: " + post.getPostVideo() + "\n");
						}
						break;
					case 5:
						// TODO comments
						Comment comment;
						System.out.print("comment ID: ");
						int commentId = scanner.nextInt();
						System.out.print("user ID: ");
						int commentUserId = scanner.nextInt();
						comment = dbm.findComment(commentId, commentUserId);
						if(comment == null)
						{
							System.out.println("Comment not found");
						}
						else
						{
							System.out.println("Comment:\n"
									+ "ID: " + comment.getCommentId() + "\n"
									+ "Post ID: " + comment.getPostId() + "\n"
									+ "User ID: " + comment.getUserId() + "\n"
									+ "Comment Text: " + comment.getCommentContext() + "\n"
									+ "Comment Date: " + comment.getCommentDate()+ "\n");
						}
						break;
					case 6:
						// back out
						continue;
				}
			}
			else
			{
				System.out.println("Sorry, I did not understand that command. Enter 'help' for possible commands without the qoutation marks.");
			}
		}
	}
	
	// http://alvinalexander.com/java/java-current-date-example-now
	private static java.sql.Date getCurrentDate()
	{
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentDate = calendar.getTime();
		java.sql.Date date = new java.sql.Date(currentDate.getTime());
		return date;
	}
	
	private static void initialize()
	{
		users = new ArrayList<User>();
		
		friends = new ArrayList<Friend>();
		likes = new ArrayList<Like>();
		posts = new ArrayList<Post>();
		comments = new ArrayList<Comment>();
	}
	
	private static void initiateTimer()
	{
		TimerTask timerTask = new Test3();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10 * 1000);
	}

	@Override
	public void run()
	{
		try 
		{	
			// wait 25 seconds
			Thread.sleep(1 * 25 * 1000);		
		} 
		catch (InterruptedException e) 
		{
		     e.printStackTrace();
		}
		
		changed = false;
		
		// commit any changes
		if(users.size() > 0)
		{
			updateUsers();
			users.clear();
			changed = true;
		}
		if(friends.size() > 0)
		{
			updateFriends();
			friends.clear();
			changed = true;
		}
		if(likes.size() > 0)
		{
			updateLike();
			likes.clear();
			changed = true;
		}
		if(posts.size() > 0)
		{
			updatePost();
			posts.clear();
			changed = true;
		}
		if(comments.size() > 0)
		{
			updateComment();
			comments.clear();
			changed = true;
		}
		
		// only print this if there was anything new to the database
		if(changed)
		{
			System.out.println("Routine update committed");
		}
	}	
	
	//----------------------------------------------------------
	// background thread updating database
	private void updateUsers()
	{
		for(int i = 0; i < users.size(); i++)
		{
			dbm.insertUser(users.get(i).getUserId(), users.get(i).getUserName(), users.get(i).getPassword(), users.get(i).getEmail());
		}
	}
	
	private void updateFriends()
	{
		for(int i = 0; i < friends.size(); i++)
		{
			dbm.insertFriend(friends.get(i).getRequestSender(), friends.get(i).getRequestRecipient(), friends.get(i).getFriendSince(), 
					friends.get(i).isFriendRequestPending(), friends.get(i).isFriendRequestCancelled(), friends.get(i).isFriendRequestComplete());
		}
	}
	
	private void updateLike()
	{
		for(int i = 0; i < likes.size(); i++)
		{
			dbm.insertLike(likes.get(i).getLikeId(), likes.get(i).getUserId(), likes.get(i).getPostId(), likes.get(i).getLikeDate());
		}
	}
	
	private void updatePost()
	{
		for(int i = 0; i < posts.size(); i++)
		{
			dbm.insertPost(posts.get(i).getPostId(), posts.get(i).getUserId(), posts.get(i).getPostDate(), posts.get(i).getPostText(), 
					posts.get(i).getPostImage(), posts.get(i).getPostVideo());
		}
	}
	
	private void updateComment()
	{
		for(int i = 0; i < comments.size(); i++)
		{
			dbm.insertComment(comments.get(i).getCommentId(), comments.get(i).getUserId(), comments.get(i).getPostId(), comments.get(i).getCommentDate(),
					comments.get(i).getCommentContext());
		}
	}
}