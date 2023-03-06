package com.mycompany.mavenproject1;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
public class MainController {

    /************************************************************************************ */
    public void changeSize() throws IOException{
        changeScene("ChngeSize.fxml");
    }
    /************************************************************************************** */
    public void sync() throws IOException{
        changeScene("Sync.fxml");
    }
    /************************************************************************************** */
    public void convert() throws IOException{
        changeScene("Convert.fxml");
    }
    /************************************************************************************** */
    public void color() throws IOException{
        changeScene("Color.fxml");
    }
    public void changeScene(String scene) throws IOException{
        App.root=FXMLLoader.load(getClass().getResource(scene));
        App.scene.setRoot(App.root);
        App.stage.setScene(App.scene);
    }
}
