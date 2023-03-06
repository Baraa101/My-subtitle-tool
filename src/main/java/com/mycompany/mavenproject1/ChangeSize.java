package com.mycompany.mavenproject1;

import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.io.IOException;
import java.io.File;
import java.net.URL;

public class ChangeSize extends Common implements Initializable{
    // Class Vars
    /***************************************************************************************************************************************************************************** */
    String size;
    int comma,nextComma,commaLoc;
    String multSize;
    /***************************************************************************************************************************************************************************** */
    public void sync(File file) throws IOException {
        if(text2.getText()==null||text2.getText().equals("")){
            size="100";
        }else{
            size=text2.getText();
        }
        multSize=size;
        if ((file.toString().toLowerCase().endsWith(".ass"))) {
            checkSyncedFiles(".ass");
            files += file.getName() + "\n";
            comma = filterStyle("Fontsize");
            setReader();
            while ((line = read.readLine()) != null) {
                if (line.startsWith("Style:")) {
                    commaLoc=findIndex(line, ',', comma);
                    nextComma=findIndex(line, ',', comma + 1);
                    size = Double
                            .valueOf(line.substring(commaLoc+1, nextComma))
                            * (Double.valueOf(multSize) / 100) + "";
                            System.out.println(size);
                    size=form.format(Double.valueOf(size));
                    line = line.replace(
                            line.substring(commaLoc + 1, nextComma), size);
                }
                if (line != null) {
                    if (x == 0) {
                        con = "";
                        x++;
                    }
                    con += line + "\n";}
            }
            x = 0;
            write(con);
            con="";
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text2.textProperty().addListener((Observable,oldValue,newValue) ->{
            if(newValue.length()>=1){
                if(!isNumber(newValue.substring(newValue.length()-1))){
                    text2.setText(oldValue);
                }else if(Double.valueOf(newValue)>1000||Double.valueOf(newValue)<=0){
                    text2.setText(oldValue);
                }
            }
        });
        
    }
    
}
