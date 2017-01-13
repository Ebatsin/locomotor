package locomotor.components.network;

/**
 * The interface allows the objects that implement it to receive and handle network requests
 * from a client en send a response.
 */
public interface IEndpointHandler {

	/**
	 * The core of this method will contain all the handling of the client's request.
	 * @param data All the data sent by the client in its request
	 * @param response The context allowing the current method to send the response to the client
	 */
	public void handle(NetworkData data, NetworkResponseFactory response);

}