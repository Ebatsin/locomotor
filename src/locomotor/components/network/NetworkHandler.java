package locomotor.components.network;

/**
 * Handle all the tasks related to the network. Setting the server up, handling connections and dispatching the clients on different threads.
 */
public class NetworkHandler {
	/**
	 * Create a server
	 * @param port The port to listen to
	 * @param endpoint The root directory of the network (ex. For https://server/endpoint, use "/endpoint")
	 */
	public NetworkHandler(int port, String endpoint) {

	}

	/**
	 * Create a server using a custom certificate (useful when using self-signed certificates)
	 * @param port The port to listen to
	 * @param endpoint The root directory of the network (ex. For https://server/endpoint, use "/endpoint")
	 * @param keyStoreName The path to the keyStore that contains the certificate
	 * @param keyStorePassword The password of the keyStore
	 */
	public NetworkHandler(int ip, String endpoint, String keyStoreName, String keyStorePassword) {

	}
}