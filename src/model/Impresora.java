package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Impresora {
    private final SimpleIntegerProperty idImpresora;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty modelo;
    private final SimpleStringProperty tipoTinta;

    public Impresora(int idImpresora, String nombre, String modelo, String tipoTinta) {
        this.idImpresora = new SimpleIntegerProperty(idImpresora);
        this.nombre = new SimpleStringProperty(nombre);
        this.modelo = new SimpleStringProperty(modelo);
        this.tipoTinta = new SimpleStringProperty(tipoTinta);
    }
    
    public Impresora(String nombre, String modelo, String tipoTinta) {
        this.idImpresora = new SimpleIntegerProperty();
		this.nombre = new SimpleStringProperty(nombre);
        this.modelo = new SimpleStringProperty(modelo);
        this.tipoTinta = new SimpleStringProperty(tipoTinta);
    }

    public int getIdImpresora() {
        return idImpresora.get();
    }

    public SimpleIntegerProperty idImpresoraProperty() {
        return idImpresora;
    }

    public void setIdImpresora(int idImpresora) {
        this.idImpresora.set(idImpresora);
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

    public String getModelo() {
        return modelo.get();
    }

    public SimpleStringProperty modeloProperty() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo.set(modelo);
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
}
