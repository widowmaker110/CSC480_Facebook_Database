package facebook.database.main;

import java.util.ArrayList;

import facebook.database.dao.DatabaseManager;
import facebook.database.model.*;

/**
 * Simple client that retrieves data from an already created database.
 * Running this after Test will check that the same data may be retrieved
 * from the database and not just from the in-memory cache.
 * 
 * @version 4/7/2015
 * @author Alexander Miller, Congshu Wang
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DatabaseManager dbm = new DatabaseManager();

		ArrayList<User> array = dbm.getAllUsers();
		
		for(int i = 0; i < array.size(); i++)
		{
			System.out.println("-------------USER-------------");
			System.out.println("id: " + array.get(i).getUserId());
			System.out.println("name: " + array.get(i).getUserName());
			System.out.println("password: " + array.get(i).getPassword());
			System.out.println("email: " + array.get(i).getEmail());
			System.out.println("");
		}
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}
}