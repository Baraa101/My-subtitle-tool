package com.mycompany.mavenproject1;

import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.net.URL;

public class Color extends Common implements Initializable {
    /***************************************************************************************************************************************************************************** */
    FileWriter fstream;
    String[] colors = { "White", "Red", "Cyan", "Blue", "Green", "Pink", "Yellow", "Black", "Fochsia" },fonts={"Arial","Roboto","Times New Roman","Verdna","SKR HEAD1"};
    String color = "FFFFFF",fontt="Arial";
    int comma,fontComma, rr, gg, bb,s, syncNum, font,colorComma;
    boolean is;
    String rev,rColor,rFont, styleName,styleBoxSelected,fontBoxSelected,temp,r, g, b;
    boolean colorChanged=false,fontChanged=false;
    ArrayList<String> styles = new ArrayList<String>(),styleColors = new ArrayList<String>(),styleFonts = new ArrayList<String>();
    /*
     * ArrayList<String> songColors3C = new ArrayList<String>();
     * ArrayList<String> songColors2C = new ArrayList<String>();
     */
    /***************************************************************************************************************************************************************************** */
    public void sync(File file) throws IOException {
        setReader();
        if (file.toString().endsWith(".srt")) {
            syncSrt();
        } else if (file.toString().endsWith(".ass")) {
            syncAss();
        }
    }

