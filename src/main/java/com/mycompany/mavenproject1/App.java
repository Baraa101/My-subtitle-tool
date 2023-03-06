package com.mycompany.mavenproject1;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    static Stage stage;
    static Parent root;
    static Scene scene;

    /****************************************************************** */
    public static void main(String[] args) {
        launch(args);
    }
    /****************************************************************** */
    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        Image image = new Image(getClass().getResource("subtitles.png").toURI().toString());
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Simpel subtitle tool");
        stage.setScene(scene);
        stage.show();
    }
}