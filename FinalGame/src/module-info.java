module FinalGame {
    requires javafx.fxml;
    requires javafx.controls;
    exports game.model;
    exports game.gui;
    opens game.model;
    opens game.gui;
}