    /***************************************************************************************************************************************************************************** */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeSample("HELLO THERE!");
        colorBox.getItems().addAll(colors);
        fontBox.getItems().addAll(fonts);
        colorBox.setOnAction((event) -> {
            colorBoxAction();
        });
        dir.textProperty().addListener((obsarvable, oldValue, newValue) -> {
            dirAction();
        });
        styleBox.setOnAction((event) -> {
            styleBoxAction();
        });
        fontBox.setOnAction((event) -> {
            fontBoxAction();
        });
        custome.textProperty().addListener((obsarvable, oldValue, newValue) -> {
            customeCoilorAction(oldValue, newValue);
        });
        colorBox.getSelectionModel().select("White");
        styles.add("All");
        styleBox.getItems().addAll(styles);
        styleBox.getSelectionModel().select("All");
    }

    /***************************************************************************************************************************************************************************** */
    private boolean isColor(String str) {
        is = false;
        for (int i = 0; i < str.length(); i++) {
            is = false;
            temp = str.charAt(i) + "";
            for (int j = 0; j <= 15; j++) {
                if (temp.equalsIgnoreCase(Integer.toHexString(j))) {
                    is = true;
                }
            }
        }
        if (is) {
            return true;
        }
        return false;
    }

    /***************************************************************************************************************************************************************************** */
    private String RGBToVBA(String color) {
        if (color.length() == 6) {
            r = color.substring(0, 2);
            g = color.substring(2, 4);
            b = color.substring(4, 6);
        }
        return "&H00" + b + g + r;
    }

    /***************************************************************************************************************************************************************************** */
    private String VBAToRGB(String color) {
        if (color.startsWith("&")) {
            rev = "";
            rev = color.substring(color.indexOf("H") + 3);
            b = rev.substring(0, 2);
            g = rev.substring(2, 4);
            r = rev.substring(4, 6);
            return r + g + b;
        }
        return color;
    }
    /***************************************************************************************************************************************************************************** */
    private void findStyles() throws IOException {
        colorComma = filterStyle("PrimaryColour");
        font = filterStyle("Fontname");
        setReader();
        while ((line = read.readLine()) != null) {
            if (line.startsWith("Style:")) {
                    styles.add(line.substring(line.indexOf(":") + 2, line.indexOf(",")));
                styleColors.add(
                        line.substring(findIndex(line, ',', colorComma) + 1, findIndex(line, ',', colorComma + 1)));
                styleFonts.add(line.substring(findIndex(line, ',', font) + 1, findIndex(line, ',', font + 1)));
            } else if (line.startsWith("[Events]")) {
                break;
            }
        }
    }
    /***************************************************************************************************************************************************************************** */
    private void changeSample(String str) {
        r = color.substring(0, 2);
        g = color.substring(2, 4);
        b = color.substring(4, 6);
        rr = Integer.parseInt(r, 16);
        gg = Integer.parseInt(g, 16);
        bb = Integer.parseInt(b, 16);
        rr = Integer.parseInt(r, 16);
        gg = Integer.parseInt(g, 16);
        bb = Integer.parseInt(b, 16);
        if ((rr + gg + bb) <= 382) {
            sample.setStyle("-fx-text-fill: #" + color+ ";-fx-background-color: white;");
        } else {
            sample.setStyle("-fx-text-fill: #" + color  + ";-fx-background-color: #212121;");
        }
        sample.setFont(Font.font(fontt,20));
        sample.setText(str);
        custome.setText(color);
    }

    /***************************************************************************************************************************************************************************** */
    private void syncAss() throws IOException {
        
        checkSyncedFiles(".ass");
        files += file.getName() + "\n";
        /************************************* */
        comma = filterStyle("PrimaryColour");
        fontComma = filterStyle("Fontname");
        setReader();
        
        while ((line = read.readLine()) != null) {
            if (line.startsWith("Style:")) {
                styleName = line.substring(line.indexOf(":") + 2, line.indexOf(","));
                if (!styleBoxSelected.equals("All")) {
                    line = line.replace(
                            line.substring(findIndex(line, ',', comma) + 1, findIndex(line, ',', comma + 1)),
                            styleColors.get(styles.indexOf(styleName) - 1));
                            line=line.replace(line.substring(findIndex(line, ',', fontComma)+1,findIndex(line, ',', fontComma+1)),styleFonts.get(styles.indexOf(styleName)-1));
                } else {
                    line = line.replace(
                            line.substring(findIndex(line, ',', comma) + 1, findIndex(line, ',', comma + 1)),
                            RGBToVBA(color));
                            line=line.replace(line.substring(findIndex(line, ',', fontComma)+1,findIndex(line, ',', fontComma+1)),fontt);
                }
            }
            if (line != null) {
                if (x==0) {
                    con="";
                    x++;
                }
                con += line + "\n";
            }
        }
        x=0;
        write(con);
        con = "";
    }

    /***************************************************************************************************************************************************************************** */
    private void syncSrt() throws IOException {
        checkSyncedFiles(".srt");
        files += file.getName() + "\n";
        while ((line = read.readLine()) != null) {
            if (line.contains(" --> ") && line.length() >= 22) {
                if (line != null) {
                    con += line + "\n";
                }
                while ((line = read.readLine()) != null && !line.equals(" ") && !line.equals("") && line != null) {
                    if (line.startsWith("<b>") && line.endsWith("</b>")) {
                        if(colorChanged){
                            if (line.contains("color")) {
                                temp=line.substring(line.indexOf("color="));
                                line = line.replace(temp.substring(temp.indexOf("color=")+8,findIndex(temp, '\"', 2)),color);
                            }else{
                                line = line.substring(0,line.indexOf("font")+5)+"color=\"" + "#" + color + "\""+line.substring(line.indexOf("font")+4);
                            }
                        }
                        if(fontChanged){
                            if(line.contains("face=")){
                                temp=line.substring(line.indexOf("face="));
                                line = line.replace(temp.substring(temp.indexOf("face=")+6,findIndex(temp, '\"', 2)),fontt);
                            }else{
                                line = line.substring(0,line.indexOf("font")+5)+"face=\"" +  fontt + "\""+line.substring(line.indexOf("font")+4);
                            }
                        }
                        
                    } else {
                        if(colorChanged&&fontChanged){
                            line = "<b><font color=\"" + "#" + color + "\""+" face= \""+fontt+"\">" + line + "</font></b>";
                        }else if(colorChanged){
                            line = "<b><font color=\"" + "#" + color + "\">" + line + "</font></b>";
                        }else if(fontChanged){
                            line = "<b><font face= \""+fontt+"\">" + line + "</font></b>";
                        }
                        
                    }
                    if (line != null) {
                        con += line + "\n";
                    }
                }
            }
            if (line != null) {
                con += line + "\n";
            }
        }
        write(con);
        con = "";
    }

    /***************************************************************************************************************************************************************************** */
    private void colorBoxAction() {
        if (colorBox.getSelectionModel().getSelectedItem().equals("Cyan")) {
            color = "00FFFF";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Red")) {
            color = "FF0000";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Blue")) {
            color = "0000CD";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Green")) {
            color = "00FF00";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Pink")) {
            color = "FFC0CB";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Yellow")) {
            color = "FFFF00";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Black")) {
            color = "000000";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("White")) {
            color = "FFFFFF";
        } else if (colorBox.getSelectionModel().getSelectedItem().equals("Fochsia")) {
            color = "FF00FF";
        }
        if (styleBoxSelected != null && !styleBoxSelected.equals("All")) {
            styleColors.set(styles.indexOf(styleBoxSelected) - 1, RGBToVBA(color));
            changeSample(styleBoxSelected);
        } else {
            changeSample("HELLO THERE!");
        } 
        colorChanged=true;
    }

    /***************************************************************************************************************************************************************************** */
    private void dirAction() {
        colorChanged=false;
        fontChanged=false;
        styleBox.getItems().clear();
        styleColors.clear();
        styleFonts.clear();
        styles.clear();
        refreshFile();
        clearFilters();
        styles.add("All");
        if (file.isFile() && file.getName().endsWith(".ass")) {
            try {
                findStyles();
            } catch (IOException e) {
            }
            styleBox.getItems().addAll(styles);
            styleBox.getSelectionModel().select("All");
        }
    }
    /***************************************************************************************************************************************************************************** */
    private void styleBoxAction() {
        if (styleBox.getSelectionModel().getSelectedItem() != null) {
            styleBoxSelected = styleBox.getSelectionModel().getSelectedItem();
            if (!styleBoxSelected.equals("All")) {
                rColor = styleColors.get(styles.indexOf(styleBoxSelected) - 1);
                // String sfont = fonts.get(styles.indexOf(styleBoxSelected));
                rColor = VBAToRGB(rColor);
                color = rColor;
                changeSample(styleBoxSelected);
                colorChanged=true;
            }
        }
    }

    private void fontBoxAction() {
        fontChanged=true;
        fontt=fontBox.getSelectionModel().getSelectedItem();
        if (styleBoxSelected != null && !styleBoxSelected.equals("All")) {
            styleFonts.set(styles.indexOf(styleBoxSelected) - 1, fontt);
            changeSample(styleBoxSelected);
        } else {
            changeSample("HELLO THERE!");
        }
    }

    /***************************************************************************************************************************************************************************** */
    private void customeCoilorAction(String oldValue, String newValue) {
        if (!isColor(newValue) && !newValue.equals("")) {
            custome.setText(oldValue);
        }
        if (newValue.length() > 6) {
            custome.setText(oldValue);
        }
        if (newValue.length() == 6 && isColor(newValue) && !newValue.equals("")) {
            color = newValue;
            colorChanged=true;
        }
        if (styleBoxSelected != null && !styleBoxSelected.equals("All")) {
            styleColors.set(styles.indexOf(styleBoxSelected) - 1, RGBToVBA(color));
            changeSample(styleBoxSelected);
        } else if(newValue.length()==6){
            color=newValue;
            changeSample("HELLO THERE!");
        }
    }
}

