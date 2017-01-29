package locomotor.front.user;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.Class;
import java.nio.file.Files;
import java.nio.file.Paths;
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

		// ClientRequest cr = new ClientRequest("https://localhost:8000/");
		// cr.addParam("username", "test");
		// cr.addParam("password", "motdepasse");
		// cr.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString());	
		// 	}
		// });

		// ClientRequest cr2 = new ClientRequest("https://localhost:8000/");
		// cr2.addParam("username", "test");
		// cr2.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println("pas de password");	
		// 		System.out.println(obj.toString());	
		// 	}
		// });

		// ClientRequest cr3 = new ClientRequest("https://localhost:8000/");
		// cr3.addParam("password", "motdepasse");
		// cr3.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println("pas de username");		
		// 		System.out.println(obj.toString());	
		// 	}
		// });

		// ClientRequest cr4 = new ClientRequest("https://localhost:8000/");
		// cr4.addParam("username", "test");
		// cr4.addParam("password", "efvwsdvs");
		// cr4.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println("invalid password");		
		// 		System.out.println(obj.toString());	
		// 	}
		// });

		// ClientRequest cr5 = new ClientRequest("https://localhost:8000/");
		// cr5.addParam("token", "pouet");
		// cr5.requestJson("api/user/auth").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println("invalid token");		
		// 		System.out.println(obj.toString());	
		// 	}
		// });

		// ClientRequest cr6 = new ClientRequest("https://localhost:8000/");
		// // this is the long term token for (test:motdepasse)
		// String longTerm = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1ODRkODI1NjU3YWI5ZTQ5Yzg3ODFiNjAiLCJpc3MiOiJMb2NvbW90b3JTZXJ2ZXIiLCJpYXQiOjE0ODQyNDQ2NzYsInJvbGUiOmZhbHNlLCJleHAiOjE0ODk0Mjg2NzZ9.WmPntGSZ_gOODZ-hCtXDvp015buI9lVxlNVkdLnvXf4mBIt87iIotgvLcJRzAViPXSk8BZ0_gZBzlyp0GwWMLw";
		// cr6.addParam("token", longTerm);
		// cr6.requestJson("api/model/get").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString(WriterConfig.PRETTY_PRINT));
		// 	}
		// });

		// ClientRequest cr7 = new ClientRequest("https://localhost:8000/");
		// // this is the long term token for (test:motdepasse)
		// String longTerm = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1ODRkODI1NjU3YWI5ZTQ5Yzg3ODFiNjAiLCJpc3MiOiJMb2NvbW90b3JTZXJ2ZXIiLCJpYXQiOjE0ODQyNDQ2NzYsInJvbGUiOmZhbHNlLCJleHAiOjE0ODk0Mjg2NzZ9.WmPntGSZ_gOODZ-hCtXDvp015buI9lVxlNVkdLnvXf4mBIt87iIotgvLcJRzAViPXSk8BZ0_gZBzlyp0GwWMLw";
		// cr7.addParam("token", longTerm);
		// String content = new String(Files.readAllBytes(Paths.get("userSelection.json")));
		// cr7.addParam("criterias", content);
		// cr7.requestJson("api/search").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString(WriterConfig.PRETTY_PRINT));
		// 	}
		// });
		
		// ClientRequest cr7 = new ClientRequest("https://localhost:8000/");
		// // this is the long term token for (test:motdepasse)
		// String longTerm = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1ODRkODI1NjU3YWI5ZTQ5Yzg3ODFiNjAiLCJpc3MiOiJMb2NvbW90b3JTZXJ2ZXIiLCJpYXQiOjE0ODQyNDQ2NzYsInJvbGUiOmZhbHNlLCJleHAiOjE0ODk0Mjg2NzZ9.WmPntGSZ_gOODZ-hCtXDvp015buI9lVxlNVkdLnvXf4mBIt87iIotgvLcJRzAViPXSk8BZ0_gZBzlyp0GwWMLw";
		// cr7.addParam("token", longTerm);
		// cr7.addParam("id", "584336b157ab9e1521a8bd9f"); // ok
		// cr7.requestJson("api/universe/get").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString(WriterConfig.PRETTY_PRINT));
		// 	}
		// });

		// ClientRequest cr8 = new ClientRequest("https://localhost:8000/");
		// // this is the long term token for (test:motdepasse)
		// String longTerm = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1ODRkODI1NjU3YWI5ZTQ5Yzg3ODFiNjAiLCJpc3MiOiJMb2NvbW90b3JTZXJ2ZXIiLCJpYXQiOjE0ODQyNDQ2NzYsInJvbGUiOmZhbHNlLCJleHAiOjE0ODk0Mjg2NzZ9.WmPntGSZ_gOODZ-hCtXDvp015buI9lVxlNVkdLnvXf4mBIt87iIotgvLcJRzAViPXSk8BZ0_gZBzlyp0GwWMLw";
		// cr8.addParam("token", longTerm);
		// cr8.addParam("id", "58433aa157ab9e1f6f26334b"); // not ok
		// cr8.requestJson("api/universe/get").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString(WriterConfig.PRETTY_PRINT));
		// 	}
		// });

		ClientRequest cr8 = new ClientRequest("https://localhost:8000/");
		// this is the long term token for (test:motdepasse)
		String longTerm = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1ODRkODI1NjU3YWI5ZTQ5Yzg3ODFiNjAiLCJpc3MiOiJMb2NvbW90b3JTZXJ2ZXIiLCJpYXQiOjE0ODQyNDQ2NzYsInJvbGUiOmZhbHNlLCJleHAiOjE0ODk0Mjg2NzZ9.WmPntGSZ_gOODZ-hCtXDvp015buI9lVxlNVkdLnvXf4mBIt87iIotgvLcJRzAViPXSk8BZ0_gZBzlyp0GwWMLw";
		cr8.addParam("token", longTerm);
		cr8.addParam("id", "58433aa157ab9e1f6f26334b"); // ok
		cr8.requestJson("api/item/get").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				System.out.println(obj.toString(WriterConfig.PRETTY_PRINT));
			}
		});

		ClientRequest cr9 = new ClientRequest("https://localhost:8000/");
		// this is the long term token for (test:motdepasse)
		longTerm = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1ODRkODI1NjU3YWI5ZTQ5Yzg3ODFiNjAiLCJpc3MiOiJMb2NvbW90b3JTZXJ2ZXIiLCJpYXQiOjE0ODQyNDQ2NzYsInJvbGUiOmZhbHNlLCJleHAiOjE0ODk0Mjg2NzZ9.WmPntGSZ_gOODZ-hCtXDvp015buI9lVxlNVkdLnvXf4mBIt87iIotgvLcJRzAViPXSk8BZ0_gZBzlyp0GwWMLw";
		cr9.addParam("token", longTerm);
		cr9.addParam("id", "58433aa157ab9e1f6f26334z"); // not ok
		cr9.requestJson("api/item/get").thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				System.out.println(obj.toString(WriterConfig.PRETTY_PRINT));
			}
		});

		// ClientRequest cr7 = new ClientRequest("https://localhost:8000/");
		// cr7.addParam("username", "test");
		// cr7.addParam("password", "motdepasse");
		// cr7.requestJson("api/user/register").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString());
		// 	}
		// });

		// ClientRequest cr8 = new ClientRequest("https://localhost:8000/");
		// cr8.addParam("username", "test3");
		// cr8.addParam("password", "motdepasse");
		// cr8.requestJson("api/user/register").thenAccept(new Consumer<JsonObject>() {
		// 	public void accept(JsonObject obj) {
		// 		System.out.println(obj.toString());
		// 	}
		// });

		// ClientRequest cr7 = new ClientRequest("https://localhost:8000/");
		// cr7.addParam("name", "chat.png");
		// cr7.requestBinary("img/get").thenAccept(new Consumer<BinaryObject>() {
		// 	public void accept(BinaryObject obj) {
		// 		if(obj.isSuccess()) {
		// 			ByteArrayOutputStream img = obj.getAsBinary();
		// 			try {
		// 				OutputStream outputStream = new FileOutputStream("resources/user/images/cat" + obj.guessFileExtension());
		// 				img.writeTo(outputStream);
		// 			}
		// 			catch(Exception exception) {
		// 				System.out.println("unable to write the file");
		// 			}
		// 		}
		// 		else {
		// 			System.out.println("erreur : " + obj.getErrorMessage());
		// 		}
		// 	}
		// });

		// ClientRequest cr8 = new ClientRequest("https://localhost:8000/");
		// cr8.addParam("name", "firefox.jpg");
		// cr8.requestBinary("img/get").thenAccept(new Consumer<BinaryObject>() {
		// 	public void accept(BinaryObject obj) {
		// 		if(obj.isSuccess()) {
		// 			ByteArrayOutputStream img = obj.getAsBinary();
		// 			try {
		// 				OutputStream outputStream = new FileOutputStream("resources/user/images/firefox" + obj.guessFileExtension());
		// 				img.writeTo(outputStream);
		// 			}
		// 			catch(Exception exception) {
		// 				System.out.println("unable to write the file");
		// 			}
		// 		}
		// 		else {
		// 			System.out.println("erreur : " + obj.getErrorMessage());
		// 		}
		// 	}
		// });
		
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
