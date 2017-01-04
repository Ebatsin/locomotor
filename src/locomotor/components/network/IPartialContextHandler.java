package locomotor.components.network;

import java.net.URI;

/**
 * @todo.
 */
public interface IPartialContextHandler {

	/**
	 * @todo.
	 */
	public void handle(URI uri, NetworkData data, NetworkResponse response);

}