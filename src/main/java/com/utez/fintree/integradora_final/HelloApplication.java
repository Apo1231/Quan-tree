package com.utez.fintree.integradora_final;

import com.utez.fintree.integradora_final.db.ConexionDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image; // <-- IMPORT NECESARIO
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        // --- LÍNEA AÑADIDA PARA ESTABLECER EL ÍCONO ---
        // Asegúrate de que tu ícono esté en la ruta correcta.
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));

        stage.setTitle("QuanTree - Iniciar Sesión");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ConexionDB.inicializarDB();
        launch();
    }
}