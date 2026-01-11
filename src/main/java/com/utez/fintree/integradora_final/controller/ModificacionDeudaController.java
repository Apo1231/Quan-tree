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

public class ModificacionDeudaController {

    @FXML private Button btnVolver;
    @FXML private ImageView walletImageView;
    @FXML private TextField txtConcepto;
    @FXML private DatePicker pickerFechaInicio;
    @FXML private TextField txtMonto;
    @FXML private DatePicker pickerFechaLimite;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtNotas;

    private Deuda deudaAEditar;

    @FXML
    public void initialize() {
        try {
            walletImageView.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar icono: " + e.getMessage());
        }
        cmbCategoria.setItems(GestorDatos.getInstance().getCategorias());
    }

    // Método para recibir la deuda desde la pantalla principal
    public void initData(Deuda deuda) {
        this.deudaAEditar = deuda;
        txtConcepto.setText(deuda.getConcepto());
        pickerFechaInicio.setValue(deuda.getFechaInicio());
        txtMonto.setText(String.valueOf(deuda.getMontoInicial()));
        pickerFechaLimite.setValue(deuda.getFechaLimite());
        // Seleccionar la categoría correcta en el ComboBox
        for (Categoria cat : cmbCategoria.getItems()) {
            if (cat.getNombre().equals(deuda.getCategoria())) {
                cmbCategoria.setValue(cat);
                break;
            }
        }
        txtNotas.setText(deuda.getNotas());
    }

    @FXML
    void handleGuardar(ActionEvent event) {
        if (txtConcepto.getText().isEmpty() || pickerFechaInicio.getValue() == null || txtMonto.getText().isEmpty() || pickerFechaLimite.getValue() == null || cmbCategoria.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Todos los campos son obligatorios, excepto las notas.").showAndWait();
            return;
        }

        try {
            // Actualizamos el objeto existente
            deudaAEditar.setConcepto(txtConcepto.getText());
            deudaAEditar.setMontoInicial(Double.parseDouble(txtMonto.getText()));
            deudaAEditar.setFechaInicio(pickerFechaInicio.getValue());
            deudaAEditar.setFechaLimite(pickerFechaLimite.getValue());
            deudaAEditar.setCategoria(cmbCategoria.getValue().getNombre());
            deudaAEditar.setNotas(txtNotas.getText());

            GestorDatos.getInstance().actualizarDeuda(deudaAEditar);
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
        alert.setHeaderText("Deuda editada exitosamente");
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/checkmark_icon.png")));
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        alert.getDialogPane().setGraphic(icon);
        alert.setContentText(null);
        alert.showAndWait();
    }
}