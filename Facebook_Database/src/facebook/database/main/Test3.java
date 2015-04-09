package facebook.database.main;

import java.util.Scanner;

import facebook.database.dao.DatabaseManager;

/**
 * This class is a little something extra Congshu and Alex thought 
 * about implementing. This program will run in the console until 
 * the user enters 'Quit'. It takes a series of commands and interacts
 * with the database in real time. Every 15 second, all of the user's 
 * changes will be commited to the database via multithreading.
 * 
 * @version 4/9/2015
 * @author Alexander Miller, Congshu Wang
 */
public class Test3 {
	
	private static Scanner scanner;
	private static String command, help;
	
	public static void main(String[] args)
	{
		scanner = new Scanner(System.in);
		command = "";
		help = "------- COMMANDS -------\n"
			+ "1. Quit - stops the program\n"
			+ "2. Help - lists all of the commands\n"
			+ "3. Insert - gives you a list of options for inserting objects into the database\n"
			+ "4. Find - gives you a list of tables to search in and then asks for an existing ID number\n"
			+ "5. Remove - allows you to remove an object from the database with its given ID number\n"
			+ "6. Update - allows you to update an object from the database with its given ID number\n";
		
		DatabaseManager dbm = new DatabaseManager();

		while(!command.equalsIgnoreCase("quit"))
		{
			command = scanner.next();
			if(command.equalsIgnoreCase("quit"))
			{
				System.out.println("Closing");
				dbm.close();
				System.out.println("Bye");
			}
			else if(command.equalsIgnoreCase("help"))
			{
				System.out.println(help);
			}
		}
	}
}