package facebook.database.main;

import java.util.Collection;

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
		
		// TODO
//		Department mathcs = dbm.findDepartmentByName("MathCS");
//		
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
		
		dbm.commit();
		
		dbm.close();
		
		System.out.println("Done");
	}

}