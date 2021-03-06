package locomotor.front.components.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * @todo.
 */
public class FileUpload {

	/**
	 * @todo.
	 */
	URL _url;

	/**
	 * @todo.
	 */
	HttpsURLConnection _connection;

	/**
	 * @todo.
	 */
	ByteArrayOutputStream _outStream;

	/**
	 * @todo.
	 */
	int _length;

	/**
	 * @todo.
	 */
	String _boundary;

	/**
	 * Constructs the object.
	 */
	public FileUpload() {
		try {
			_outStream = new ByteArrayOutputStream();
			_boundary = "===" + System.currentTimeMillis() + "===";
			_length = 0;
		}
		catch(Exception exception) { // @todo meaningful error
			System.out.println("Erreur lors de l'initalisation de la requête");
		}
	}

	/**
	 * @todo.
	 */
	public void add(String parameter, String value) {
		try {
			String addedContent = "--" + _boundary + "\r\n"
				+ "Content-Disposition: form-data; name=\"" + parameter + "\"\r\n"
				+ "Content-Type: text/plain; charset=utf-8\r\n\r\n"
				+ value + "\r\n";
			byte[] data = addedContent.getBytes("utf-8");
			_length += data.length;
			_outStream.write(data, 0, data.length);
		}
		catch(Exception exception) {
			System.out.println("Unable to UTF-8 encore the value");
		}
	}

	/**
	 * @todo.
	 */
	public void add(String parameter, InputStream value) {

	}

	/**
	 * @todo.
	 */
	public void send(String requestURL) {
		try {
			_url = new URL(requestURL);
			_connection = (HttpsURLConnection)_url.openConnection();

			_connection.setUseCaches(false);
			_connection.setDoOutput(true); // POST method
			_connection.setDoInput(true);
			_connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + _boundary);

			String finish = "--" + _boundary + "--\r\n";
			byte[] data = finish.getBytes("utf-8");
			_length += data.length;
			_outStream.write(data, 0, data.length);

			_connection.setRequestProperty("Content-Length", String.valueOf(_length));

			OutputStream out = _connection.getOutputStream();
			_outStream.writeTo(out);
			out.flush();
		}
		catch(ConnectException ce) {
			System.out.println("Cannot connect to the server (not running, firewall, network issues, etc.)");
		}
		catch(Exception exception) {
			System.out.println("Unable to UTF-8 encore the finish data");
		}
	}

	/**
	 * Gets the url connection.
	 *
	 * @return     The url connection.
	 */
	public HttpsURLConnection getURLConnection() {
		return _connection;
	}
}