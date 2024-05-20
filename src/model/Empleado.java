package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Empleado {

    private final StringProperty NIE = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty cargo = new SimpleStringProperty();

    public Empleado() {}

    // Métodos getter y setter para NIE
    public String getNIE() {
        return NIE.get();
    }

    public void setNIE(String nie) {
        this.NIE.set(nie);
    }

    public StringProperty NIEProperty() {
        return NIE;
    }

    // Métodos getter y setter para nombre
    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    // Métodos getter y setter para cargo
    public String getCargo() {
        return cargo.get();
    }

    public void setCargo(String cargo) {
        this.cargo.set(cargo);
    }

    public StringProperty cargoProperty() {
        return cargo;
    }
}
