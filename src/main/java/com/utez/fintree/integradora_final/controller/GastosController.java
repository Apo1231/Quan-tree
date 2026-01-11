package com.utez.fintree.integradora_final.controller;

import com.utez.fintree.integradora_final.GestorDatos;
import com.utez.fintree.integradora_final.model.Categoria;
import com.utez.fintree.integradora_final.model.Gasto;
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
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class GastosController extends BaseController {

    @FXML private ImageView walletIconTop;
    @FXML private BarChart<String, Number> chartGastos;
    @FXML private ListView<Gasto> listaGastos;
    @FXML private ComboBox<String> cmbSort;

    @FXML
    public void initialize() {
        try {
            walletIconTop.setImage(new Image(getClass().getResourceAsStream("/icons/wallet_icon.png")));
        } catch (Exception e) { System.err.println("Error al cargar icono."); }

        // --- LÓGICA DE ORDENAMIENTO ---
        ObservableList<Gasto> sourceList = GestorDatos.getInstance().getGastos();
        SortedList<Gasto> sortedList = new SortedList<>(sourceList);
        listaGastos.setItems(sortedList);

        ObservableList<String> sortOptions = FXCollections.observableArrayList(
                "Más Recientes", "Monto (Mayor a Menor)", "Categoría"
        );
        cmbSort.setItems(sortOptions);

        cmbSort.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Más Recientes":
                        sortedList.setComparator(Comparator.comparing(Gasto::getFecha).reversed());
                        break;
                    case "Monto (Mayor a Menor)":
                        sortedList.setComparator(Comparator.comparing(Gasto::getMonto).reversed());
                        break;
                    case "Categoría":
                        sortedList.setComparator(Comparator.comparing(Gasto::getCategoria));
                        break;
                }
            }
        });

        cmbSort.getSelectionModel().selectFirst();

        // La gráfica se sigue actualizando con la lista original
        actualizarGraficaGastos();
        // --- LÍNEA CORREGIDA ---
        GestorDatos.getInstance().getGastos().addListener((ListChangeListener<Gasto>) c -> actualizarGraficaGastos());

        listaGastos.setCellFactory(param -> new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final Text text = new Text();
            private final Pane pane = new Pane();
            private final Button button = new Button("Editar");

            {
                hbox.getChildren().addAll(text, pane, button);
                HBox.setHgrow(pane, Priority.ALWAYS);
                button.setOnAction(event -> {
                    Gasto item = getItem();
                    if (item != null) abrirPantallaDeEdicion(item, event);
                });
            }
            @Override
            protected void updateItem(Gasto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(String.format("[%s] %s - $%.2f", item.getCategoria(), item.getConcepto(), item.getMonto()));
                    setGraphic(hbox);
                }
            }
        });
    }

    private void actualizarGraficaGastos() {
        chartGastos.getData().clear();
        chartGastos.setLegendVisible(false);

        Map<String, Double> gastosPorCategoria = GestorDatos.getInstance().getGastos().stream()
                .collect(Collectors.groupingBy(
                        Gasto::getCategoria,
                        Collectors.summingDouble(Gasto::getMonto)
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Double> entry : gastosPorCategoria.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chartGastos.getData().add(series);

        for (XYChart.Data<String, Number> data : series.getData()) {
            String categoriaNombre = data.getXValue();
            String colorHex = obtenerColorParaCategoria(categoriaNombre);
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: " + colorHex + ";");
            }
        }
    }

    private String obtenerColorParaCategoria(String nombreCategoria) {
        return GestorDatos.getInstance().getCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombreCategoria))
                .findFirst()
                .map(Categoria::getColor)
                .orElse("#FF6347");
    }

    private void abrirPantallaDeEdicion(Gasto gasto, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utez/fintree/integradora_final/ModificacionGastoView.fxml"));
            Parent root = loader.load();
            ModificacionGastoController controller = loader.getController();
            controller.initData(gasto);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    void handleRegistrarGasto(ActionEvent event) {
        navegarA("RegistroGastoView.fxml", event);
    }
}