package locomotor.components.network;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import delight.fileupload.FileUpload;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;

/**
 * Holds all the data about the parameters sent in a POST request.
 */
public class NetworkData {
	
	/**
	 * Contains all the parameters sent in the request.
	 */
	TreeMap<String, ByteArrayOutputStream> _params;

	/**
	 * Stores whether the parameter parsing was a success or not.
	 */
	boolean _isValid;

	/**
	 * Builds a new data holder. Before using its methods, check with {@link #isValid() isValid} 
	 * that no error happened during the generation.
	 *
	 * @param      exchange  The exchange
	 */
	public NetworkData(HttpExchange exchange) {
		_params = new TreeMap<String, ByteArrayOutputStream>();
		_isValid = true;

		Headers headers = exchange.getRequestHeaders();

		if(!headers.containsKey("Content-Length")) {
			//drop the handling
			_isValid = false;
			System.out.println("Error: No length specified in this request");
			return;
		}

		if(!headers.containsKey("Content-Type")) {
			_isValid = false;
			System.out.println("Error : No Content-Type specified in this request");
			return;
		}

		int contentLength;
		String contentType;
		byte[] data;

		contentLength = Integer.parseInt(headers.getFirst("Content-Length"));
		contentType = headers.getFirst("Content-Type");

		data = new byte[contentLength]; // prepare a buffer the size of the request body

		DataInputStream dis = new DataInputStream(exchange.getRequestBody());
		try {
			dis.readFully(data);
		}
		catch(IOException exception) {
			_isValid = false;
			System.out.println("Error : Unable to read the request content");
			return;
		}

		FileItemIterator iterator = FileUpload.parse(data, contentType);

		if(iterator == null) {
			_isValid = false;
			System.out.println("Error : The request is not in the multipart/form-data form");
			return;
		}

		try {
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream is = item.openStream();
				byte[] buffer = new byte[8192];
				int bytesRead;
				ByteArrayOutputStream output = new ByteArrayOutputStream();

				while((bytesRead = is.read(buffer)) != -1) {
					output.write(buffer, 0, bytesRead);
				}
				output.flush();

				_params.put(item.getFieldName(), output);
			}
		}
		catch(Exception exception) {
			_isValid = false;
			System.out.println("Error : Invalid parameter");
			return;
		}
	}

	/**
	 * Returns a parameter content as a String.
	 * @param param The name of the parameter to search for. It must exist (see {@link #isDefined(String) isDefined})
	 * @return The content of the parameter or an empty string if the charset is not known
	 */
	public String getAsString(String param) {
		try {
			return _params.get(param).toString("utf-8");
		}
		catch(Exception exception) {
			return "";
		}
	}

	/**
	 * Returns a parameter content as bineray.
	 * @param param The name of the parameter to search for. It must exist (see {@link #isDefined(String) isDefined})
	 * @return The content of the parameter
	 */
	public byte[] getAsBinary(String param) {
		return _params.get(param).toByteArray();
	}

	/**
	 * Check if a given parameter has been defined in the POST request.
	 * @param param The name of the param to search for
	 * @return Whether the parameter is defined or not
	 */
	public boolean isDefined(String param) {
		return _params.containsKey(param);
	}

	/**
	 * Returns all the received parameters names.
	 * @return An array of the received parameters names.
	 */
	public ArrayList<String> getParametersName() {
		ArrayList<String> names = new ArrayList<String>();
		for(Map.Entry<String, ByteArrayOutputStream> entry : _params.entrySet()) {
			names.add(entry.getKey());
		}

		return names;
	}

	/**
	 * Check whether the parameter parsing was or a success or not.
	 * @return true if the parsing was a success
	 */
	public boolean isValid() {
		return _isValid;
	}
}