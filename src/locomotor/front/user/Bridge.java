package locomotor.front.user;

import com.eclipsesource.json.JsonObject;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import locomotor.front.components.network.ClientRequest;

public class Bridge {
	WebView _view;
	Stage _stage;

	public Bridge(WebView view, Stage stage) {
		_view = view;
		_stage = stage;

		view.getEngine().executeScript("console.log = function(message){app.log(message);};");

		view.getEngine().executeScript("window.initWhenReady = true;"); // say to the client that everything is ready
	}

	public void log(String text) {
		System.out.println(text);
	}

	public void setTitle(String title) {
		_stage.setTitle("Locomotor - " + title);
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
}