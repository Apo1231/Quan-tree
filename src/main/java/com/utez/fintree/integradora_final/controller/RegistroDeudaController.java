package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria;
import com.utez.fintree.integradora_final.model.Deuda;
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

public class RegistroDeudaController {

    @FXML private Button btnVolver;
    @FXML private ImageView walletImageView;
    @FXML private TextField txtConcepto;
    @FXML private DatePicker pickerFechaInicio;
    @FXML private TextField txtMonto;
    @FXML private DatePicker pickerFechaLimite;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtNotas;

    @FXML
    public void initialize() {
        try {
            walletImageView.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar icono: " + e.getMessage());
        }
        cmbCategoria.setItems(GestorDatos.getInstance().getCategorias());
    }

    @FXML
    void handleRegistrar(ActionEvent event) {
        // Validaciones
        if (txtConcepto.getText().isEmpty() || pickerFechaInicio.getValue() == null || txtMonto.getText().isEmpty() || pickerFechaLimite.getValue() == null || cmbCategoria.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Todos los campos son obligatorios, excepto las notas.").showAndWait();
            return;
        }

        try {
            Deuda nuevaDeuda = new Deuda(
                    txtConcepto.getText(),
                    Double.parseDouble(txtMonto.getText()),
                    pickerFechaInicio.getValue(),
                    pickerFechaLimite.getValue(),
                    cmbCategoria.getValue().getNombre(),
                    txtNotas.getText()
            );

            GestorDatos.getInstance().agregarDeuda(nuevaDeuda);
            mostrarAlertaConfirmacion();
            handleVolver(event);

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El monto debe ser un número válido.").showAndWait();
        }
    }

    @FXML
    void handleVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/DeudasView.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlertaConfirmacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Deuda registrada exitosamente");
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/checkmark_icon.png")));
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        alert.getDialogPane().setGraphic(icon);
        alert.setContentText(null);
        alert.showAndWait();
    }
}