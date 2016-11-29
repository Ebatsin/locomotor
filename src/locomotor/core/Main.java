package locomotor.core;

import java.util.logging.*;
import locomotor.components.types.*;

public class Main {
	public static void main(String[] args) {

		// disable logging on mongodb driver
		// cf http://mongodb.github.io/mongo-java-driver/3.0/driver/reference/management/logging/
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE); 

		try {

			DBH db = DBH.getInstance();
			db.connect("localhost", 27017);
			db.disconnect();
			db.connect("localhost", 27017);
			db.connect();
			db.disconnect();

    		System.out.println(UserType.LIST.ordinal());

    		for (UserType ut : UserType.values()) {
    			System.out.println(ut.name());
    			System.out.println(ut.getID());
    		}

    		System.out.println(UniverseType.LIST.ordinal());

    		for (UniverseType ut : UniverseType.values()) {
    			System.out.println(ut.name());
    			System.out.println(ut.getID());
    		}

    		Interval[] intervals = new Interval[5];
	        intervals[0] = new IntervalInteger(15, 48);
	        intervals[1] = new IntervalInteger(45, 60);
	        intervals[2] = new IntervalDouble(20.0, 70.0);
	        intervals[3] = new IntervalDouble(46.0, 55.0);
	        intervals[4] = new IntervalDouble(10.0, 5.0);

	        System.out.println(intervals[0].intersects(intervals[1]));
	        System.out.println(intervals[0].contains(1));
	        System.out.println(intervals[2].contains(40.0));

	        for (Interval i : intervals) {
	        	System.out.println(i);
	        }

		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
	}
}