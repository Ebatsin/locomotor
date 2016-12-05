package locomotor.components.network;

import com.sun.net.httpserver.HttpsServer;

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
	 * Returns the instance of this singleton class
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
		InetSocketAddress address = new InetSocketAddress(port);
		char[] password = keyStorePassword.toCharArray();

		_server = HttpsServer.create(address, 0);
		SSLContext sslContext = SSLContext.getInstance("TLS");

		// retrieve keyStore
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		FileInputStream fis = new FileInputStream(keyStoreName);

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, password);

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

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

	/**
	 * Add a handler for a particular endpoint.
	 */
	public void link(String context, HttpHandler handler) {
		_server.createContext(context, handler);
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
		_server.stop();
	}
}