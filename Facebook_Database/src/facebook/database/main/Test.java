package facebook.database.main;

import java.util.Collection;

import facebook.database.dao.DatabaseManager;
import facebook.database.model.*;

/**
 * Simple client that inserts sample data then runs a query.
 * 
 * @author bhoward
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DatabaseManager dbm = new DatabaseManager();
		
		dbm.clearTables();
		
		// 1. create some dummy variables such as 4 users with a post each.
		// Department chem = dbm.insertDepartment(5, "Chemistry", null);

		// 2. insert the data into the appropriate tables
		// dbm.insertCourse(mathcs, 131, "Calculus", john);

		// 3. commit the changes
		dbm.commit();
		
		// 4. retrieve retrieve everything and make some changes
//		// Now retrieve a table of MathCS faculty and their courses;
//		// each course also lists the head of the department offering the course
//		Collection<Faculty> faculty = mathcs.getFaculty();
//		for (Faculty fac : faculty) {
//			System.out.println(fac);
//			Collection<Course> courses = fac.getCourses();
//			for (Course c : courses) {
//				System.out.println("  " + c + " [Head: " + c.getDept().getHead() + "]");
//			}
//		}
		
		// 5. commit again
		dbm.commit();
		
		// 6. make some changes again
		
		// 7. commit again
		dbm.commit();
		
		System.out.println("Made some changes");
	
		
		// close the connection
		dbm.close();
		
		System.out.println("Done");
	}
}