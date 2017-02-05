package locomotor.front.components;

import java.util.function.Consumer;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import com.eclipsesource.json.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import locomotor.components.ResourceManager;
import locomotor.front.components.network.BinaryObject;
import locomotor.front.components.network.ClientRequest;

/**
 * The resource manager of the frontend.
 */
public class FrontResourceManager extends ResourceManager {
	
	/**
	 * The singleton.
	 */
	protected static FrontResourceManager _instance = null;

	/**
	 * Constructs the object.
	 */
	protected FrontResourceManager() {
		super();
		_baseURL = "resources/cache/";
	}

	/**
	 * Gets the instance.
	 *
	 * @return     The instance.
	 */
	public static synchronized FrontResourceManager getInstance() {
		if(_instance == null) {
			_instance = new FrontResourceManager();
		}

		return _instance;
	}

	public CompletableFuture<Long> getRemoteVersion(String resource, String token) {
		ClientRequest cr = new ClientRequest();
		cr.addParam("token", token);
		cr.addParam("id", resource);
		return cr.requestJson("api/resource/version").thenApply(new Function<JsonObject, Long>() {
			public Long apply(JsonObject obj) {
				if(obj.get("success").asString().equals("true")) {
					return obj.get("data").asObject().get("version").asLong();
				}
				else {
					System.out.println("version : erreur");
				}

				return new Long(0);
			}
		});
	}
	/**
	* Fetch a ressource from the server if the local version is not up to date. If the resource does not exist, returns null.
	* @param resource The resource to get on the server
	* @returns A promise containing the resource stored locally. When resolved, the promise contains either the resource as a File object 
	* or null if the resource was not found on the server
	*/
	public CompletableFuture<File> getRemoteResource(String resource, String token) {
		// check if the ressource exists locally
		if(!exists(resource)) {
			// get the remote resource
			ClientRequest cr = new ClientRequest();
			cr.addParam("token", token);
			cr.addParam("id", resource);
			return cr.requestBinary("api/resource/get").thenApply(new Function<BinaryObject, File>() {
				public File apply(BinaryObject obj) {
					if(obj.isSuccess()) {
						ByteArrayOutputStream receivedResource = obj.getAsBinary();
						try {
							OutputStream outputStream = new FileOutputStream(_baseURL + resource);
							receivedResource.writeTo(outputStream);
						}
						catch(Exception exception) {
							System.out.println("Unable to write the file");
							exception.printStackTrace();
						}

						return new File(_baseURL + resource);
					}
					return null;
				}
			});
		}
		else {
			CompletableFuture<File> file = new CompletableFuture<File>();

			// get the remote version
			getRemoteVersion(resource, token).thenAccept(new Consumer<Long>() {
				public void accept(Long version) {
					if(version > getVersion(resource)) { // update the local version
						ClientRequest crUpdate = new ClientRequest();
						crUpdate.addParam("token", token);
						crUpdate.addParam("id", resource);
						crUpdate.requestBinary("api/resource/get").thenAccept(new Consumer<BinaryObject>() {
							public void accept(BinaryObject obj) {
								if(obj.isSuccess()) {
									ByteArrayOutputStream receivedResource = obj.getAsBinary();
									try {
										OutputStream outputStream = new FileOutputStream(_baseURL + resource);
										receivedResource.writeTo(outputStream);
									}
									catch(Exception exception) {
										System.out.println("Unable to write the file (1)");
										exception.printStackTrace();
									}

									file.complete(new File(_baseURL + resource));
								}
								else {
									System.out.println("erreur : " + obj.getErrorMessage());
									file.complete(null);
								}
							}
						});
					}
					else {
						// return the local version
						file.complete(new File(_baseURL + resource));
					}
				}
			});

			return file;
		}
	}
}