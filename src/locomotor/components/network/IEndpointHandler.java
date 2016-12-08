package locomotor.components.network;

public interface IEndpointHandler {
	public void handle(NetworkData data, NetworkResponseFactory response);
}