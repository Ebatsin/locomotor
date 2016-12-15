package locomotor.front.user;

import com.eclipsesource.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.Class;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import locomotor.front.components.network.BinaryObject;
import locomotor.front.components.network.ClientRequest;
import locomotor.front.components.network.FileUpload;

public class Main extends Application {

	public static void main(String[] args) throws Exception {
		// System.setProperty("prism.lcdtext", "false"); // enhance fonts		
		// launch(args);

		System.setProperty("javax.net.ssl.trustStore", "clientKey.pfx");
		System.setProperty("javax.net.ssl.trustStorePassword","motdepasse");


		System.out.println("Tentative de requête");

		ClientRequest cr = new ClientRequest("https://localhost:8000/");
		cr.addParam("idParam1", "le contenu de mon paramètre");
		cr.addParam("test1", "Un petit et esthétique texte permettant de voir si le contenu arrive bien à destination");
		cr.requestJson("api/test").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				System.out.println(obj.toString());
			}
		});

		ClientRequest cr2 = new ClientRequest("https://localhost:8000/");
		cr2.addParam("name", "chat.png");
		cr2.requestBinary("img/get").thenAccept(new Consumer<BinaryObject>() {
			public void accept(BinaryObject obj) {
				if(obj.isSuccess()) {
					ByteArrayOutputStream img = obj.getAsBinary();
					try {
						OutputStream outputStream = new FileOutputStream("resources/user/images/cat" + obj.guessFileExtension());
						img.writeTo(outputStream);
					}
					catch(Exception exception) {
						System.out.println("unable to write the file");
					}
				}
				else {
					System.out.println("erreur : " + obj.getErrorMessage());
				}
				System.exit(0);
			}
		});



		ClientRequest cr3 = new ClientRequest("https://localhost:8000/");
		cr3.addParam("name", "firefox.jpg");
		cr3.requestBinary("img/get").thenAccept(new Consumer<BinaryObject>() {
			public void accept(BinaryObject obj) {
				if(obj.isSuccess()) {
					ByteArrayOutputStream img = obj.getAsBinary();
					try {
						OutputStream outputStream = new FileOutputStream("resources/user/images/firefox" + obj.guessFileExtension());
						img.writeTo(outputStream);
					}
					catch(Exception exception) {
						System.out.println("unable to write the file");
					}
				}
				else {
					System.out.println("erreur : " + obj.getErrorMessage());
				}
				System.exit(0);
			}
		});

		while(true) {} // wait for the request to be sent and the response read
	}

	@Override
	public void start(Stage primaryStage) {
		
		String _page = "/index.html";
		
		WebView webView = createWebView(primaryStage, _page);
		
		primaryStage.setScene(new Scene(webView));
		primaryStage.setTitle("Locomotor");		
		primaryStage.show();
	}

	private WebView createWebView(Stage primaryStage, String page) {
		
		// create the JavaFX webview
		final WebView webView = new WebView();
		
		// load html page
		webView.getEngine().load(getClass().getResource(page).toExternalForm());

		return webView;
	}
}
