package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.ConexionDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ClienteSesion;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Agregar un evento de clic al botón de inicio de sesión
        loginButton.setOnAction(event -> login());
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Consulta SQL para buscar el usuario en la base de datos
        String query = "SELECT rol FROM usuarios WHERE usuario = ? AND password = ?";
        
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                if (rol.equals("cliente")) {
                    // Cuando el cliente inicia sesión correctamente
                    ClienteSesion.setNieCliente(username);
                    try {
                        // Cargar la interfaz del panel de cliente desde el archivo FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/ClientPanel.fxml"));
                        Parent root = loader.load();
                        
                        // Crear una nueva escena
                        Scene scene = new Scene(root);
                        
                        // Obtener el escenario principal y establecer la nueva escena
                        Stage clientStage = new Stage();
                        clientStage.setScene(scene);
                        clientStage.setTitle("Panel de Cliente");
                        
                        // Mostrar el panel de cliente
                        clientStage.setResizable(false);
                        clientStage.show();
                        
                        // Cerrar la ventana actual (ventana de inicio de sesión)
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (rol.equals("empleado")) {
                    try {
                        // Cargar la interfaz del panel de empleado desde el archivo FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/EmployeePanel.fxml"));
                        Parent root = loader.load();
                        
                        // Crear una nueva escena
                        Scene scene = new Scene(root);
                        
                        // Obtener el escenario principal y establecer la nueva escena
                        Stage employeeStage = new Stage();
                        employeeStage.setScene(scene);
                        employeeStage.setTitle("Panel de Empleado");
                        
                        // Mostrar el panel de empleado
                        employeeStage.setResizable(false);
                        employeeStage.show();
                        
                        // Cerrar la ventana actual (ventana de inicio de sesión)
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                // Mostrar mensaje de error si las credenciales son inválidas
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de inicio de sesión");
                alert.setHeaderText(null);
                alert.setContentText("Credenciales inválidas. Por favor, inténtalo de nuevo.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
