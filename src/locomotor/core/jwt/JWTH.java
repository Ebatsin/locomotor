package locomotor.core.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64; 
import java.util.Date; 
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.lang.Thread;
import locomotor.components.logging.*;

/**
 * JSON Web Token class handler: create and check token.
 */
public class JWTH {

	/**
	 * Singleton JSON Web Token handler object, with lazy instanciation.
	 */
	private static JWTH jwt = null;

	/**
	 * @todo currently hard, need to generate and store or retrieve it.
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
	 * @param      subject     The subject
	 * @param      isAdmin     Indicates if admin
	 * @param      expiration  The expiration (milliseconds)
	 *
	 * @return     The token (serialized)
	 */
	private static String createToken(String subject, boolean isAdmin, long expiration) {

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
							.claim("role", (isAdmin ? "admin" : "user"))
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
			
			System.out.println("Error: token signature not valid");
			System.out.println(se);
			// @todo: handle error with new system
			return null;

		}

		return claims;
	}

	/**
	 * Creates a short-time token.
	 *
	 * @param      subject  The subject
	 * @param      isAdmin  Indicates if admin
	 *
	 * @return     The short-time token
	 */
	public static String createShortToken(String subject, boolean isAdmin) {
		// 2 hours
		long duration = 7200000L;
		return createToken(subject, isAdmin, duration);
	}

	/**
	 * Creates a long-time token.
	 *
	 * @param      subject  The subject
	 * @param      isAdmin  Indicates if admin
	 *
	 * @return     The long-time token
	 */
	public static String createLongToken(String subject, boolean isAdmin) {
		String method1 = "createLongToken";
		Logging log1 = new Logging("Create the long term token", false, "User " + subject);
		ErrorContext ec = ErrorHandler.getInstance().get(Thread.currentThread().getId());
		ec.add(method1, log1);
		// 60 days
		long duration = 5184000000L;
		return createToken(subject, isAdmin, duration);
	}

	/**
	 * Check token validity.
	 *
	 * @param      token  The token
	 */
	public static void checkToken(String token) {

		// not null cause error check before
		Claims claims = verifyTokenSignature(token);

		boolean isTokenStillValid = claims.getExpiration().after(new Date(System.currentTimeMillis()));

		// @todo: handle error with new system, if not valid
		
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Issuer: " + claims.getIssuer());
		System.out.println("Role: " + claims.get("role"));
	}
}