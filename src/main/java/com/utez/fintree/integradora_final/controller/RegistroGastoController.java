package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria; // <-- IMPORTAR
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
import java.time.LocalDate;

public class RegistroGastoController {

    @FXML private Button btnVolver;
    @FXML private ImageView walletImageView;
    @FXML private DatePicker pickerFecha;
    @FXML private TextField txtMonto;
    @FXML private TextField txtMetodoPago;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtNotas;

    // --- CAMBIO: De TextField a ComboBox ---
    @FXML private ComboBox<Categoria> cmbCategoria;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/icons/wallet_icon.png"));
            walletImageView.setImage(image);
        } catch (Exception e) { System.err.println("No se pudo cargar el icono."); }

        pickerFecha.setValue(LocalDate.now());

        // --- LÍNEA AÑADIDA: Cargar las categorías en el ComboBox ---
        cmbCategoria.setItems(GestorDatos.getInstance().getCategorias());
    }

    @FXML
    void handleRegistrarGasto(ActionEvent event) {
        // --- LÓGICA ACTUALIZADA ---
        Categoria categoriaSeleccionada = cmbCategoria.getValue();

        // Validación para asegurarse de que se seleccionó una categoría
        if (categoriaSeleccionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecciona una categoría.");
            alert.showAndWait();
            return;
        }

        try {
            Gasto nuevoGasto = new Gasto(
                    txtDescripcion.getText(),
                    Double.parseDouble(txtMonto.getText()),
                    categoriaSeleccionada.getNombre(), // Se obtiene el nombre del objeto seleccionado
                    pickerFecha.getValue(),
                    txtMetodoPago.getText(),
                    txtNotas.getText()
            );

            GestorDatos.getInstance().agregarGasto(nuevoGasto);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("Gasto registrado exitosamente");
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
            System.err.println("Error al registrar el gasto: " + e.getMessage());
        }
    }

    @FXML
    void handleVolver(ActionEvent event) {
        //... (sin cambios)
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/GastosView.fxml"));
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}