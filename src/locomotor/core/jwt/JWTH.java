package locomotor.core.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.lang.Thread;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64; 
import java.util.Date; 
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import locomotor.components.Pair;
import locomotor.components.logging.ErrorHandler;
import locomotor.core.AccreditationLevel;

/**
 * JSON Web Token class handler: create and check token.
 */
public class JWTH {

	/**
	 * Singleton JSON Web Token handler object, with lazy instanciation.
	 */
	private static JWTH jwt = null;

	/**
	 * The private key.
	 */
	private static String secretKey = "testKey";

	/**
	 * Private constructor to prevent any other class to instantiate.
	 */
	private JWTH() {}

	/**
	 * Gets the instance.
	 *
	 * @return     The instance.
	 */
	public static synchronized JWTH getInstance() {
		if(jwt == null) {
			jwt = new JWTH();
		}
		return jwt;
	}

	/**
	 * Creates a token.
	 *
	 * @param      subject      The subject
	 * @param      level     	Indicate the accreditation's level
	 * @param      expiration	The expiration (milliseconds)
	 *
	 * @return     The token (serialized)
	 */
	private static String createToken(String subject, AccreditationLevel level, long expiration) {

		// the JWT signature algorithm we will be using to sign the token (HMAC using SHA-512)
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
		
		// current time
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// we will sign our JWT with our secretKey
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// set claims
		JwtBuilder builder = Jwts.builder()
							.setSubject(subject)
							.setIssuer("LocomotorServer")
							.setIssuedAt(now)
							.claim("role", new Integer(level.getValue()).toString())
							.signWith(signatureAlgorithm, signingKey);

		// add the expiration if specified
		if (expiration >= 0) {
			long expMillis = nowMillis + expiration;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	/**
	 * Validate the token signature.
	 *
	 * @param      token  The token
	 *
	 * @return     Return the claims or null if validation failed
	 */
	private static Claims verifyTokenSignature(String token) {
		
		Claims claims;
		// throw if it is not a signed JWS (as expected)
		try {
			
			claims = Jwts.parser()         
				.setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(token).getBody();

		}
		catch (SignatureException se) {
			
			String message = "The token signature is not valid";
			ErrorHandler.getInstance().push("verifyTokenSignature", true, message, token);
			return null;

		}
		catch (Exception ex) {

			String message = "An error occured while checking token signature";
			ErrorHandler.getInstance().push("verifyTokenSignature", true, message, token);
			return null;

		}

		return claims;
	}

	/**
	 * Creates a short-time token.
	 *
	 * @param      subject  The subject
	 * @param      level    Indicate the accreditation's level
	 *
	 * @return     The short-time token
	 */
	public static String createShortToken(String subject, AccreditationLevel level) {
		ErrorHandler.getInstance().push("createShortToken", false, "Create the short term token", "User " + subject);
		// 2 hours
		long duration = 7200000L;
		return createToken(subject, level, duration);
	}

	/**
	 * Creates a long-time token.
	 *
	 * @param      subject  The subject
	 * @param      level    Indicate the accreditation's level
	 *
	 * @return     The long-time token
	 */
	public static String createLongToken(String subject, AccreditationLevel level) {
		ErrorHandler.getInstance().push("createLongToken", false, "Create the long term token", "User " + subject);
		// 60 days
		long duration = 5184000000L;
		return createToken(subject, level, duration);
	}

	/**
	 * Check token validity.
	 *
	 * @param      token  The token
	 *
	 * @return     The subject and the role
	 */
	public static Pair<String,AccreditationLevel> checkToken(String token) {
		ErrorHandler.getInstance().push("checkToken", false, "Check the token", token);

		Claims claims = verifyTokenSignature(token);
		
		// null is error, verification fail
		if (claims == null) {
			return null;
		}

		// check the validity
		boolean isTokenStillValid = claims.getExpiration().after(new Date(System.currentTimeMillis()));

		if (!isTokenStillValid) {
			String message = "Expiration date of the token is " + claims.getExpiration();
			ErrorHandler.getInstance().push("checkToken", true, "The token is no longer valid", message);
			return null;
		}

		return new Pair(claims.getSubject(), AccreditationLevel.valueOf(Integer.parseInt(claims.get("role").toString())));
	}
}