package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria;
import com.utez.fintree.integradora_final.model.Gasto;
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

public class ModificacionGastoController {

    @FXML private Button btnVolver;
    @FXML private ImageView walletImageView;
    @FXML private DatePicker pickerFecha;
    @FXML private TextField txtMonto;
    @FXML private TextField txtMetodoPago;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtNotas;

    private Gasto gastoAEditar;

    @FXML
    public void initialize() {
        try {
            walletImageView.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar icono: " + e.getMessage());
        }
        // Cargar las categorías del usuario actual
        cmbCategoria.setItems(GestorDatos.getInstance().getCategorias());
    }

    public void initData(Gasto gasto) {
        this.gastoAEditar = gasto;
        pickerFecha.setValue(gasto.getFecha());
        txtMonto.setText(String.valueOf(gasto.getMonto()));
        txtMetodoPago.setText(gasto.getMetodoPago());
        txtDescripcion.setText(gasto.getConcepto());
        txtNotas.setText(gasto.getNotas());

        // Seleccionar la categoría correcta en el ComboBox
        for (Categoria cat : cmbCategoria.getItems()) {
            if (cat.getNombre().equals(gasto.getCategoria())) {
                cmbCategoria.setValue(cat);
                break;
            }
        }
    }

    @FXML
    void handleGuardar(ActionEvent event) {
        if (cmbCategoria.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Debes seleccionar una categoría.").showAndWait();
            return;
        }

        try {
            // Actualizamos el objeto Gasto
            gastoAEditar.setFecha(pickerFecha.getValue());
            gastoAEditar.setMonto(Double.parseDouble(txtMonto.getText()));
            gastoAEditar.setMetodoPago(txtMetodoPago.getText());
            gastoAEditar.setCategoria(cmbCategoria.getValue().getNombre());
            gastoAEditar.setConcepto(txtDescripcion.getText());
            gastoAEditar.setNotas(txtNotas.getText());

            // Llamamos al GestorDatos (que ya sabe quién es el usuario)
            GestorDatos.getInstance().actualizarGasto(gastoAEditar);

            new Alert(Alert.AlertType.INFORMATION, "Gasto actualizado exitosamente.").showAndWait();
            handleVolver(event);

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El monto debe ser un número válido.").showAndWait();
        }
    }

    @FXML
    void handleVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/GastosView.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}