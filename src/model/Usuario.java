package model;

import javafx.beans.property.SimpleStringProperty;

public class Usuario {
    private final SimpleStringProperty usuario;
    private final SimpleStringProperty password;
    private final SimpleStringProperty group;

    public Usuario(String usuario, String password, String group) {
        this.usuario = new SimpleStringProperty(usuario);
        this.password = new SimpleStringProperty(password);
        this.group = new SimpleStringProperty(group);
    }

    public String getUsuario() {
        return usuario.get();
    }

    public SimpleStringProperty usuarioProperty() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
    
    public String getGroup() {
        return group.get();
    }

    public SimpleStringProperty groupProperty() {
        return group;
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

}
