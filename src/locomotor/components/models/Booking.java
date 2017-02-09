package locomotor.components.models;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.JSONDisplayable;

/**
 * Represent a booking made by an user on a item for a range of time.
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
	 * The name of the universe where belongs the item.
	 */
	private String _universeName;

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

	/**
	 * Constructs the object.
	 *
	 * @param      id            The identifier
	 * @param      itemID        The item id
	 * @param      itemName      The item name
	 * @param      itemImage     The item image
	 * @param      universeName  The universe name
	 * @param      qt            The quantity
	 * @param      startDate     The start date
	 * @param      endDate       The end date
	 */
	public Booking(String id, String itemID, String itemName, String itemImage, String universeName, int qt, long startDate, long endDate) {
		_identifier = id;
		_itemID = itemID;
		_itemName = itemName;
		_itemImage = itemImage;
		_universeName = universeName;
		_quantity = qt;
		_startDate = startDate;
		_endDate = endDate;
	}

	/**
	 * Return the JSON value of the booking.
	 *
	 * @return     The booking
	 */
	public JsonValue toJSON() {
		JsonObject booking = Json.object();
		booking.add("_id", _identifier);
		booking.add("itemID", _itemID);
		booking.add("itemName", _itemName);
		booking.add("itemImage", _itemImage);
		booking.add("universeName", _universeName);
		booking.add("qt", _quantity);
		booking.add("startDate", _startDate);
		booking.add("endDate", _endDate);
		return booking;
	}

}