package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.ConexionDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.ClienteSesion;
import model.Impresora;

public class ClientPanelController implements Initializable {

    @FXML
    private TableView<Impresora> printerTableView;

    @FXML
    private TableColumn<Impresora, Integer> idImpresoraColumn;

    @FXML
    private TableColumn<Impresora, String> nombreColumn;

    @FXML
    private TableColumn<Impresora, String> modeloColumn;

    @FXML
    private TableColumn<Impresora, String> tipoTintaColumn;

    @FXML
    private Button solicitarCartuchoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar las columnas de la tabla
        initTableColumns();
        // Cargar los datos de las impresoras en la tabla
        loadImpresoras();
    }

    private void initTableColumns() {
        idImpresoraColumn.setCellValueFactory(cellData -> cellData.getValue().idImpresoraProperty().asObject());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        modeloColumn.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        tipoTintaColumn.setCellValueFactory(cellData -> cellData.getValue().tipoTintaProperty());
    }

    private void loadImpresoras() {
        // Consulta SQL para obtener los datos de las impresoras del cliente actual (puedes ajustarlo según sea necesario)
        String query = "SELECT * FROM impresorasclientes WHERE nieCliente = ?";

        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
        	// Utiliza el NIE del cliente almacenado en ClienteSesion
            pstmt.setString(1, ClienteSesion.getNieCliente());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Impresora impresora = new Impresora(
                        rs.getInt("idImpresora"),
                        rs.getString("nombreImpresora"),
                        rs.getString("modeloImpresora"),
                        rs.getString("tipoTinta")
                );
                printerTableView.getItems().add(impresora);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void solicitarCartucho() {
        // Obtener la impresora seleccionada por el cliente
        Impresora selectedPrinter = printerTableView.getSelectionModel().getSelectedItem();
        if (selectedPrinter == null) {
            // Si no se selecciona ninguna impresora, mostrar un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona una impresora antes de solicitar el cartucho.");
            alert.showAndWait();
            return;
        }

        // Pedir al cliente que ingrese la cantidad de cartuchos deseados
        TextInputDialog cantidadDialog = new TextInputDialog("1");
        cantidadDialog.setTitle("Solicitud de Cartucho");
        cantidadDialog.setHeaderText("Especifica la cantidad de cartuchos");
        cantidadDialog.setContentText("Cantidad:");
        cantidadDialog.showAndWait().ifPresent(cantidadStr -> {
            try {
                int cantidad = Integer.parseInt(cantidadStr);

                // Pedir al cliente que seleccione el tipo de tinta deseado
                ChoiceDialog<String> tipoTintaDialog = new ChoiceDialog<>("Negro", "Color", "Cyan", "Magenta", "Amarillo");
                tipoTintaDialog.setTitle("Solicitud de Cartucho");
                tipoTintaDialog.setHeaderText("Especifica el tipo de tinta");
                tipoTintaDialog.setContentText("Tipo de Tinta:");
                tipoTintaDialog.showAndWait().ifPresent(tipoTinta -> {
                    // Consulta SQL para insertar la solicitud del cartucho en la tabla de pedidos
                    String query = "INSERT INTO pedidos (nieCliente, tipoTinta, cantidad) VALUES (?, ?, ?)";

                    try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
                    	// Utiliza el NIE del cliente almacenado en ClienteSesion
                        pstmt.setString(1, ClienteSesion.getNieCliente());
                        pstmt.setString(2, tipoTinta);
                        pstmt.setInt(3, cantidad);
                        pstmt.executeUpdate();

                        // Mostrar un mensaje de confirmación de la solicitud
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("INFORMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Solicitud de cartucho enviada correctamente.");
                        alert.showAndWait();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    
                    // Incrementa el contador de adquisiciones en la tabla de clientes
                    String updateQuery = "UPDATE clientes SET adquiridos = adquiridos + ? WHERE nie = ?";
                    try (PreparedStatement updateStmt = ConexionDB.getConnection().prepareStatement(updateQuery)) {
                        // Utiliza el NIE del cliente almacenado en ClienteSesion
                    	updateStmt.setInt(1, cantidad);
                        updateStmt.setString(2, ClienteSesion.getNieCliente());
                        int rowsAffected = updateStmt.executeUpdate();
                        if (rowsAffected == 1) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("INFORMATION");
                            alert.setHeaderText(null);
                            alert.setContentText("Contador de adquisiciones actualizado correctamente para el cliente: " + ClienteSesion.getNieCliente());
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("WARNING");
                            alert.setHeaderText(null);
                            alert.setContentText("No se pudo actualizar el contador de adquisiciones para el cliente: " + ClienteSesion.getNieCliente());
                            alert.showAndWait();
                        }
                    } catch (SQLException e) {
                        // Manejar cualquier excepción SQL
                        e.printStackTrace();
                    }

                });
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("Ingresa un número válido para la cantidad de cartuchos.");
                alert.showAndWait();
            }
        });
    }
    
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
    
}
