package locomotor.front.user;

import com.eclipsesource.json.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import locomotor.front.components.FrontResourceManager;
import locomotor.front.components.network.ClientRequest;

/**
 * The bridge between Java and JavaScript.
 */
public class Bridge {
	
	/**
	 * The WebView.
	 */
	WebView _view;

	/**
	 * The Stage.
	 */
	Stage _stage;

	/**
	 * The long token.
	 */
	String _longToken;

	/**
	 * The short token.
	 */
	String _shortToken;

	/**
	 * The name where the token is stored.
	 */
	static String tokenFileName = ".token";

	/**
	 * Constructs the object.
	 *
	 * @param      view   The view
	 * @param      stage  The stage
	 */
	public Bridge(WebView view, Stage stage) {
		_view = view;
		_stage = stage;
		_longToken = null;
		_shortToken = null;

		view.getEngine().executeScript("console.log = function(message){app.log(message);};");

		view.getEngine().executeScript("window.initWhenReady = true;"); // say to the client that everything is ready
	}

	/**
	 * Print the log in the standard output.
	 *
	 * @param      text  The text
	 */
	public void log(String text) {
		System.out.println(text);
	}

	/**
	 * Sets the title.
	 *
	 * @param      title  The title
	 */
	public void setTitle(String title) {
		_stage.setTitle("Locomotor - " + title);
	}

	/**
	 * Sets the long token.
	 *
	 * @param      token  The token
	 */
	public void setLongToken(String token) {
		_longToken = token;
		try {
			PrintWriter writer = new PrintWriter(tokenFileName, "UTF-8");
			writer.println(token);
			writer.close();
		}
		catch(Exception e) {
			System.out.println("Client, trying to write the long token to disk : " + e);
		}
	}

	/**
	 * Sets the short token.
	 *
	 * @param      token  The token
	 */
	public void setShortToken(String token) {
		_shortToken = token;
	}

	/**
	 * Gets the long token.
	 *
	 * @return     The long token.
	 */
	public String getLongToken() {
		if(_longToken != null) {
			return _longToken;
		}

		// check if the file .token exists
		File f = new File(tokenFileName);
		if(!f.exists()) {
			return null;
		}

		String str = null;

		// read
		try {
			InputStream is = new FileInputStream(tokenFileName);
			InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			str = br.readLine();
		}
		catch(Exception e) {
			System.out.println("Client, trying to read the long token from disk: ");
		}

		return str;
	}

	/**
	 * Delete the long token.
	 */
	public void deleteLongToken() {
		_longToken = null;
		File f = new File(tokenFileName);
		f.delete();
	}

	/**
	 * Gets the short token.
	 *
	 * @return     The short token.
	 */
	public String getShortToken() {
		return _shortToken;
	}

	// API

	/**
	 * Authentification with the username and password.
	 *
	 * @param      name       The name
	 * @param      password   The password
	 * @param      promiseID  The promise id
	 */
	public void auth(String name, String password, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("username", name);
		c.addParam("password", password);
		c.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	/**
	 * Authentification with the token.
	 *
	 * @param      token      The token
	 * @param      promiseID  The promise id
	 */
	public void tokenAuth(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	/**
	 * Register with the username and password.
	 *
	 * @param      name       The name
	 * @param      password   The password
	 * @param      promiseID  The promise id
	 */
	public void register(String name, String password, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("username", name);
		c.addParam("password", password);
		c.requestJson("api/user/register").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	/**
	 * Gets the model.
	 *
	 * @param      token      The token
	 * @param      promiseID  The promise id
	 */
	public void getModel(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/model/get").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void search(String token, String criteria, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("criterias", criteria);
		c.requestJson("api/search").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getImage(String token, String url, int promiseID) {
		FrontResourceManager rm = FrontResourceManager.getInstance();
		rm.getRemoteResource("images/" + url, token).thenAccept(new Consumer<File>() {
			public void accept(File file) {
				if(file == null) {
					return;
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].resolve('" + file.toString() + "');");
					}
				});
			}
		});
	}

	public void getItem(String token, String id, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("id", id);
		c.requestJson("api/item/get").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getUniverse(String token, String id, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("id", id);
		c.requestJson("api/universe/get").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getUserInfo(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/user/info").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void changeUsername(String token, String newName, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("newUsername", newName);
		c.requestJson("api/user/change/username").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void changePassword(String token, String oldPass, String newPass, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("oldPassword", oldPass);
		c.addParam("newPassword", newPass);
		c.requestJson("api/user/change/password").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void book(String token, String id, long startDate, long endDate, int quantity, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("id", id);
		c.addParam("startDate", String.valueOf(startDate));
		c.addParam("endDate", String.valueOf(endDate));
		c.addParam("quantity", String.valueOf(quantity));
		c.requestJson("api/booking/add").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getAllBooking(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/booking/get-all").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void removeBooking(String token, String id, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("id", id);
		c.requestJson("api/booking/remove").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getUnits(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/unit/get-all").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	/* -------------------------------------- ADMIN ------------------------------- */

	public void adminAuth(String login, String password, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("username", login);
		c.addParam("password", password);
		c.requestJson("api/admin/auth").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void adminTokenAuth(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/admin/auth").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getAllItems(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/admin/item/get-all").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void getAllUniverses(String token, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.requestJson("api/admin/universe/get-all").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void addItem(String token, String item, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("item", item);
		c.requestJson("api/admin/item/add").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void updateItem(String token, String item, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("item", item);
		c.requestJson("api/admin/item/update").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void removeItem(String token, String item, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("id", item);
		c.requestJson("api/admin/item/remove").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void removeUniverse(String token, String id, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("id", id);
		c.requestJson("api/admin/universe/remove").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void updateUniverse(String token, String universe, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("universe", universe);
		c.requestJson("api/admin/universe/update").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}

	public void addUniverse(String token, String universe, int promiseID) {
		ClientRequest c = new ClientRequest();
		c.addParam("token", token);
		c.addParam("universe", universe);
		c.requestJson("api/admin/universe/add").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_view.getEngine().executeScript("window.promises[" + promiseID + "].jsonResolve(" + obj + ");");
					}
				});
			}
		});
	}
}