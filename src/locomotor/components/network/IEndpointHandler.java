package locomotor.components.network;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The interface allows the objects that implement it to receive and handle network requests
 * from a client en send a response.
 */
public abstract class IEndpointHandler {
	ArrayList<String> _expectedParams;
	NetworkData _data;
	NetworkResponseFactory _response;

	ArrayList<String> _nonDefinedParams;

	public IEndpointHandler() {
	}

	/**
	 * The core of this method will contain all the handling of the client's request.
	 * @param data All the data sent by the client in its request
	 * @param response The context allowing the current method to send the response to the client
	 * @return true if everything went well. false otherwise (the data was not valid, an error has been sent)
	 */
	public boolean handle(NetworkData data, NetworkResponseFactory response) {
		_data = data;
		_response = response;

		if(!data.isValid()) {
			response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
				"The request must be in POST format to be read by the server", API.ErrorCode.DEFAULT_ERROR_CODE);
			return false;
		}

		return true;
	}

	/**
	* Set all the parameters expected to complete this request.
	* @param params All the params to be defined for the request to be valid
	*/
	public void setExpectedParams(String ... params) {
		_expectedParams = new ArrayList<>(Arrays.asList(params));

		_nonDefinedParams = new ArrayList<>();
		for(String param : _expectedParams) {
			if(!_data.isDefined(param)) {
				_nonDefinedParams.add(param);
			}
		}
	}

	/**
	* Check wether all the parameters needed to complete the request are defined in the request.
	* @return true if all the parameters are defined. false otherwise
	*/
	public boolean areAllParamsDefined() {
		if(_expectedParams == null) {
			return true;
		}

		return _expectedParams == null ? true : (_nonDefinedParams.size() == 0);
	}

	/**
	* Returns all the parameters expected for the request but not defined.
	*/
	public ArrayList<String> getNonDefinedParams() {
		return _expectedParams == null ? new ArrayList<String>() : _nonDefinedParams;
	}

	/**
	* Create a default message listing all the missing parameters.
	*/
	public String getDefaultMissingParametersMessage() {
		if(_nonDefinedParams.size() == 0) {
			return "All the parameters are defined";
		}

		String message = "The following parameters are missing : ";
		for(String param : _nonDefinedParams) {
			message += param + ", ";
		}

		message += "they are mendatory for this request.";

		return message;
	}

	/**
	* Send a JSON failure response using the missing parameter message.
	*/
	public void sendDefaultMissingParametersMessage() {
		_response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, getDefaultMissingParametersMessage(), 
			API.ErrorCode.DEFAULT_ERROR_CODE);
	}

}