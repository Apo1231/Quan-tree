package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Presupuesto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public class PresupuestoController extends BaseController {

    private static final int ANIO = 2025;
    @FXML private ImageView walletIconTop;
    @FXML private VBox vboxEnero, vboxFebrero, vboxMarzo, vboxAbril, vboxMayo, vboxJunio, vboxJulio, vboxAgosto, vboxSeptiembre, vboxOctubre, vboxNoviembre, vboxDiciembre;
    @FXML private GridPane gridEnero, gridFebrero, gridMarzo, gridAbril, gridMayo, gridJunio, gridJulio, gridAgosto, gridSeptiembre, gridOctubre, gridNoviembre, gridDiciembre;
    @FXML private HBox hboxEnero, hboxFebrero, hboxMarzo, hboxAbril, hboxMayo, hboxJunio, hboxJulio, hboxAgosto, hboxSeptiembre, hboxOctubre, hboxNoviembre, hboxDiciembre;

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar el icono del wallet: " + e.getMessage());
        }
        dibujarCalendarios();
        actualizarControlesPresupuesto();
    }

    private void dibujarCalendarios() {
        GridPane[] calendarios = {gridEnero, gridFebrero, gridMarzo, gridAbril, gridMayo, gridJunio, gridJulio, gridAgosto, gridSeptiembre, gridOctubre, gridNoviembre, gridDiciembre};
        String[] diasSemana = {"L", "M", "M", "J", "V", "S", "D"};

        for (int i = 0; i < calendarios.length; i++) {
            int mes = i + 1;
            GridPane grid = calendarios[i];
            grid.getChildren().clear();

            for (int j = 0; j < diasSemana.length; j++) {
                Label lblDia = new Label(diasSemana[j]);
                lblDia.setStyle("-fx-font-weight: bold;");
                GridPane.setHalignment(lblDia, HPos.CENTER);
                grid.add(lblDia, j, 0);
            }

            YearMonth yearMonth = YearMonth.of(ANIO, mes);
            LocalDate primerDiaDelMes = yearMonth.atDay(1);
            int offset = primerDiaDelMes.getDayOfWeek().getValue() - 1;

            int diasEnMes = yearMonth.lengthOfMonth();
            for (int dia = 1; dia <= diasEnMes; dia++) {
                int columna = (dia + offset - 1) % 7;
                int fila = (dia + offset - 1) / 7 + 1;
                Label lblNumero = new Label(String.valueOf(dia));
                GridPane.setHalignment(lblNumero, HPos.CENTER);
                grid.add(lblNumero, columna, fila);
            }
        }
    }

    private void actualizarControlesPresupuesto() {
        VBox[] vBoxes = {vboxEnero, vboxFebrero, vboxMarzo, vboxAbril, vboxMayo, vboxJunio, vboxJulio, vboxAgosto, vboxSeptiembre, vboxOctubre, vboxNoviembre, vboxDiciembre};
        HBox[] hBoxes = {hboxEnero, hboxFebrero, hboxMarzo, hboxAbril, hboxMayo, hboxJunio, hboxJulio, hboxAgosto, hboxSeptiembre, hboxOctubre, hboxNoviembre, hboxDiciembre};

        for (int i = 0; i < vBoxes.length; i++) {
            final int mes = i + 1;
            VBox mesContainer = vBoxes[i];
            HBox actionBox = hBoxes[i];
            actionBox.getChildren().clear();

            mesContainer.setOnMouseClicked(event -> navegarADetalleMes(mes, ANIO));
            mesContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-cursor: hand;");

            Optional<Presupuesto> presupuestoOpt = GestorDatos.getInstance().getPresupuestos().stream()
                    .filter(p -> p.getMes() == mes && p.getAnio() == ANIO)
                    .findFirst();

            if (presupuestoOpt.isPresent() && presupuestoOpt.get().getMonto() > 0) {
                double monto = presupuestoOpt.get().getMonto();
                Label montoLabel = new Label(String.format("$%,.2f", monto));
                montoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #38761d;");
                actionBox.getChildren().add(montoLabel);
                mesContainer.setStyle("-fx-background-color: #eaf4ea; -fx-background-radius: 10; -fx-cursor: hand;");
            } else {
                Button addButton = new Button("Agregar Presupuesto");
                addButton.setOnAction(event -> handleAgregarPresupuesto(mes));
                addButton.setStyle("-fx-background-color: #6aa84f; -fx-text-fill: white; -fx-background-radius: 8;");
                actionBox.getChildren().add(addButton);
            }
        }
    }

    private void handleAgregarPresupuesto(int mes) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Presupuesto");
        dialog.setContentText("Monto para el mes " + mes + ":");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(montoStr -> {
            try {
                double monto = Double.parseDouble(montoStr);
                Presupuesto nuevo = new Presupuesto(monto, mes, ANIO);
                GestorDatos.getInstance().agregarOActualizarPresupuesto(nuevo);
                actualizarControlesPresupuesto();
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Monto no válido.").showAndWait();
            }
        });
    }

    private void navegarADetalleMes(int mes, int anio) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/fintree/integradora_final/PresupuestoMesView.fxml"));
            Parent root = loader.load();

            PresupuestoMesController controller = loader.getController();
            controller.initData(mes, anio);

            Stage stage = (Stage) vboxEnero.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}