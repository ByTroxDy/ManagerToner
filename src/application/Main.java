package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {			
			// Cargar y mostrar la ventana de inicio de sesión
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/Login.fxml"));
			Parent root = loader.load();
			primaryStage.setScene(new Scene(root));
			
//			Scene scene = new Scene(root);

            // Cargar el archivo CSS y aplicarlo a la escena
//            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
//            
//            primaryStage.setScene(scene);
			
			// Crear el icono de la ventana
	        Image icon = new Image(getClass().getResourceAsStream("/icon/mbd_logo.png"));
	        primaryStage.getIcons().add(icon);
	        
	        primaryStage.setTitle("MBD - Iniciar Sesión");
	        primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
