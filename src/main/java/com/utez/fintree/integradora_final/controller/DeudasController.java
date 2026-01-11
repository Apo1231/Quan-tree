package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria;
import com.utez.fintree.integradora_final.model.Deuda;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeudasController extends BaseController {

    @FXML private ImageView walletIconTop;
    @FXML private Label lblTotalDeudas;
    @FXML private Label lblMontoPendiente;
    @FXML private FlowPane flowPaneDeudasActivas;
    @FXML private FlowPane flowPaneDeudasPagadas;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) { System.err.println("Error al cargar icono: " + e.getMessage()); }

        actualizarVistas();
        GestorDatos.getInstance().getDeudas().addListener((ListChangeListener<Deuda>) c -> actualizarVistas());
    }

    private void actualizarVistas() {
        actualizarVistaActivas();
        actualizarVistaPagadas();
    }

    private void actualizarVistaActivas() {
        List<Deuda> deudasActivas = GestorDatos.getInstance().getDeudas().stream()
                .filter(d -> "Activa".equalsIgnoreCase(d.getEstado()))
                .collect(Collectors.toList());

        lblTotalDeudas.setText("Total de deudas activas: " + deudasActivas.size());
        double montoTotalPendiente = deudasActivas.stream().mapToDouble(Deuda::getMontoRestante).sum();
        lblMontoPendiente.setText("Monto total pendiente: " + currencyFormatter.format(montoTotalPendiente));

        flowPaneDeudasActivas.getChildren().clear();
        for (Deuda deuda : deudasActivas) {
            flowPaneDeudasActivas.getChildren().add(crearTarjetaDeuda(deuda));
        }
    }

    private void actualizarVistaPagadas() {
        List<Deuda> deudasPagadas = GestorDatos.getInstance().getDeudas().stream()
                .filter(d -> "Pagada".equalsIgnoreCase(d.getEstado()))
                .collect(Collectors.toList());

        flowPaneDeudasPagadas.getChildren().clear();
        for (Deuda deuda : deudasPagadas) {
            flowPaneDeudasPagadas.getChildren().add(crearTarjetaDeudaPagada(deuda));
        }
    }

    private VBox crearTarjetaDeuda(Deuda deuda) {
        String colorHex = "#e0e0e0";
        Optional<Categoria> catOpt = GestorDatos.getInstance().getCategorias().stream().filter(c -> c.getNombre().equalsIgnoreCase(deuda.getCategoria())).findFirst();
        if(catOpt.isPresent()) colorHex = catOpt.get().getColor();

        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(200);
        tarjeta.setPadding(new Insets(10));
        tarjeta.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 15;");

        HBox topRow = new HBox(5);
        topRow.setAlignment(Pos.CENTER_LEFT);
        CheckBox chkPagada = new CheckBox();
        Button btnEditar = new Button("✎");
        btnEditar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        Button btnEliminar = new Button("🗑");
        btnEliminar.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        Button btnAbonar = new Button("Abonar");
        btnAbonar.setStyle("-fx-font-size: 10px; -fx-background-color: #6aa84f; -fx-text-fill: white; -fx-background-radius: 5;");
        topRow.getChildren().addAll(chkPagada, btnEditar, btnEliminar, spacer, btnAbonar);

        Label lblConcepto = new Label(deuda.getConcepto());
        lblConcepto.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label lblVence = new Label("Vence: " + deuda.getFechaLimite().format(formatter));
        Label lblMonto = new Label(currencyFormatter.format(deuda.getMontoRestante()));
        lblMonto.setFont(Font.font("System", FontWeight.BOLD, 16));
        Label lblPagado = new Label("Pagado: " + currencyFormatter.format(deuda.getMontoPagado()));
        lblPagado.setStyle("-fx-font-size: 10px;");
        Label lblNotas = new Label("\"" + deuda.getNotas() + "\"");
        lblNotas.setWrapText(true);
        lblNotas.setStyle("-fx-font-size: 10px;");

        tarjeta.getChildren().addAll(topRow, lblConcepto, lblVence, lblMonto, lblPagado, lblNotas);

        chkPagada.setOnAction(e -> marcarComoPagada(deuda));
        btnAbonar.setOnAction(e -> mostrarDialogoAbonar(deuda));
        btnEditar.setOnAction(e -> handleEditarDeuda(deuda));
        btnEliminar.setOnAction(e -> handleEliminarDeuda(deuda));

        return tarjeta;
    }

    // --- MÉTODO CORREGIDO ---
    private VBox crearTarjetaDeudaPagada(Deuda deuda) {
        VBox tarjeta = new VBox(5);
        tarjeta.setPrefWidth(200);
        tarjeta.setPadding(new Insets(10));
        // Estilo corregido: color de fondo sólido y más oscuro.
        tarjeta.setStyle("-fx-background-color: #EAEAEA; -fx-background-radius: 15;");

        Label lblConcepto = new Label(deuda.getConcepto());
        lblConcepto.setFont(Font.font("System", FontWeight.BOLD, 14));
        // Estilo corregido: color de texto oscuro para mejorar la legibilidad.
        lblConcepto.setStyle("-fx-text-fill: #333333;");

        Label lblMonto = new Label("Total pagado: " + currencyFormatter.format(deuda.getMontoInicial()));
        lblMonto.setStyle("-fx-text-fill: #555555;");

        Label lblFecha = new Label("Liquidada el: " + deuda.getFechaLimite().format(formatter));
        lblFecha.setStyle("-fx-font-size: 10px; -fx-text-fill: #555555;");

        tarjeta.getChildren().addAll(lblConcepto, lblMonto, lblFecha);
        return tarjeta;
    }

    private void marcarComoPagada(Deuda deuda) {
        deuda.setEstado("Pagada");
        deuda.setMontoPagado(deuda.getMontoInicial());
        GestorDatos.getInstance().actualizarDeuda(deuda);
    }

    private void mostrarDialogoAbonar(Deuda deuda) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Abonar a Deuda");
        dialog.setHeaderText("Abono para: " + deuda.getConcepto());
        dialog.setContentText("Monto a abonar:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(montoStr -> {
            try {
                double montoAbono = Double.parseDouble(montoStr);
                if (montoAbono > deuda.getMontoRestante() || montoAbono <= 0) {
                    new Alert(Alert.AlertType.ERROR, "El monto a abonar es inválido.").showAndWait();
                    return;
                }
                deuda.setMontoPagado(deuda.getMontoPagado() + montoAbono);
                if (deuda.getMontoRestante() <= 0.001) {
                    deuda.setEstado("Pagada");
                    deuda.setMontoPagado(deuda.getMontoInicial());
                }
                GestorDatos.getInstance().actualizarDeuda(deuda);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Por favor, ingresa un número válido.").showAndWait();
            }
        });
    }

    @FXML
    private void handleAgregarDeuda() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/utez/fintree/integradora_final/RegistroDeudaView.fxml"));
            Stage stage = (Stage) flowPaneDeudasActivas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEditarDeuda(Deuda deuda) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/fintree/integradora_final/ModificacionDeudaView.fxml"));
            Parent root = loader.load();
            ModificacionDeudaController controller = loader.getController();
            controller.initData(deuda);
            Stage stage = (Stage) flowPaneDeudasActivas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEliminarDeuda(Deuda deuda) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Eliminar la deuda '" + deuda.getConcepto() + "'?");
        alert.setContentText("Esta acción es permanente y no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            GestorDatos.getInstance().eliminarDeuda(deuda.getId());
        }
    }
}
