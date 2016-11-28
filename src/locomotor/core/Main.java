package locomotor.core;

import java.util.logging.*;

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
    			System.out.println(ut.ordinal());
    		}

    		System.out.println(UniverseType.LIST.ordinal());

    		for (UniverseType ut : UniverseType.values()) {
    			System.out.println(ut.name());
    			System.out.println(ut.ordinal());
    		}

		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
	}
}