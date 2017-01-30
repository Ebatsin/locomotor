package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONDisplayable;

/**
 * @todo
 */
public class Booking implements JSONDisplayable {

	/**
	 * The identifier of the booking.
	 */
	private String _identifier;

	/**
	 * The identifier of the item booked.
	 */
	private String _itemID;

	/**
	 * The name of the item booked.
	 */
	private String _itemName;

	/**
	 * The image URL of the item booked.
	 */
	private String _itemImage;

	/**
	 * The quantity of the item booked.
	 */
	private int _quantity;
	
	/**
	 * The start date of the booking (timestamp).
	 */
	private long _startDate;

	/**
	 * The end date of the booking (timestamp).
	 */
	private long _endDate;


	public Booking(String id, String itemID, String itemName, String itemImage, int qt, long startDate, long endDate) {
		_identifier = id;
		_itemID = itemID;
		_itemName = itemName;
		_itemImage = itemImage;
		_quantity = qt;
		_startDate = startDate;
		_endDate = endDate;
	}

	/**
	 * @todo.
	 *
	 * @return     { description_of_the_return_value }
	 */
	public JsonValue toJSON() {
		JsonObject booking = Json.object();
		booking.add("_id", _identifier);
		booking.add("itemID", _itemID);
		booking.add("itemName", _itemName);
		booking.add("itemImage", _itemImage);
		booking.add("qt", _quantity);
		booking.add("startDate", _startDate);
		booking.add("endDate", _endDate);
		return booking;
	}

}