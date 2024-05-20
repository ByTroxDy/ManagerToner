package controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.ConexionDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Pedido;

public class PedidosController  implements Initializable {
	
	@FXML
    private TableView<Pedido> pedidoTableView;

    @FXML
    private TableColumn<Pedido, Integer> idPedidoColumn;

    @FXML
    private TableColumn<Pedido, String> nieClienteColumn;

    @FXML
    private TableColumn<Pedido, String> tipoTintaColumn;

    @FXML
    private TableColumn<Pedido, Integer> cantidadColumn;

    @FXML
    private TableColumn<Pedido, String> fechaPedidoColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar las columnas de las tablas
        initPedidoTableColumns();
        // Cargar los datos en las tablas
        loadPedidos();
    }
    
    private void initPedidoTableColumns() {
        idPedidoColumn.setCellValueFactory(cellData -> cellData.getValue().idPedidoProperty().asObject());
        nieClienteColumn.setCellValueFactory(cellData -> cellData.getValue().nieClienteProperty());
        tipoTintaColumn.setCellValueFactory(cellData -> cellData.getValue().tipoTintaProperty());
        cantidadColumn.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        fechaPedidoColumn.setCellValueFactory(cellData -> cellData.getValue().fechaPedidoProperty());
    }
    
    private void loadPedidos() {
        // Consulta SQL para obtener los datos de los pedidos
        String query = "SELECT * FROM pedidos";

        try (PreparedStatement pstmt = ConexionDB.getConnection().prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("idPedido"),
                        rs.getString("nieCliente"),
                        rs.getString("tipoTinta"),
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fechaPedido")
                );
                pedidoTableView.getItems().add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void cerrar() {
    	// Cerrar la ventana actual
        Stage stage = (Stage) pedidoTableView.getScene().getWindow();
        stage.close();
    }

}
