package facebook.database.main;

import java.util.Scanner;

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
	private static String command;
	
	public static void main(String[] args)
	{
		scanner = new Scanner(System.in);
		command = "";
		
		while(!command.equalsIgnoreCase("quit"))
		{
			
		}
	}
}