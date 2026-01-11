package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.dao.UsuarioDAO;
import com.utez.fintree.integradora_final.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnCrearCuenta;
    @FXML private ImageView walletImageView;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/icons/wallet_icon.png"));
            walletImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error al cargar icono.");
        }
    }

    @FXML
    void handleIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioValidado = usuarioDAO.validar(usuario, contrasena);

        if (usuarioValidado != null) {
            // --- LÍNEA CLAVE ---
            // Le decimos al gestor quién es el usuario y que cargue sus datos
            GestorDatos.getInstance().iniciarSesion(usuarioValidado);

            System.out.println("Login exitoso para: " + usuarioValidado.getNombreUsuario());
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/InicioView.fxml"));
                Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Inicio de Sesión");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("El usuario o la contraseña no son válidos.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleCrearCuenta(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/RegistroView.fxml"));
            Stage stage = (Stage) btnCrearCuenta.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
