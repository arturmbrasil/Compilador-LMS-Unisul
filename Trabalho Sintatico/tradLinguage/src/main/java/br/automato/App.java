package br.automato;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class App extends Application {
    
	@Override
	public void start(Stage primaryStage) {
		try {
//			Thread.currentThread().getContextClassLoader().getResource("Principal.fxml");
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/automato/controller/MenuPrincipal.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/br/automato/controller/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.setTitle("Automato");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