/*
 * else if(line.contains("move")){
 * String temp=line,col;
 * int i=1;
 * while (temp.contains("3c&")) {
 * System.out.println(i);
 * i++;
 * col=temp.subSequence(temp.indexOf("3c&")+4, temp.indexOf("3c&")+10)+"";
 * if(!songColors3C.contains(col)){
 * songColors3C.add(col);
 * }
 * if(i==1){
 * line= line.replaceAll(col, "D1CE00");
 * 
 * }else if (i==2) {
 * line= line.replaceAll(col, "FFFF00");
 * } else if (i==3){
 * line= line.replaceAll(col, "FFFFE0");
 * }
 * temp=temp.substring(temp.indexOf("3c&")+3);
 * 
 * }
 * temp=line;
 * i=1;
 * while (temp.contains("1c&")) {
 * col=temp.subSequence(temp.indexOf("1c&")+4, temp.indexOf("1c&")+10)+"";
 * if(!songColors2C.contains(col)){
 * songColors2C.add(col);
 * }
 * if(i==1){
 * line= line.replaceAll(col, "CD5A00");
 * 
 * }else if (i==2) {
 * line= line.replaceAll(col, "CB9300");
 * } else if (i==3){
 * line= line.replaceAll(col, "FFF0BE");
 * }
 * temp=temp.substring(temp.indexOf("1c&")+3);
 * }
 * }
 */