package model;

import java.sql.Timestamp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Pedido {
    private final SimpleIntegerProperty idPedido;
    private final SimpleStringProperty nieCliente;
    private final SimpleStringProperty tipoTinta;
    private final SimpleIntegerProperty cantidad;
    private final ObjectProperty<Timestamp> fechaPedido;

    public Pedido(int idPedido, String nieCliente, String tipoTinta, int cantidad, Timestamp fechaPedido) {
        this.idPedido = new SimpleIntegerProperty(idPedido);
        this.nieCliente = new SimpleStringProperty(nieCliente);
        this.tipoTinta = new SimpleStringProperty(tipoTinta);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.fechaPedido = new SimpleObjectProperty<>(fechaPedido);
    }

    public int getIdPedido() {
        return idPedido.get();
    }

    public SimpleIntegerProperty idPedidoProperty() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido.set(idPedido);
    }

    public String getNieCliente() {
        return nieCliente.get();
    }

    public SimpleStringProperty nieClienteProperty() {
        return nieCliente;
    }

    public void setNieCliente(String nieCliente) {
        this.nieCliente.set(nieCliente);
    }

    public String getTipoTinta() {
        return tipoTinta.get();
    }

    public SimpleStringProperty tipoTintaProperty() {
        return tipoTinta;
    }

    public void setTipoTinta(String tipoTinta) {
        this.tipoTinta.set(tipoTinta);
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public SimpleIntegerProperty cantidadProperty() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public ObservableValue<String> fechaPedidoProperty() {
        return new SimpleStringProperty(fechaPedido.get().toString());
    }

    public Timestamp getFechaPedido() {
        return fechaPedido.get();
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido.set(fechaPedido);
    }
}
