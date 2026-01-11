package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ModificacionCategoriaController {

    @FXML private Button btnVolver;
    @FXML private ImageView walletImageView;
    @FXML private TextField txtNombre;
    @FXML private ColorPicker colorPicker;

    private Categoria categoriaAEditar;

    @FXML
    public void initialize() {
        try {
            walletImageView.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar icono: " + e.getMessage());
        }
    }

    public void initData(Categoria categoria) {
        this.categoriaAEditar = categoria;
        txtNombre.setText(categoria.getNombre());
        colorPicker.setValue(Color.web(categoria.getColor()));
    }

    @FXML
    void handleGuardar(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        Color color = colorPicker.getValue();

        if (nombre.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "El nombre no puede estar vacío.").showAndWait();
            return;
        }

        // --- VALIDACIÓN DE DUPLICADOS AÑADIDA ---
        // Revisa si otra categoría (distinta a la que estamos editando) ya tiene ese nombre
        List<Categoria> categoriasExistentes = GestorDatos.getInstance().getCategorias();
        for (Categoria cat : categoriasExistentes) {
            if (cat.getNombre().equalsIgnoreCase(nombre) && cat.getId() != categoriaAEditar.getId()) {
                new Alert(Alert.AlertType.ERROR, "Ya existe otra categoría con este nombre.").showAndWait();
                return;
            }
        }
        // --- FIN DE LA VALIDACIÓN ---

        String hexColor = String.format("#%02x%02x%02x",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        categoriaAEditar.setNombre(nombre);
        categoriaAEditar.setColor(hexColor);

        GestorDatos.getInstance().actualizarCategoria(categoriaAEditar);

        mostrarAlertaConfirmacion();
        handleVolver(event);
    }

    @FXML
    void handleVolver(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/CategoriasView.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlertaConfirmacion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Categoría editada exitosamente");
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/checkmark_icon.png")));
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        alert.getDialogPane().setGraphic(icon);
        alert.setContentText(null);
        alert.showAndWait();
    }
}