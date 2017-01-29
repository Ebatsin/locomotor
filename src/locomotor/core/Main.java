package locomotor.core;

import com.eclipsesource.json.*;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import locomotor.components.Compare;
import locomotor.components.models.*;
import locomotor.components.network.*;
import locomotor.components.Pair;
import locomotor.components.types.*;

/**
 * Where all the magic happens.
 */
public class Main {
	
	/**
	 * One method to rule them all.
	 */
	public static void main(String[] args) {

		// disable logging on mongodb driver
		// cf http://mongodb.github.io/mongo-java-driver/3.0/driver/reference/management/logging/
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE); 
			
		DBH db = DBH.getInstance();
		db.connect("localhost", 27017);
		db.connectToDatabase("locomotor");
		
		// long startTime = System.nanoTime();
		// long estimatedTime = System.nanoTime() - startTime;
		// System.out.println(TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS) + " ms");

		NetworkHandler nh = NetworkHandler.getInstance();
		nh.init(8000, "key.pfx", "motdepasse");

		// check the parameters given to the server. --create-superuser ask for the creation of an admin
		for(String a : args) {
			System.out.println(a);
		}
		if(args.length > 0) {
			switch(args[0]) {
				case "-h":
				case "--help":
					System.out.println("locomotor\nTo create a new super user, use the following command :");
					System.out.println("locomotor --create-superuser");
					return;
				case "--create-superuser":
					String name;
					String password, passwordCheck;
					Scanner sc = new Scanner(System.in);
					System.out.print("Username (will be modifiable from the app) :\n> ");
					name = sc.nextLine();
					System.out.print("User password :\n> ");
					password = sc.nextLine();
					System.out.print("Verify the user password :\n> ");
					passwordCheck = sc.nextLine();

					if(!password.equals(passwordCheck)) {
						System.out.println("The two passwords or not the same.");
						return;
					}

					Pair<String, Integer> user = db.registerUser(name, password, 2);

					if(user == null) {
						System.out.println("The username is already taken. Please try with another name.");
					}
					return;
			}
		}

		API.createHooks(nh);
		nh.start();
		
	}
}