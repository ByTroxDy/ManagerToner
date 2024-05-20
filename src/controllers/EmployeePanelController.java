package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EmployeePanelController {

	@FXML
	private void cerrarSesion(ActionEvent e) throws IOException {
		// Cargar y mostrar la ventana de inicio de sesión
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/Login.fxml"));
		Parent root = loader.load();
		
		// Obtener el escenario principal y establecer la nueva escena
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
		
		// Crear el icono de la ventana
        Image icon = new Image(getClass().getResourceAsStream("/icon/mbd_logo.png"));
        stage.getIcons().add(icon);
        
        stage.setTitle("MBD - Iniciar Sesión");
        stage.setResizable(false);
        stage.show();
		
		cerrarVentana(e);
	}

	public void cerrarVentana(ActionEvent e) {
		Node source = (Node) e.getSource(); // Me devuelve el elemento al que hice click
		Stage stage = (Stage) source.getScene().getWindow(); // Me devuelve la ventana donde se encuentra el elemento
		stage.close(); // Me cierra la ventana
	}

	@FXML
	private void verClientes() throws IOException {
		// Cargar la interfaz del panel de clientes desde el archivo FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/ClientesPanel.fxml"));
		Parent root = loader.load();

		// Crear una nueva escena
		Scene scene = new Scene(root);

		// Obtener el escenario principal y establecer la nueva escena
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Tabla clientes");

		// Mostrar el panel de clientes
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	private void verPedidos() throws IOException {
		// Cargar la interfaz del panel de pedidos desde el archivo FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/PedidosPanel.fxml"));
		Parent root = loader.load();

		// Crear una nueva escena
		Scene scene = new Scene(root);

		// Obtener el escenario principal y establecer la nueva escena
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Tabla pedidos");

		// Mostrar el panel de pedidos
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	private void verImpresoras() throws IOException {
		// Cargar la interfaz del panel de impresoras desde el archivo FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/ImpresorasPanel.fxml"));
		Parent root = loader.load();

		// Crear una nueva escena
		Scene scene = new Scene(root);

		// Obtener el escenario principal y establecer la nueva escena
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Tabla impresoras");

		// Mostrar el panel de impresoras
		stage.setResizable(false);
		stage.show();
	}

}
