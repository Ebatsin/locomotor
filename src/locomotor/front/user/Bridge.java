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

public class Bridge {
	WebView _view;
	Stage _stage;

	// tokens
	String _longToken;
	String _shortToken;

	static String tokenFileName = ".token";

	public Bridge(WebView view, Stage stage) {
		_view = view;
		_stage = stage;
		_longToken = null;
		_shortToken = null;

		view.getEngine().executeScript("console.log = function(message){app.log(message);};");

		view.getEngine().executeScript("window.initWhenReady = true;"); // say to the client that everything is ready
	}

	public void log(String text) {
		System.out.println(text);
	}

	public void setTitle(String title) {
		_stage.setTitle("Locomotor - " + title);
	}



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

	public void setShortToken(String token) {
		_shortToken = token;
	}

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

	public void deleteLongToken() {
		_longToken = null;
		File f = new File(tokenFileName);
		f.delete();
	}

	public String getShortToken() {
		return _shortToken;
	}

	// API

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
					System.out.println("Le fichier est null");
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
}