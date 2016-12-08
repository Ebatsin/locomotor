package locomotor.components.network;

import java.net.URI;

public interface IPartialContextHandler {
	public void handle(URI uri, NetworkData data, NetworkResponse response);
}