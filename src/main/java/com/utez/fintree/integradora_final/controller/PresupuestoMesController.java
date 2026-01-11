package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Gasto;
import com.utez.fintree.integradora_final.model.Presupuesto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class PresupuestoMesController extends BaseController {

    @FXML private ImageView walletIconTop, presupuestoIcon, gastoIcon;
    @FXML private Label lblTituloMes, lblPresupuestoTotal, lblGastoAcumulado, lblPorcentajeConsumido;
    @FXML private Button btnVolver;

    private int mes;
    private int anio;
    private Presupuesto presupuestoActual;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
            presupuestoIcon.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
            gastoIcon.setImage(new Image(getClass().getResourceAsStream("/icons/shopping_bag_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar iconos: " + e.getMessage());
        }
    }

    public void initData(int mes, int anio) {
        this.mes = mes;
        this.anio = anio;
        actualizarVista();
    }

    private void actualizarVista() {
        // 1. Poner el título
        String nombreMes = Month.of(mes).getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        lblTituloMes.setText(nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1) + " " + anio);

        // 2. Obtener el presupuesto para este mes
        Optional<Presupuesto> presupuestoOpt = GestorDatos.getInstance().getPresupuestos().stream()
                .filter(p -> p.getMes() == mes && p.getAnio() == anio)
                .findFirst();

        presupuestoActual = presupuestoOpt.orElse(new Presupuesto(0.0, mes, anio));
        lblPresupuestoTotal.setText(currencyFormatter.format(presupuestoActual.getMonto()));

        // 3. Calcular el gasto acumulado del mes
        YearMonth anioMes = YearMonth.of(anio, mes);
        double gastoTotal = GestorDatos.getInstance().getGastos().stream()
                .filter(gasto -> YearMonth.from(gasto.getFecha()).equals(anioMes))
                .mapToDouble(Gasto::getMonto)
                .sum();
        lblGastoAcumulado.setText(currencyFormatter.format(gastoTotal));

        // 4. Calcular el porcentaje consumido
        if (presupuestoActual.getMonto() > 0) {
            double porcentaje = (gastoTotal / presupuestoActual.getMonto()) * 100;
            lblPorcentajeConsumido.setText(String.format("%.2f%%", porcentaje));
        } else {
            lblPorcentajeConsumido.setText("N/A");
        }
    }

    @FXML
    void handleEditarPresupuesto() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(presupuestoActual.getMonto()));
        dialog.setTitle("Editar Presupuesto");
        dialog.setHeaderText("Editando presupuesto para " + lblTituloMes.getText());
        dialog.setContentText("Nuevo monto:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(montoStr -> {
            try {
                double nuevoMonto = Double.parseDouble(montoStr);
                presupuestoActual.setMonto(nuevoMonto);
                GestorDatos.getInstance().agregarOActualizarPresupuesto(presupuestoActual);
                actualizarVista(); // Refrescar los datos en pantalla
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Por favor, ingresa un número válido.").showAndWait();
            }
        });
    }

    @FXML
    void handleVolver(ActionEvent event) {
        navegarA("PresupuestoView.fxml", event);
    }
}
