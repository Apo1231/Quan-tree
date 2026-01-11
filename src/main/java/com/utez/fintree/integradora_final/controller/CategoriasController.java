package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class CategoriasController extends BaseController {

    @FXML private ImageView walletIconTop;
    @FXML private Label lblTotalCategorias;
    @FXML private FlowPane flowPaneCategorias;

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }

        actualizarVistaCategorias();

        // Escucha cambios en la lista para refrescar la vista automáticamente
        GestorDatos.getInstance().getCategorias().addListener((ListChangeListener<Categoria>) c -> {
            actualizarVistaCategorias();
        });
    }

    private void actualizarVistaCategorias() {
        flowPaneCategorias.getChildren().clear();
        int total = GestorDatos.getInstance().getCategorias().size();
        lblTotalCategorias.setText("Categorías registradas: " + total);

        for (Categoria categoria : GestorDatos.getInstance().getCategorias()) {
            flowPaneCategorias.getChildren().add(crearTarjetaCategoria(categoria));
        }

        flowPaneCategorias.getChildren().add(crearTarjetaAgregar());
    }

    private VBox crearTarjetaCategoria(Categoria categoria) {
        VBox tarjeta = new VBox(10);
        tarjeta.setPrefSize(180, 100);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPadding(new Insets(10));
        String colorHex = categoria.getColor() != null && !categoria.getColor().isEmpty() ? categoria.getColor() : "#E0E0E0";
        tarjeta.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 15;");

        HBox topRow = new HBox(5);
        topRow.setAlignment(Pos.CENTER_LEFT);
        Button btnEditar = new Button("✎");
        Button btnEliminar = new Button("🗑");
        btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        // Acción para el botón de editar
        btnEditar.setOnAction(e -> mostrarDialogoEditar(categoria));

        // --- LÓGICA DE ELIMINAR CORREGIDA ---
        btnEliminar.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("Eliminar la categoría: '" + categoria.getNombre() + "'");
            alert.setContentText("¿Estás seguro? Esta acción no se puede deshacer.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                GestorDatos.getInstance().eliminarCategoria(categoria.getId());
            }
        });

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Circle colorCircle = new Circle(8, Color.web(colorHex).darker());

        topRow.getChildren().addAll(btnEditar, btnEliminar, spacer, colorCircle);

        Label lblNombre = new Label(categoria.getNombre().toUpperCase());
        lblNombre.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblNombre.setTextFill(Color.BLACK);

        tarjeta.getChildren().addAll(topRow, lblNombre);
        return tarjeta;
    }

    private VBox crearTarjetaAgregar() {
        VBox tarjeta = new VBox();
        tarjeta.setPrefSize(180, 100);
        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setStyle("-fx-background-color: #E9E9E9; -fx-background-radius: 15; -fx-cursor: hand;");
        Label lblPlus = new Label("+");
        lblPlus.setFont(Font.font(48));
        tarjeta.getChildren().add(lblPlus);
        tarjeta.setOnMouseClicked(e -> mostrarPantallaDeRegistro());
        return tarjeta;
    }

    // --- MÉTODO CORREGIDO PARA NAVEGAR A LA VISTA DE REGISTRO ---
    private void mostrarPantallaDeRegistro() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/RegistroCategoriaView.fxml"));
            Stage stage = (Stage) flowPaneCategorias.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODO DE EDICIÓN (AÚN PENDIENTE DE IMPLEMENTAR SU VISTA) ---
    private void mostrarDialogoEditar(Categoria categoria) {
        try {
            // 1. Cargar el FXML de modificación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/fintree/integradora_final/ModificacionCategoriaView.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de la nueva pantalla
            ModificacionCategoriaController controller = loader.getController();

            // 3. Pasar los datos de la categoría al controlador
            controller.initData(categoria);

            // 4. Mostrar la nueva pantalla
            Stage stage = (Stage) flowPaneCategorias.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}