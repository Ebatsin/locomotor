package locomotor.front.components.network;

public class FileUpload {
	URL _url;
	HttpsURLConnection _connection;
	OutputStream _outStream;
	int _length;
	String _boundary;

	public FileUpload(String requestURL) {

	}

	public void add(String parameter, String value) {

	}

	public void add(String parameter, InputStream value) {

	}

	public void send() {
		_url = new URL(requestURL);
		_boundary = "===" + System.currentTimeMillis() + "===";

		_connection = (HttpsURLConnection)_url.openConnection();

		_connection.setUseCaches(false);
		_connection.setDoOutput(true); // POST method
		_connection.setDoInput(true);
		_connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + _boundary);
		_connection.setRequestProperty("Content-Length", _length);

		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.close();

	}

	public HttpsURLConnection getURLConnection() {

	}
}