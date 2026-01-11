module com.utez.fintree.integradora_final {
    // Permisos para usar los componentes de JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // <-- AÑADE ESTA LÍNEA


    // Permiso para que JavaFX pueda acceder a tu paquete
    opens com.utez.fintree.integradora_final to javafx.fxml;

    // Permiso para que la aplicación se pueda ejecutar
    exports com.utez.fintree.integradora_final;
    exports com.utez.fintree.integradora_final.db;
    opens com.utez.fintree.integradora_final.db to javafx.fxml;
    exports com.utez.fintree.integradora_final.model;
    opens com.utez.fintree.integradora_final.model to javafx.fxml;
    exports com.utez.fintree.integradora_final.controller;
    opens com.utez.fintree.integradora_final.controller to javafx.fxml;
}