package locomotor.core;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Utility class to hash and check password.
 */
public class PasswordStorage {

	/**
	 * Exception for signaling invalid hash errors.
	 */
	@SuppressWarnings("serial")
	public static class InvalidHashException extends Exception {
		
		/**
		 * Constructs the object.
		 *
		 * @param      message  The message
		 */
		public InvalidHashException(String message) {
			super(message);
		}
		
		/**
		 * Constructs the object.
		 *
		 * @param      message  The message
		 * @param      source   The source
		 */
		public InvalidHashException(String message, Throwable source) {
			super(message, source);
		}
	}

	/**
	 * Exception for signaling cannot perform operation errors.
	 */
	@SuppressWarnings("serial")
	public static class CannotPerformOperationException extends Exception {
		
		/**
		 * Constructs the object.
		 *
		 * @param      message  The message
		 */
		public CannotPerformOperationException(String message) {
			super(message);
		}
		
		/**
		 * Constructs the object.
		 *
		 * @param      message  The message
		 * @param      source   The source
		 */
		public CannotPerformOperationException(String message, Throwable source) {
			super(message, source);
		}
	}
	
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	// These constants may be changed without breaking existing hashes.
	//! \{
	public static final int SALT_BYTE_SIZE = 24;
	public static final int HASH_BYTE_SIZE = 18;
	public static final int PBKDF2_ITERATIONS = 64000;
	//! \}

	// These constants define the encoding and may not be changed.
	//! \{
	public static final int HASH_SECTIONS = 5;
	public static final int HASH_ALGORITHM_INDEX = 0;
	public static final int ITERATION_INDEX = 1;
	public static final int HASH_SIZE_INDEX = 2;
	public static final int SALT_INDEX = 3;
	public static final int PBKDF2_INDEX = 4;
	//! \}

	/**
	* Creates a hash.
	*
	* @param      password								The password
	*
	* @return     The password hashed
	*
	* @throws     CannotPerformOperationException		can't perform the operation.
	*/
	public static String createHash(String password) throws CannotPerformOperationException {
		return createHash(password.toCharArray());
	}

	/**
	* Creates a hash.
	*
	* @param      password								The password
	*
	* @return     The password hashed
	*
	* @throws     CannotPerformOperationException		can't perform the operation.
	*/
	public static String createHash(char[] password) throws CannotPerformOperationException {
		// Generate a random salt
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);

		// Hash the password
		byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
		int hashSize = hash.length;

		// format: algorithm:iterations:hashSize:salt:hash
		String parts = "sha1:"
			+ PBKDF2_ITERATIONS
			+ ":"
			+ hashSize
			+ ":"
			+ toBase64(salt)
			+ ":"
			+ toBase64(hash);
		return parts;
	}

	/**
	* Verify the password.
	*
	* @param      password							The password
	* @param      correctHash                      The correct hash
	*
	* @return     True if same, false otherwise.
	*
	* @throws     CannotPerformOperationException  can't perform the operation.
	* @throws     InvalidHashException             hash is not valid.
	*/
	public static boolean verifyPassword(String password, String correctHash)
		throws CannotPerformOperationException, InvalidHashException {
		return verifyPassword(password.toCharArray(), correctHash);
	}

	/**
	 * Verify the password.
	 *
	 * @param      password                         The password
	 * @param      correctHash                      The correct hash
	 *
	 * @return     True if same, false otherwise.
	 *
	 * @throws     CannotPerformOperationException  can't perform the operation.
	 * @throws     InvalidHashException             hash is not valid.
	 */
	public static boolean verifyPassword(char[] password, String correctHash) 
		throws CannotPerformOperationException, InvalidHashException {
		// Decode the hash into its parameters
		String[] params = correctHash.split(":");
		if(params.length != HASH_SECTIONS) {
			throw new InvalidHashException(
				"Fields are missing from the password hash."
			);
		}

		// Currently, Java only supports SHA1.
		if(!params[HASH_ALGORITHM_INDEX].equals("sha1")) {
			throw new CannotPerformOperationException(
				"Unsupported hash type."
			);
		}

		int iterations = 0;
		try {
			iterations = Integer.parseInt(params[ITERATION_INDEX]);
		}
		catch (NumberFormatException ex) {
			throw new InvalidHashException(
				"Could not parse the iteration count as an integer.",
				ex
			);
		}

		if(iterations < 1) {
			throw new InvalidHashException(
				"Invalid number of iterations. Must be >= 1."
			);
		}


		byte[] salt = null;
		try {
			salt = fromBase64(params[SALT_INDEX]);
		}
		catch (IllegalArgumentException ex) {
			throw new InvalidHashException(
				"Base64 decoding of salt failed.",
				ex
			);
		}

		byte[] hash = null;
		try {
			hash = fromBase64(params[PBKDF2_INDEX]);
		}
		catch (IllegalArgumentException ex) {
			throw new InvalidHashException(
				"Base64 decoding of pbkdf2 output failed.",
				ex
			);
		}


		int storedHashSize = 0;
		try {
			storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
		}
		catch (NumberFormatException ex) {
			throw new InvalidHashException(
				"Could not parse the hash size as an integer.",
				ex
			);
		}

		if(storedHashSize != hash.length) {
			throw new InvalidHashException(
				"Hash length doesn't match stored hash length."
			);
		}

		// Compute the hash of the provided password, using the same salt, 
		// iteration count, and hash length
		byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(hash, testHash);
	}

	/**
	* Compare two bytes array.
	*
	* @param      a     The first array
	* @param      b     The second array
	*
	* @return     True if same, false otherwise.
	*/
	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for(int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}

	/**
	* Compute the hash of the provided password, using the same salt
	* iteration count, and hash length.
	*
	* @param      password							The password
	* @param      salt								The salt
	* @param      iterations                       The iterations
	* @param      bytes                            The bytes
	*
	* @return     An array of bytes, the hashed password
	*
	* @throws     CannotPerformOperationException	cannot performe the operation.
	*/
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws CannotPerformOperationException {
		try {
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			return skf.generateSecret(spec).getEncoded();
		}
		catch (NoSuchAlgorithmException ex) {
			throw new CannotPerformOperationException(
				"Hash algorithm not supported.",
				ex
			);
		}
		catch (InvalidKeySpecException ex) {
			throw new CannotPerformOperationException(
				"Invalid key spec.",
				ex
			);
		}
	}

	/**
	* Convert hex to bytes.
	*
	* @param      hex	The hexadecimal
	*
	* @return     The array of byte
	*
	* @throws     IllegalArgumentException	argument is not valid.
	*/
	private static byte[] fromBase64(String hex) throws IllegalArgumentException {
		return DatatypeConverter.parseBase64Binary(hex);
	}

	/**
	* Convert bytes to hex.
	*
	* @param      array	The array of bytes
	*
	* @return     The hex
	*/
	private static String toBase64(byte[] array) {
		return DatatypeConverter.printBase64Binary(array);
	}

}
