package locomotor.front.components.network;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @todo.
 */
public class BinaryObject {
	
	/**
	 * @todo.
	 */
	boolean _isError;

	/**
	 * @todo.
	 */
	String _error;

	/**
	 * @todo.
	 */
	ByteArrayOutputStream _stream;

	/**
	 * @todo.
	 */
	String _mimeType;

	/**
	 * Constructs the object.
	 *
	 * @param      input     The input
	 * @param      mimeType  The mime type
	 */
	public BinaryObject(InputStream input, String mimeType) {
		_isError = false;
		_error = "";
		_stream = new ByteArrayOutputStream();
		_mimeType = mimeType;

		try {
			int character;
			while((character = input.read()) != -1) {
				_stream.write(character);
			}
		}
		catch(Exception exception) {
			_isError = true;
			_error = "Unable to read the server answer";
		}
	}

	/**
	 * Constructs the object.
	 *
	 * @param      errorMessage  The error message
	 */
	public BinaryObject(String errorMessage) {
		_isError = true;
		_error = errorMessage;
		_stream = new ByteArrayOutputStream();
		_mimeType = "";
	}

	/**
	 * Determines if success.
	 *
	 * @return     True if success, False otherwise.
	 */
	public boolean isSuccess() {
		return !_isError;
	}

	/**
	 * Gets the error message.
	 *
	 * @return     The error message.
	 */
	public String getErrorMessage() {
		return _error;
	}

	/**
	 * Gets as binary.
	 *
	 * @return     As binary.
	 */
	public ByteArrayOutputStream getAsBinary() {
		return _stream;
	}

	/**
	 * Gets the mime type.
	 *
	 * @return     The mime type.
	 */
	public String getMimeType() {
		return _mimeType;
	}

	/**
	 * @todo.
	 */
	public String guessFileExtension() {
		switch(_mimeType) {
			case "image/png" : { // Java 7 allows string in case statements
				return ".png";
			}
			case "image/jpg": {
				return ".jpg";
			}
			default: {
				break;
			}
		}

		return "";
	}
}