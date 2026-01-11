package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Ahorro;
import com.utez.fintree.integradora_final.model.Gasto;
import com.utez.fintree.integradora_final.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class InicioController extends BaseController {

    @FXML private ImageView walletIconTop, gastosIcon, ahorrosIcon;
    @FXML private Label lblTotalGastos, lblTotalAhorros, lblMesActual, lblMesActualAhorros;
    @FXML private PieChart pieChartGastos, pieChartAhorros;
    @FXML private VBox leyendaGastos, leyendaAhorros;
    @FXML private Label lblNombreUsuario; // Variable para el nombre de usuario

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
            gastosIcon.setImage(new Image(getClass().getResourceAsStream("/icons/shopping_bag_icon.png")));
            ahorrosIcon.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar iconos: " + e.getMessage());
        }

        // Lógica para mostrar el nombre del usuario
        Usuario usuario = GestorDatos.getInstance().getUsuarioActual();
        if (usuario != null) {
            lblNombreUsuario.setText("Usuario: " + usuario.getNombreUsuario());
        }

        actualizarResumenGastos();
        actualizarResumenAhorros();
    }

    private void actualizarResumenGastos() {
        YearMonth mesActual = YearMonth.now();
        String nombreMes = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        lblMesActual.setText(nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1) + " " + mesActual.getYear());

        List<Gasto> gastosDelMes = GestorDatos.getInstance().getGastos().stream()
                .filter(gasto -> YearMonth.from(gasto.getFecha()).equals(mesActual))
                .collect(Collectors.toList());

        double total = gastosDelMes.stream().mapToDouble(Gasto::getMonto).sum();
        lblTotalGastos.setText(String.format("$%,.2f MXN", total));

        Map<String, Double> gastosPorCategoria = gastosDelMes.stream()
                .collect(Collectors.groupingBy(Gasto::getCategoria, Collectors.summingDouble(Gasto::getMonto)));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        gastosPorCategoria.forEach((categoria, monto) -> pieChartData.add(new PieChart.Data(categoria, monto)));
        pieChartGastos.setData(pieChartData);

        crearLeyenda(leyendaGastos, pieChartData);
    }

    private void actualizarResumenAhorros() {
        YearMonth mesActual = YearMonth.now();
        String nombreMes = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        lblMesActualAhorros.setText(nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1) + " " + mesActual.getYear());

        List<Ahorro> ahorrosDelMes = GestorDatos.getInstance().getAhorros().stream()
                .filter(ahorro -> YearMonth.from(ahorro.getFecha()).equals(mesActual))
                .collect(Collectors.toList());

        double total = ahorrosDelMes.stream().mapToDouble(Ahorro::getMonto).sum();
        lblTotalAhorros.setText(String.format("$%,.2f MXN", total));

        Map<String, Double> ahorrosPorConcepto = ahorrosDelMes.stream()
                .collect(Collectors.groupingBy(Ahorro::getConcepto, Collectors.summingDouble(Ahorro::getMonto)));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ahorrosPorConcepto.forEach((concepto, monto) -> pieChartData.add(new PieChart.Data(concepto, monto)));
        pieChartAhorros.setData(pieChartData);

        crearLeyenda(leyendaAhorros, pieChartData);
    }

    private void crearLeyenda(VBox contenedorLeyenda, ObservableList<PieChart.Data> data) {
        contenedorLeyenda.getChildren().clear();
        int colorIndex = 0;
        String[] colores = {"#FF6347", "#4682B4", "#32CD32", "#FFD700", "#6A5ACD", "#FF4500"};

        for (PieChart.Data entry : data) {
            HBox itemLeyenda = new HBox(5);
            itemLeyenda.setAlignment(Pos.CENTER_LEFT);

            Rectangle colorRect = new Rectangle(10, 10);
            String color = colores[colorIndex % colores.length];
            colorRect.setStyle("-fx-fill: " + color + ";");

            Label lblNombre = new Label(entry.getName());

            itemLeyenda.getChildren().addAll(colorRect, lblNombre);
            contenedorLeyenda.getChildren().add(itemLeyenda);

            if (entry.getNode() != null) {
                entry.getNode().setStyle("-fx-pie-color: " + color + ";");
            }
            colorIndex++;
        }
    }
}