package locomotor.front.components.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BinaryObject {
	boolean _isError;
	String _error;
	ByteArrayOutputStream _stream;
	String _mimeType;

	public BinaryObject(InputStream input, String mimeType) {
		_isError = false;
		_error = "";
		_stream = new ByteArrayOutputStream();
		_mimeType = mimeType;

		try {
			int c;
			while((c = input.read()) != -1) {
				_stream.write(c);
			}
		}
		catch(Exception exception) {
			_isError = true;
			_error = "Unable to read the server answer";
		}
	}

	public BinaryObject(String errorMessage) {
		_isError = true;
		_error = errorMessage;
		_stream = new ByteArrayOutputStream();
		_mimeType = "";
	}

	public boolean isSuccess() {
		return !_isError;
	}

	public String getErrorMessage() {
		return _error;
	}

	public ByteArrayOutputStream getAsBinary() {
		return _stream;
	}

	public String getMimeType() {
		return _mimeType;
	}

	public String guessFileExtension() {
		switch(_mimeType) {
			case "image/png" : { // Java 7 allows string in case statements
					return ".png";
				}
			case "image/jpg": {
					return ".jpg";
				}
		}

		return "";
	}
}