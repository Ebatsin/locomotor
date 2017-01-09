package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Thread;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import locomotor.components.logging.ErrorContext;
import locomotor.components.logging.ErrorHandler;


/**
 * Singleton class tp handle all the tasks related to the network. Setting the server up, 
 * handling connections and dispatching the clients on different threads.
 */
public class NetworkHandler {
	
	/**
	 * Contains the only instance of NetworkHandler.
	 */
	private static NetworkHandler _netHandler = null;

	/**
	 * Contains the HTTPS server used by the handler.
	 */
	private HttpsServer _server;

	/**
	 * @todo.
	 */
	private HashMap<String, IEndpointHandler> _endpointHandlers;

	/**
	 * @todo.
	 */
	private NetworkHandler() {
		_endpointHandlers = new HashMap<String, IEndpointHandler>();
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

			// retrieve keyStore
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			FileInputStream fis = new FileInputStream(keyStoreName);
			keyStore.load(fis, password);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, password);


			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			_server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
				@Override
				public void configure(HttpsParameters params) {
					try {
						SSLContext context = SSLContext.getDefault();
						SSLEngine engine = context.createSSLEngine();
						params.setNeedClientAuth(false);
						params.setCipherSuites(engine.getEnabledCipherSuites());
						params.setProtocols(engine.getEnabledProtocols());
						params.setSSLParameters(context.getDefaultSSLParameters());
					}
					catch(Exception exception) {
						System.out.println("Unable to create context to serve the client. Aborting. Error : "
							+ exception.toString());
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
				System.out.println("Client connection on '" + exchange.getRequestURI() + "'");
				IEndpointHandler handler = _endpointHandlers.get(exchange.getRequestURI().toString());
				if(handler == null) {
					handler = _endpointHandlers.get(exchange.getRequestURI().toString() + '/');
				}

				if(handler == null) {
					NetworkJsonResponse njr = new NetworkJsonResponse(exchange);
					njr.failure(NetworkResponse.ErrorCode.BAD_REQUEST, "There is no API endpoint with this name.");
					return;
				}
				else {
					
					// create error context for the current thread
					ErrorHandler.getInstance().get();

					handler.handle(new NetworkData(exchange), new NetworkResponseFactory(exchange));

					// remove error context for the current thread
					ErrorHandler.getInstance().remove();
				}
			}
		});
	}

	/**
	 * Creates an endpoint.
	 *
	 * @param      endpointName  The endpoint name
	 * @param      handler       The handler
	 */
	public void createEndpoint(String endpointName, IEndpointHandler handler) {
		_endpointHandlers.put(endpointName + '/', handler);
	}

	/**
	 * Starts the server.
	 */
	public void start() {
		// 100 threads
		_server.setExecutor(Executors.newFixedThreadPool(100));
		_server.start();
	}

	/**
	 * Stops the server.
	 */
	public void stop() {
		_server.stop(5);
	}
}