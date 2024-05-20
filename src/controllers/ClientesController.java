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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Cliente;
import model.Usuario;

public class ClientesController implements Initializable {

    @FXML
    private TableView<Cliente> clientTableView;

    @FXML
    private TableColumn<Cliente, String> nieColumn;

    @FXML
    private TableColumn<Cliente, String> nombreColumn;

    @FXML
    private TableColumn<Cliente, String> direccionColumn;

    @FXML
    private TableColumn<Cliente, String> telefonoColumn;

    @FXML
    private TableColumn<Cliente, Integer> adquiridoColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar las columnas de las tablas
        initClientTableColumns();
        // Cargar los datos en las tablas
        loadClientes();
    }

    private void initClientTableColumns() {
        nieColumn.setCellValueFactory(cellData -> cellData.getValue().nieProperty());
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        direccionColumn.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());
        telefonoColumn.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        adquiridoColumn.setCellValueFactory(cellData -> cellData.getValue().adquiridoProperty().asObject());
    }

    private void loadClientes() {
        // Consulta SQL para obtener los datos de los clientes
        String query = "SELECT * FROM clientes";

        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nie"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getInt("adquirido")
                );
                clientTableView.getItems().add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void cerrar() {
    	// Cerrar la ventana actual
        Stage stage = (Stage) clientTableView.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void agregarCliente() {
        // Mostrar un diálogo para que el empleado ingrese los datos del nuevo usuario
        Dialog<Usuario> dialogUsuario = new Dialog<>();
        dialogUsuario.setTitle("Agregar Usuario");
        dialogUsuario.setHeaderText("Ingresa los datos del nuevo usuario");

        // Setear los botones de OK y Cancelar en el diálogo
        ButtonType buttonTypeOkUsuario = new ButtonType("Aceptar", ButtonData.OK_DONE);
        dialogUsuario.getDialogPane().getButtonTypes().addAll(buttonTypeOkUsuario, ButtonType.CANCEL);

        // Crear campos de texto para que el empleado ingrese los datos del nuevo usuario
        GridPane gridUsuario = new GridPane();
        gridUsuario.setHgap(10);
        gridUsuario.setVgap(10);
        gridUsuario.setPadding(new Insets(20, 150, 10, 10));

        TextField usuarioField = new TextField();
        usuarioField.setPromptText("Nombre de usuario");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Contraseña");
        TextField groupField = new TextField("cliente");

        gridUsuario.add(new Label("Usuario:"), 0, 0);
        gridUsuario.add(usuarioField, 1, 0);
        gridUsuario.add(new Label("Contraseña:"), 0, 1);
        gridUsuario.add(passwordField, 1, 1);

        dialogUsuario.getDialogPane().setContent(gridUsuario);

        // Convertir el resultado del diálogo a un objeto Usuario cuando se presiona OK
        dialogUsuario.setResultConverter(dialogButtonUsuario -> {
            if (dialogButtonUsuario == buttonTypeOkUsuario) {
                return new Usuario(usuarioField.getText(), passwordField.getText(), groupField.getText());
            }
            return null;
        });

        // Mostrar el diálogo y procesar el resultado cuando se presiona OK
        Optional<Usuario> resultUsuario = dialogUsuario.showAndWait();
        resultUsuario.ifPresent(usuario -> {
            // Llamar al método agregarUsuario con los datos del nuevo usuario
            agregarUsuario(usuario.getUsuario(), usuario.getPassword());

            // ---------------------------------------------------------------------------------------------------------- //

            // Mostrar un diálogo para que el empleado ingrese los datos del nuevo cliente
            Dialog<Cliente> dialogCliente = new Dialog<>();
            dialogCliente.setTitle("Agregar Cliente");
            dialogCliente.setHeaderText("Ingresa los datos del nuevo cliente");

            // Setear los botones de OK y Cancelar en el diálogo
            ButtonType buttonTypeOkCliente = new ButtonType("Aceptar", ButtonData.OK_DONE);
            dialogCliente.getDialogPane().getButtonTypes().addAll(buttonTypeOkCliente, ButtonType.CANCEL);

            // Crear campos de texto para que el empleado ingrese los datos del nuevo cliente
            GridPane gridCliente = new GridPane();
            gridCliente.setHgap(10);
            gridCliente.setVgap(10);
            gridCliente.setPadding(new Insets(20, 150, 10, 10));

            TextField nieField = new TextField(usuarioField.getText());
            nieField.setEditable(false);
            TextField nombreField = new TextField();
            nombreField.setPromptText("Nombre");
            TextField direccionField = new TextField();
            direccionField.setPromptText("Dirección");
            TextField telefonoField = new TextField();
            telefonoField.setPromptText("Teléfono");
            TextField adquiridoField = new TextField("0");
            adquiridoField.setPromptText("Adquirido");

            gridCliente.add(new Label("NIE:"), 0, 0);
            gridCliente.add(nieField, 1, 0);
            gridCliente.add(new Label("Nombre:"), 0, 1);
            gridCliente.add(nombreField, 1, 1);
            gridCliente.add(new Label("Dirección:"), 0, 2);
            gridCliente.add(direccionField, 1, 2);
            gridCliente.add(new Label("Teléfono:"), 0, 3);
            gridCliente.add(telefonoField, 1, 3);
            gridCliente.add(new Label("Adquirido:"), 0, 4);
            gridCliente.add(adquiridoField, 1, 4);

            dialogCliente.getDialogPane().setContent(gridCliente);

            // Convertir el resultado del diálogo a un objeto Cliente cuando se presiona OK
            dialogCliente.setResultConverter(dialogButtonCliente -> {
                if (dialogButtonCliente == buttonTypeOkCliente) {
                    return new Cliente(nieField.getText(), nombreField.getText(), direccionField.getText(), telefonoField.getText(), Integer.parseInt(adquiridoField.getText()));
                }
                return null;
            });

            // Mostrar el diálogo y procesar el resultado cuando se presiona OK
            Optional<Cliente> resultCliente = dialogCliente.showAndWait();
            resultCliente.ifPresent(cliente -> {
                // Llamar al método agregarCliente con los datos del nuevo cliente
                agregarCliente(cliente.getNie(), cliente.getNombre(), cliente.getDireccion(), cliente.getTelefono(), cliente.getAdquirido());
            });
        });
        
        // Vaciar los datos en las tablas
        clientTableView.getItems().clear();
        
        // Cargar los datos en las tablas
        loadClientes();
    }
    
    public void agregarUsuario(String usuario, String password) {
    	String query = "INSERT INTO usuarios (usuario, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Usuario agregado correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo agregar el usuario.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void agregarCliente(String nie, String nombre, String direccion, String telefono, int adquirido) {
        String query = "INSERT INTO clientes (nie, nombre, direccion, telefono, adquirido) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nie);
            pstmt.setString(2, nombre);
            pstmt.setString(3, direccion);
            pstmt.setString(4, telefono);
            pstmt.setInt(5, adquirido);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Cliente agregado correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo agregar el cliente.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modificarCliente() {
        // Mostrar un diálogo para que el empleado seleccione el cliente a modificar
        Cliente selectedCliente = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedCliente == null) {
            // Si no se selecciona ningún cliente, mostrar un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona un cliente antes de modificar.");
            alert.showAndWait();
            return;
        }

        // Mostrar un diálogo similar al de agregarCliente pero con los datos del cliente seleccionado prellenados
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle("Modificar Cliente");
        dialog.setHeaderText("Modifica los datos del cliente seleccionado");

        // Setear los botones de OK y Cancelar en el diálogo
        ButtonType buttonTypeOk = new ButtonType("Aceptar", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        // Crear campos de texto prellenados con los datos del cliente seleccionado
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nieField = new TextField(selectedCliente.getNie());
        TextField nombreField = new TextField(selectedCliente.getNombre());
        TextField direccionField = new TextField(selectedCliente.getDireccion());
        TextField telefonoField = new TextField(selectedCliente.getTelefono());
        TextField adquiridoField = new TextField(String.valueOf(selectedCliente.getAdquirido()));

        grid.add(new Label("NIE:"), 0, 0);
        grid.add(nieField, 1, 0);
        grid.add(new Label("Nombre:"), 0, 1);
        grid.add(nombreField, 1, 1);
        grid.add(new Label("Dirección:"), 0, 2);
        grid.add(direccionField, 1, 2);
        grid.add(new Label("Teléfono:"), 0, 3);
        grid.add(telefonoField, 1, 3);
        grid.add(new Label("Adquirido:"), 0, 4);
        grid.add(adquiridoField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir el resultado del diálogo a un objeto Cliente cuando se presiona OK
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                return new Cliente(nieField.getText(), nombreField.getText(), direccionField.getText(), telefonoField.getText(), Integer.parseInt(adquiridoField.getText()));
            }
            return null;
        });

        // Mostrar el diálogo y procesar el resultado cuando se presiona OK
        Optional<Cliente> result = dialog.showAndWait();
        result.ifPresent(cliente -> {
            // Llamar al método modificarCliente con los datos del cliente modificado
            modificarCliente(selectedCliente.getNie(), cliente.getNombre(), cliente.getDireccion(), cliente.getTelefono(), cliente.getAdquirido());
        });
        
        // Vaciar los datos en las tablas
        clientTableView.getItems().clear();
        
        // Cargar los datos en las tablas
        loadClientes();
    }
    
    public void modificarCliente(String nie, String nuevoNombre, String nuevaDireccion, String nuevoTelefono, int nuevoAdquirido) {
        String query = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ?, adquirido = ? WHERE nie = ?";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nuevoNombre);
            pstmt.setString(2, nuevaDireccion);
            pstmt.setString(3, nuevoTelefono);
            pstmt.setInt(4, nuevoAdquirido);
            pstmt.setString(5, nie);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Cliente modificado correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo modificar el cliente.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarCliente() {
        // Mostrar un diálogo para que el empleado seleccione el cliente a eliminar
        Cliente selectedCliente = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedCliente == null) {
            // Si no se selecciona ningún cliente, mostrar un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona un cliente antes de eliminar.");
            alert.showAndWait();
            return;
        }

        // Mostrar un diálogo de confirmación para confirmar la eliminación del cliente seleccionado
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Estás seguro de que quieres eliminar este cliente?");
        alert.setContentText("NIE: " + selectedCliente.getNie() + "\nNombre: " + selectedCliente.getNombre());

        // Obtener el resultado del diálogo de confirmación y eliminar el cliente si se confirma
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Llamar al método eliminarCliente con el NIE del cliente seleccionado
            eliminarCliente(selectedCliente.getNie());
        }
        
        // Vaciar los datos en las tablas
        clientTableView.getItems().clear();
        
        // Cargar los datos en las tablas
        loadClientes();
    }

    public void eliminarCliente(String nie) {
        String query = "DELETE FROM clientes WHERE nie = ?";
        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nie);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText(null);
                alert.setContentText("Cliente eliminado correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText(null);
                alert.setContentText("No se pudo eliminar el cliente.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
