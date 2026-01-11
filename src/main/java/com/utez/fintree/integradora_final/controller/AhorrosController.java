package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Ahorro;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AhorrosController extends BaseController {

    @FXML private ImageView walletIconTop;
    @FXML private BarChart<String, Number> chartAhorros;
    @FXML private ListView<Ahorro> listaAhorros;
    @FXML private ComboBox<String> cmbSort;

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) { System.err.println("Error al cargar icono."); }

        actualizarGraficaAhorros();

        // --- LÓGICA DE ORDENAMIENTO ---
        ObservableList<Ahorro> sourceList = GestorDatos.getInstance().getAhorros();
        SortedList<Ahorro> sortedList = new SortedList<>(sourceList);
        listaAhorros.setItems(sortedList);

        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Más Recientes", "Monto (Mayor a Menor)", "Concepto"
        );
        cmbSort.setItems(sortOptions);

        cmbSort.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Más Recientes":
                        sortedList.setComparator(Comparator.comparing(Ahorro::getFecha).reversed());
                        break;
                    case "Monto (Mayor a Menor)":
                        sortedList.setComparator(Comparator.comparing(Ahorro::getMonto).reversed());
                        break;
                    case "Concepto":
                        sortedList.setComparator(Comparator.comparing(Ahorro::getConcepto));
                        break;
                }
            }
        });

        cmbSort.getSelectionModel().selectFirst();

        // --- LÍNEA CORREGIDA ---
        GestorDatos.getInstance().getAhorros().addListener((ListChangeListener<Ahorro>) c -> actualizarGraficaAhorros());

        listaAhorros.setCellFactory(param -> new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final Text text = new Text();
            private final Pane pane = new Pane();
            private final Button button = new Button("Editar");

            {
                hbox.getChildren().addAll(text, pane, button);
                HBox.setHgrow(pane, Priority.ALWAYS);
                button.setOnAction(event -> {
                    Ahorro item = getItem();
                    if (item != null) abrirPantallaDeEdicion(item, event);
                });
            }
            @Override
            protected void updateItem(Ahorro item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item.toString());
                    setGraphic(hbox);
                }
            }
        });
    }

    private void actualizarGraficaAhorros() {
        chartAhorros.getData().clear();

        List<Ahorro> ahorros = GestorDatos.getInstance().getAhorros();
        int anioActual = LocalDate.now().getYear();

        Map<Month, Double> ahorrosPorMes = ahorros.stream()
                .filter(ahorro -> ahorro.getFecha().getYear() == anioActual)
                .collect(Collectors.groupingBy(
                        ahorro -> ahorro.getFecha().getMonth(),
                        TreeMap::new,
                        Collectors.summingDouble(Ahorro::getMonto)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(String.valueOf(anioActual));

        for (Map.Entry<Month, Double> entry : ahorrosPorMes.entrySet()) {
            String nombreMes = entry.getKey().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"));
            series.getData().add(new XYChart.Data<>(nombreMes, entry.getValue()));
        }

        chartAhorros.getData().add(series);
    }

    private void abrirPantallaDeEdicion(Ahorro ahorro, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/fintree/integradora_final/ModificacionAhorroView.fxml"));
            Parent root = loader.load();
            ModificacionAhorroController controller = loader.getController();
            controller.initData(ahorro);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void handleRegistrarAhorro(ActionEvent event) {
        navegarA("RegistroAhorroView.fxml", event);
    }
}