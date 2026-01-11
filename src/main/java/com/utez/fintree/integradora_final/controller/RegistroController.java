package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.dao.UsuarioDAO;
import com.utez.fintree.integradora_final.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistroController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private PasswordField txtConfirmarContrasena;
    @FXML
    private ImageView walletImageView;
    @FXML
    private Hyperlink linkVolverLogin;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/icons/wallet_icon.png"));
            walletImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error al cargar icono en Registro.");
        }
    }

    // En RegistroController.java
    @FXML
    void handleRegistrarse(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String pass1 = txtContrasena.getText();
        String pass2 = txtConfirmarContrasena.getText();

        if (usuario.isEmpty() || pass1.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "El usuario y la contraseña son obligatorios.");
            return;
        }
        if (!pass1.equals(pass2)) {
            mostrarAlerta(AlertType.ERROR, "Error de Registro", "Las contraseñas no coinciden.");
            return;
        }

        // --- LÓGICA DE GUARDADO EN BD ---
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuarioDAO.agregar(new Usuario(usuario, pass1))) {
            mostrarAlerta(AlertType.INFORMATION, "Registro Exitoso", "¡Usuario registrado! Ahora puedes iniciar sesión.");
            handleVolverLogin(event);
        } else {
            mostrarAlerta(AlertType.ERROR, "Error de Registro", "El nombre de usuario ya existe o hubo un error en la base de datos.");
        }
    }
    @FXML
    void handleVolverLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/LoginView.fxml"));
            Stage stage = (Stage) linkVolverLogin.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método helper para crear alertas fácilmente
    private void mostrarAlerta(AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}