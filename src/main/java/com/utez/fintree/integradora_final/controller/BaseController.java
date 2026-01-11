package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

public abstract class BaseController {

    protected void navegarA(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/" + fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void handleMenuInicio(ActionEvent event) { navegarA("InicioView.fxml", event); }
    @FXML void handleMenuAhorros(ActionEvent event) { navegarA("AhorrosView.fxml", event); }
    @FXML void handleMenuGastos(ActionEvent event) { navegarA("GastosView.fxml", event); }
    @FXML void handleMenuDeudas(ActionEvent event) { navegarA("DeudasView.fxml", event); }
    @FXML void handleMenuPresupuesto(ActionEvent event) { navegarA("PresupuestoView.fxml", event); }
    @FXML void handleMenuCategorias(ActionEvent event) { navegarA("CategoriasView.fxml", event); }

    /**
     * --- MÉTODO NUEVO PARA CERRAR SESIÓN ---
     * Limpia los datos del usuario actual y regresa a la pantalla de Login.
     */
    @FXML
    void handleCerrarSesion(ActionEvent event) {
        // 1. Llama al método para limpiar los datos en GestorDatos
        GestorDatos.getInstance().cerrarSesion();

        // 2. Navega de vuelta a la pantalla de Login
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
