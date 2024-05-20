package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Cliente {
    private final SimpleStringProperty nie;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty direccion;
    private final SimpleStringProperty telefono;
    private final SimpleIntegerProperty adquirido;

    public Cliente(String nie, String nombre, String direccion, String telefono, int adquirido) {
        this.nie = new SimpleStringProperty(nie);
        this.nombre = new SimpleStringProperty(nombre);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleStringProperty(telefono);
        this.adquirido = new SimpleIntegerProperty(adquirido);
    }

    public String getNie() {
        return nie.get();
    }

    public SimpleStringProperty nieProperty() {
        return nie;
    }

    public void setNie(String nie) {
        this.nie.set(nie);
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public SimpleStringProperty direccionProperty() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public String getTelefono() {
        return telefono.get();
    }

    public SimpleStringProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public int getAdquirido() {
        return adquirido.get();
    }

    public SimpleIntegerProperty adquiridoProperty() {
        return adquirido;
    }

    public void setAdquirido(int adquirido) {
        this.adquirido.set(adquirido);
    }
}
