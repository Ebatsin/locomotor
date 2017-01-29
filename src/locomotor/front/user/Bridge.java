package locomotor.front.user;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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
	}
}