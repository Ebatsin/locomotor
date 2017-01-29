package locomotor.front.user;

import javafx.scene.web.WebView;

public class Bridge {
	WebView _view;

	public Bridge(WebView view) {
		_view = view;

		view.getEngine().executeScript("console.log = function(message){app.log(message);};");

		view.getEngine().executeScript("window.initWhenReady = true;"); // say to the client that everything is ready
	}

	public void log(String text) {
		System.out.println(text);
	}
}