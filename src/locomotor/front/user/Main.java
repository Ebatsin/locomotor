package locomotor.front.user;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.Class;
import java.util.function.Consumer;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import locomotor.front.components.network.BinaryObject;
import locomotor.front.components.network.ClientRequest;
import locomotor.front.components.network.FileUpload;

import netscape.javascript.JSObject;

public class Main extends Application {

	public static void main(String[] args) throws Exception {


		System.setProperty("prism.lcdtext", "false"); // enhance fonts		
		launch(args);

		System.setProperty("javax.net.ssl.trustStore", "clientKey.pfx");
		System.setProperty("javax.net.ssl.trustStorePassword","motdepasse");
	}

	@Override
	public void start(Stage primaryStage) {
		String page = "/index.html";

		List<String> params = getParameters().getRaw();
		if(!params.isEmpty()) {
			if(params.get(0).equals("--admin")) {
				page = "/index-admin.html";
			}
		}
		
		WebView webView = createWebView(primaryStage, page);
		webView.setContextMenuEnabled(false);
		
		primaryStage.setScene(new Scene(webView));
		primaryStage.setTitle("Locomotor");	
		primaryStage.setMaximized(true);

		primaryStage.setMinWidth(680); // absolutely random value based on the look
		primaryStage.setMinHeight(418); // absolutely not a random value. pixel perfect height of the 6 sidebar menu items

		primaryStage.show();
	}

	private WebView createWebView(Stage primaryStage, String page) {
		
		// create the JavaFX webview
		final WebView webView = new WebView();
		
		// load html page
		webView.getEngine().load(getClass().getResource(page).toExternalForm());

		JSObject win = (JSObject)webView.getEngine().executeScript("window");
		win.setMember("app", new Bridge(webView, primaryStage));

		return webView;
	}
}
