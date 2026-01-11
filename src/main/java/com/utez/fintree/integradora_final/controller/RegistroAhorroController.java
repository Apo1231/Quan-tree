package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Ahorro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class RegistroAhorroController {

    @FXML private Button btnVolver;
    @FXML private ImageView walletImageView;
    @FXML private DatePicker pickerFecha;
    @FXML private TextField txtMonto;

    // --- CAMPOS CORREGIDOS ---
    @FXML private TextField txtConcepto;
    @FXML private TextField txtMeta;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/icons/wallet_icon.png"));
            walletImageView.setImage(image);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono del wallet.");
        }
        pickerFecha.setValue(LocalDate.now());
    }

    @FXML
    void handleRegistrar(ActionEvent event) {
        try {
            // --- LÓGICA CORREGIDA ---
            Ahorro nuevoAhorro = new Ahorro(
                    txtConcepto.getText(),
                    Double.parseDouble(txtMonto.getText()),
                    pickerFecha.getValue(),
                    txtMeta.getText()
            );

            GestorDatos.getInstance().agregarAhorro(nuevoAhorro);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Ahorro registrado exitosamente");
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/checkmark_icon.png")));
            icon.setFitHeight(80);
            icon.setFitWidth(80);
            alert.getDialogPane().setGraphic(icon);
            alert.setContentText(null);
            alert.showAndWait();

            handleVolver(event);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "El monto debe ser un número válido.");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error al registrar el ahorro: " + e.getMessage());
        }
    }

    @FXML
    void handleVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/AhorrosView.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}