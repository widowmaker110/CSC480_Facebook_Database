package facebook.database.main;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import facebook.database.dao.DatabaseManager;
import facebook.database.model.User;

/**
 * This class is a little something extra Congshu and Alex thought 
 * about implementing. This program will run in the console until 
 * the user enters 'Quit'. It takes a series of commands and interacts
 * with the database in real time. Every 15 second, all of the user's 
 * changes will be committed to the database via multithreading.
 * 
 * @version 4/9/2015
 * @author Alexander Miller, Congshu Wang
 */
public class Test3 extends TimerTask {
	
	private static Scanner scanner;
	private static String command, help;
	private static ArrayList<User> users;
	private static DatabaseManager dbm;
	
	public static void main(String[] args) 
	{
		users = new ArrayList<User>();
		scanner = new Scanner(System.in);
		command = "";
		help = "------- COMMANDS -------\n"
			+ "1. Quit - stops the program\n"
			+ "2. Help - lists all of the commands\n"
			+ "3. Insert - gives you a list of options for inserting objects into the database\n"
			+ "4. Find - gives you a list of tables to search in and then asks for an existing ID number\n"
			+ "5. Remove - allows you to remove an object from the database with its given ID number\n"
			+ "6. Update - allows you to update an object from the database with its given ID number\n";
		
		System.out.println("Please wait patiently while the database loads");
		
		dbm = new DatabaseManager();
		dbm.clearTables();
		
		System.out.println("Database loaded. Please enter your command. Enter 'help' for possible commands");
        
		
		initiateTimer();
		
		
		while(!command.equalsIgnoreCase("quit"))
		{
			System.out.print(">>>");
			command = scanner.next();
			if(command.equalsIgnoreCase("quit"))
			{
				System.out.println("Closing");
				dbm.commit();
				dbm.close();
				System.out.println("Bye.");
				break;
			}
			else if(command.equalsIgnoreCase("help"))
			{
				System.out.println(help);
			}
			else if(command.contains("insert"))
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
						break;
					case 3:
						// like
						break;
					case 4:
						// post
						break;
					case 5:
						// comment
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
		
		// commit any changes
		if(users.size() > 0)
		{
			updateUsers();
			users.clear();
		}
		updateFriends();
		updateLike();
		updatePost();
		updateComment();
		
		System.out.println("Routine update committed");
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
		
	}
	
	private void updateLike()
	{
		
	}
	
	private void updatePost()
	{
		
	}
	
	private void updateComment()
	{
		
	}
}