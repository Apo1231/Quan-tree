package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Ahorro;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ModificacionAhorroController {

    @FXML
    private Button btnVolver;
    @FXML
    private ImageView walletImageView;
    @FXML
    private DatePicker pickerFecha;
    @FXML
    private TextField txtMonto;
    @FXML
    private TextField txtTipoMovimiento;
    @FXML
    private TextField txtNotas;

    private Ahorro ahorroAEditar;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/icons/wallet_icon.png"));
            walletImageView.setImage(image);
        } catch (Exception e) { System.err.println("No se pudo cargar el icono."); }
    }

    public void initData(Ahorro ahorro) {
        this.ahorroAEditar = ahorro;
        txtNotas.setText(ahorro.getConcepto());
        txtMonto.setText(String.valueOf(ahorro.getMonto()));
        pickerFecha.setValue(ahorro.getFecha());
        // Asumimos que el tipo de movimiento y meta se guardan en el concepto o notas
    }

    @FXML
    void handleGuardar(ActionEvent event) {
        // Actualizamos el objeto Ahorro con los nuevos datos del formulario
        ahorroAEditar.setConcepto(txtNotas.getText());
        ahorroAEditar.setMonto(Double.parseDouble(txtMonto.getText()));
        ahorroAEditar.setFecha(pickerFecha.getValue());

        // Llamamos al gestor para que actualice la BD
        GestorDatos.getInstance().actualizarAhorro(ahorroAEditar);

        // --- CÓDIGO NUEVO PARA LA CONFIRMACIÓN ---
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Ahorro editado exitosamente");
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/checkmark_icon.png")));
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        alert.getDialogPane().setGraphic(icon);
        alert.setContentText(null);
        alert.showAndWait();

        // Después de aceptar, volvemos a la pantalla anterior
        handleVolver(event);
    }

    @FXML
    void handleVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/AhorrosView.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}