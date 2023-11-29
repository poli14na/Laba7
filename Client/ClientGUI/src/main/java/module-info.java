module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires ConnectionModule;
    requires Entities;
    requires mail;
    requires jfreechart;
    requires jcommon;
    opens com.example to javafx.fxml;
    exports com.example;
}