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
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;




/**
 * Singleton class tp handle all the tasks related to the network. Setting the server up, 
 * handling connections and dispatching the clients on different threads.
 */
public class NetworkHandler {
	private static NetworkHandler _netHandler = null;

	private HttpsServer _server;

	private HashMap<String, IEndpointHandler> _handlers;

	private String _root;


	private NetworkHandler() {
		_root = "/";
		_handlers = new HashMap<String, IEndpointHandler>();
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
	 * @param root The directory to listen to. Must start by "/" (if you want to listen to everything, use "/")
	 * @param keyStoreName The path to the keyStore that contains the certificate
	 * @param keyStorePassword The password of the keyStore
	 */
	public synchronized void init(int port, String root, String keyStoreName, String keyStorePassword) {
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
						System.out.println("Error"); // @todo create meaningful messages
					}
				}
			});
		}
		catch(Exception exception) {
			System.out.println("Error while creating the server : " + exception.toString());
			System.exit(1);
		}

		_root = root;

		_server.createContext(_root, new HttpHandler() {
			public void handle(HttpExchange exchange) throws IOException {
				System.out.println("Client connection. \nRequest method : " + exchange.getRequestMethod() 
					+ " on '" + exchange.getRequestURI() + "'");
				TreeMap<String, String> parameters = parsePostParameters(exchange.getRequestBody());
				IEndpointHandler handler = _handlers.get(exchange.getRequestURI().toString());
				System.out.println("Hooks existants :");
				for(Map.Entry<String,IEndpointHandler> entry : _handlers.entrySet()) {
					System.out.println(entry.getKey() + " => " + entry.getValue());
				}

				System.out.println("trouvé : " + handler);

				if(handler == null) {
					handler = _handlers.get(exchange.getRequestURI() + "/");
				}

				String response;

				if(handler == null) {
					response = "Impossible to find a handler for this request";
				}
				else {
					response = "hang on";
					handler.handle(parameters);
				}

				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			}
		});
	}

	/**
	 * Add a handler for an URI.
	 * @param endpoint The URI that will be matched (ex: for https://serverAddress/a/b, give "a/b" as the endpoint)
	 * @param handler The handler that will handle the request
	 */
	public void link(String endpoint, IEndpointHandler handler) {
		System.out.println("endpoint creation : '" + _root + endpoint + "'. objet : " + handler);
		_handlers.put(_root + endpoint, handler);
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

	public TreeMap<String, String> parsePostParameters(InputStream is) {
		try {
			TreeMap<String, String> params = new TreeMap<String, String>();
			InputStreamReader isr = new InputStreamReader(is,"utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();

			String[] keyval = query.split("[&]");
			for(String str : keyval) {
				String[] param = str.split("[=]");
				String key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
				String value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				params.put(key, value);
			}

			return params;
		}
		catch(Exception exception) {
			System.out.println("Error while parsing parameters. " + exception.toString());
			System.exit(1);
		}
		return null;
	}
}