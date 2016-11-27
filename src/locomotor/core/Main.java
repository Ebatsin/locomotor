package locomotor.core;

public class Main {
	public static void main(String[] args) {

		try {

			DBH db = DBH.getInstance("localhost", 27017);

		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		
	}
}