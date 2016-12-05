package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import java.util.Arrays;



/**
 * Singleton class tp handle all the tasks related to the network. Setting the server up, 
 * handling connections and dispatching the clients on different threads.
 */
public class NetworkHandler {
	private static NetworkHandler _netHandler = null;

	private HttpsServer _server;


	private NetworkHandler() {
	}

	/**
	 * Returns the instance of this singleton class.
	 * @returns The instance
	 */
	public static synchronized NetworkHandler getInstance() {
		if(_netHandler == null) {
			_netHandler = new NetworkHandler();
		}

		return _netHandler;
	}

	public synchronized void init(int ip, String endpoint) {

	}

	/**
	 * Create a server using a custom certificate (useful when using self-signed certificates).
	 * @param port The port to listen to
	 * @param keyStoreName The path to the keyStore that contains the certificate
	 * @param keyStorePassword The password of the keyStore
	 */
	public synchronized void init(int port, String keyStoreName, String keyStorePassword) {
		try {
			InetSocketAddress address = new InetSocketAddress(port);
			char[] password = keyStorePassword.toCharArray();


			_server = HttpsServer.create(address, 0);
			SSLContext sslContext = SSLContext.getInstance("TLS");

			// retrieve keyStore
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			FileInputStream fis = new FileInputStream(keyStoreName);
			keyStore.load(fis, password);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			System.out.println("done");
			kmf.init(keyStore, password);
			System.out.println("done");


			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);

			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			_server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
				@Override
				public void configure(HttpsParameters params) {
					try {
						System.out.println("connexion");
						SSLContext context = SSLContext.getDefault();
						SSLEngine engine = context.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());
						params.setSSLParameters(context.getDefaultSSLParameters());
					}
					catch(Exception exception) {
						System.out.println("Error"); // @todo create meaningful messages
					}
				}
			});
		}
		catch(Exception exception) {
			System.out.println("Error while creating the server : " + exception.toString());
			System.exit(1);
		}

		_server.createContext("/", new HttpHandler() {
			public void handle(HttpExchange exchange) throws IOException {
				System.out.println("Client connection. Request : " + exchange.getRequestURI());
				String response = "Hey, what's up ?";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
		});
	}

	/**
	 * Starts the server.
	 */
	public void start() {
		_server.setExecutor(null);
		_server.start();
	}

	/**
	 * Stops the server.
	 */
	public void stop() {
		_server.stop(5);
	}
}