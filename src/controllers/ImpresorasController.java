package controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import database.ConexionDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Impresora;

public class ImpresorasController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar las columnas de la tabla
        initTableColumns();
        // Cargar los datos de las impresoras en la tabla
        loadImpresoras();
    }

    private void initTableColumns() {
        idImpresoraColumn.setCellValueFactory(cellData -> cellData.getValue().idImpresoraProperty().asObject());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        modeloColumn.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        tipoTintaColumn.setCellValueFactory(cellData -> cellData.getValue().tipoTintaProperty());
    }

    private void loadImpresoras() {
        // Consulta SQL para obtener los datos de las impresoras (puedes ajustarlo según sea necesario)
        String query = "SELECT * FROM impresoras";

        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
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
    private void cerrar() {
    	// Cerrar la ventana actual
        Stage stage = (Stage) printerTableView.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void agregarImpresora() {
        // Mostrar un diálogo para que el empleado ingrese los datos de la nueva impresora
        Dialog<Impresora> dialog = new Dialog<>();
        dialog.setTitle("Agregar Impresora");
        dialog.setHeaderText("Ingresa los datos de la nueva impresora");

        // Setear los botones de OK y Cancelar en el diálogo
        ButtonType buttonTypeOk = new ButtonType("Aceptar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        // Crear campos de texto para que el empleado ingrese los datos de la nueva impresora
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        TextField modeloField = new TextField();
        modeloField.setPromptText("Modelo");
        TextField tipoTintaField = new TextField();
        tipoTintaField.setPromptText("Tipo de Tinta");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(new Label("Modelo:"), 0, 1);
        grid.add(modeloField, 1, 1);
        grid.add(new Label("Tipo de Tinta:"), 0, 2);
        grid.add(tipoTintaField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convertir el resultado del diálogo a un objeto Impresora cuando se presiona OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return new Impresora(nombreField.getText(), modeloField.getText(), tipoTintaField.getText());
            }
            return null;
        });

        // Mostrar el diálogo y procesar el resultado cuando se presiona OK
        Optional<Impresora> result = dialog.showAndWait();
        result.ifPresent(impresora -> {
            // Llamar al método agregarImpresora con los datos de la nueva impresora
        	agregarImpresora(impresora.getNombre(), impresora.getModelo(), impresora.getTipoTinta());
        });
        
        // Vaciar los datos en las tablas
        printerTableView.getItems().clear();
        
        // Cargar los datos en las tablas
        loadImpresoras();
    }
    
    public void agregarImpresora(String nombre, String modelo, String tipoTinta) {
        String query = "INSERT INTO impresoras (nombreImpresora, modeloImpresora, tipoTinta) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, modelo);
            pstmt.setString(3, tipoTinta);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Impresora agregada correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo agregar la impresora.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modificarImpresora() {
        // Mostrar un diálogo para que el empleado seleccione la impresora a modificar
        Impresora selectedImpresora = printerTableView.getSelectionModel().getSelectedItem();
        if (selectedImpresora == null) {
            // Si no se selecciona ninguna impresora, mostrar un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona una impresora antes de modificar.");
            alert.showAndWait();
            return;
        }

        // Mostrar un diálogo similar al de agregarImpresora pero con los datos de la impresora seleccionado prellenados
        Dialog<Impresora> dialog = new Dialog<>();
        dialog.setTitle("Modificar Impresora");
        dialog.setHeaderText("Modifica los datos de la impresora seleccionado");

        // Setear los botones de OK y Cancelar en el diálogo
        ButtonType buttonTypeOk = new ButtonType("Aceptar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        // Crear campos de texto prellenados con los datos de la impresora seleccionado
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idImpresoraField = new TextField(String.valueOf(selectedImpresora.getIdImpresora()));
        idImpresoraField.setEditable(false);
        TextField nombreField = new TextField(selectedImpresora.getNombre());
        TextField modeloField = new TextField(selectedImpresora.getModelo());
        TextField tipoTintaField = new TextField(selectedImpresora.getTipoTinta());;

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idImpresoraField, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(new Label("Modelo:"), 0, 2);
        grid.add(modeloField, 1, 2);
        grid.add(new Label("Tipo de Tinta:"), 0, 3);
        grid.add(tipoTintaField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convertir el resultado del diálogo a un objeto Impresora cuando se presiona OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return new Impresora(Integer.parseInt(idImpresoraField.getText()), nombreField.getText(), modeloField.getText(), tipoTintaField.getText());
            }
            return null;
        });

        // Mostrar el diálogo y procesar el resultado cuando se presiona OK
        Optional<Impresora> result = dialog.showAndWait();
        result.ifPresent(impresora -> {
            // Llamar al método modificarImpresora con los datos de la impresora modificado
        	modificarImpresora(selectedImpresora.getIdImpresora(), impresora.getNombre(), impresora.getModelo(), impresora.getTipoTinta());
        });
        
        // Vaciar los datos en las tablas
        printerTableView.getItems().clear();
        
        // Cargar los datos en las tablas
        loadImpresoras();
    }
    
    public void modificarImpresora(int idImpresora, String nuevoNombre, String nuevoModelo, String nuevoTipoTinta) {
        String query = "UPDATE impresoras SET nombreImpresora = ?, modeloImpresora = ?, tipoTinta = ? WHERE idImpresora = ?";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nuevoNombre);
            pstmt.setString(2, nuevoModelo);
            pstmt.setString(3, nuevoTipoTinta);
            pstmt.setInt(4, idImpresora);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Impresora modificado correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo modificar la impresora.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarImpresora() {
        // Mostrar un diálogo para que el empleado seleccione la impresora a eliminar
        Impresora selectedImpresora = printerTableView.getSelectionModel().getSelectedItem();
        if (selectedImpresora == null) {
            // Si no se selecciona ninguna impresora, mostrar un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona una impresora antes de eliminar.");
            alert.showAndWait();
            return;
        }

        // Mostrar un diálogo de confirmación para confirmar la eliminación de la impresora seleccionado
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Estás seguro de que quieres eliminar esta impresora?");
        alert.setContentText("Nombre: " + selectedImpresora.getNombre() + "\nModelo: " + selectedImpresora.getModelo());

        // Obtener el resultado del diálogo de confirmación y eliminar la impresora si se confirma
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Llamar al método eliminarImpresora con la id de la impresora seleccionado
            eliminarImpresora(selectedImpresora.getIdImpresora());
        }
        
        // Vaciar los datos en las tablas
        printerTableView.getItems().clear();
        
        // Cargar los datos en las tablas
        loadImpresoras();
    }

    public void eliminarImpresora(int idImpresora) {
        String query = "DELETE FROM impresoras WHERE idImpresora = ?";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, idImpresora);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Impresora eliminada correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo eliminar la impresora.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
