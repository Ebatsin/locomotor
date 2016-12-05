package locomotor.components.network;

import java.util.TreeMap;

public interface IEndpointHandler {
	public void handle(TreeMap<String, String> parameters);
}