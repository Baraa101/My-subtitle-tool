package com.mycompany.mavenproject1;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.io.FileNotFoundException;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import javafx.stage.FileChooser;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import java.io.Reader;
import java.io.File;

public abstract class Common {
    @FXML
    ChoiceBox<String> colorBox, styleBox, fontBox;
    @FXML
    RadioButton rad;
    @FXML
    TextField text2, firstDi, seg, custome, sample, dir;
    @FXML
    TextArea textArea;
    @FXML
    Button actionButton;
    @FXML
    Button btn;
    @FXML
    Label lab1, lab2, lab3;
    /***************************************************************************************************************************************************************************** */
    private DirectoryChooser dirChooser = new DirectoryChooser();
    private BufferedWriter writer;
    private FileChooser fileChooser = new FileChooser();
    ArrayList<String> styleFilter = new ArrayList<String>(), eventFilter = new ArrayList<String>();
    private boolean go = false;
    private Reader reader;
    BufferedReader read;
    DecimalFormat form = new DecimalFormat("0.00");
    private int ind, digits;
    String line, con, rev, dirTxt, secc, minn, hrss, currentClass, files = "";
    double[] hMs = new double[3];
    double count, sec, min, hrs;
    File[] fileList;
    File file, newFile, foldar;
    int finished = 0, filesCount = 0, syncNum = 1;
    int s, z;
    double timeInSec, difInSec, segLines;
    String time;
    double[] entered;
    boolean first;
    double[] start, end;
    String startTime, endTime;
    double startTimeInSec, endTimeInSec;
    int x = 0;
    /******************************** */
    private Thread thread = new Thread();
    /***************************************************************************************************************************************************************************** */
    public void back() throws IOException {
        thread.stop();
        App.root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        App.scene.setRoot(App.root);
        App.stage.setScene(App.scene);
    }
    /***************************************************************************************************************************************************************************** */
    public void sync(File f) throws Exception {
    }
    /***************************************************************************************************************************************************************************** */
    public void multiFile() throws IOException {
        fileList = file.listFiles();
        foldar = new File(file.toPath() + "\\Sync Foldar");
        if (!foldar.exists()) {
            foldar.mkdir();
        }
        boolean forAss = currentClass.equals("ChangeSize") || currentClass.equals("Convert");
        for (File file : fileList) {
            if ((forAss && file.getName().endsWith(".ass")) || (!forAss && checkIfSub(file))) {
                filesCount++;
            }
        }
        for (File f : fileList) {
            if (!f.getName().endsWith(".ass") && forAss) {
                continue;
            }
            if (checkIfSub(f)) {
                file = f;
                try {
                    sync(file);
                } catch (Exception e) {
                    System.out.println(e);
                }
                finished++;
                try {
                    thread.sleep(50);
                } catch (InterruptedException e1) {
                }
                writeToTextArea(files);
                // System.out.println(file.getName());
            }
        }

    }
    /***************************************************************************************************************************************************************************** */
    public void search() {
        if (rad.isSelected()) {
            try {
                file = dirChooser.showDialog(null);
                dir.setText(file.getPath());
            } catch (Exception e) {
            }
        } else {
            try {
                file = fileChooser.showOpenDialog(null);
                dir.setText(file.getPath());
            } catch (Exception e) {
            }
        }
    }
    /***************************************************************************************************************************************************************************** */
    public void change() throws Exception {
        if (rad.isSelected()) {
            btn.setText("Folder");
        } else {
            btn.setText("File");
        }
    }
    /***************************************************************************************************************************************************************************** */
    public void select() {
        con = "";
        files = "";
        con = dir.getText();
        con.replace("\\", "/");
        if (con.startsWith("\"") && con.endsWith("\"")) {
            con = con.substring(con.indexOf("\"") + 1, con.lastIndexOf("\""));
        }
        file = new File(con);
        clearFilters();
    }
    /***************************************************************************************************************************************************************************** */
    public int findIndex(String line, char c, int x) {
        ind = 1;
        for (int i = 0; i < line.length(); i++) {
            if (ind == x && line.charAt(i) == c) {
                return i;
            }
            if (line.charAt(i) == c) {
                ind++;
            }
        }
        return -1;
    }
    /***************************************************************************************************************************************************************************** */
    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }
    /***************************************************************************************************************************************************************************** */
    public void action2() throws IOException {
        refreshFile();
        try {
            thread.sleep(50);
        } catch (InterruptedException e1) {
        }
        currentClass = this.getClass().getName();
        currentClass = currentClass.substring(currentClass.lastIndexOf(".") + 1);
        if (currentClass.equals("Sync")) {
            if (file.exists()) {
                    if (!seg.getText().equals("")) {
                        segLines = Integer.valueOf(seg.getText());
                    } else {
                        segLines = 0;
                    }
                    if (firstDi.getText() != null && !firstDi.getText().equals("") && isTime(firstDi.getText())) {
                        go = true;
                        time = firstDi.getText();
                    entered = sepTime(time);
                    timeInSec = entered[2] + entered[1] * 60 + entered[0] * 3600;
                    } else {
                        firstDi.setText("");
                        firstDi.setPromptText("Invalid");
                    }
                    
            }
        } else if (currentClass.equals("ChangeSize")) {
            if (text2.getText() != null && !text2.getText().equals("")) {
                go = true;
            } else {
                text2.setText("");
                text2.setPromptText("Enter here");
            }
        }else if(currentClass.equals("Convert")){
            if(file.getName().endsWith(".ass")){
                go=true;
            }
        } else {
            go = true;
        }
        if (go) {
            filesCount = 0;
            finished = 0;
            files = "";
            select();
            refreshFile();
            if (file.exists()) {
                try {
                    check();
                } catch (Exception e) {
                }
            } else if ((dir.getText().equals("") || dir.getText() == null)) {
                dir.setPromptText("Please Enter a File");
            } else {
                dir.setText("");
                dir.setPromptText("File not found");
            }
        }
    }
    /***************************************************************************************************************************************************************************** */
    public void action() {
        writeToTextArea("");
        thread = new Thread(() -> {
            try {
                action2();
            } catch (IOException e) {
            }
        });
        thread.start();
    }
    /***************************************************************************************************************************************************************************** */
    public int filterEvents(String request) throws IOException {
        if (eventFilter.isEmpty()) {
            setReader();
            while ((line = read.readLine()) != null) {
                if (line.equals("[Events]")) {
                    line = read.readLine();
                    break;
                } else if (line.startsWith("Dialogue")) {
                    break;
                }
            }
            line = line.substring(8, line.length());
            do {
                eventFilter.add(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
            } while (line.contains(","));
            eventFilter.add(line);
        }
        for (String string : eventFilter) {
            if (string.contains(request)) {
                return eventFilter.indexOf(string);
            }
        }
        return -1;
    }
    /***************************************************************************************************************************************************************************** */
    public int filterStyle(String request) throws IOException {
        if (styleFilter.isEmpty()) {
            setReader();
            while ((line = read.readLine()) != null) {
                if (line.equals("[V4+ Styles]")) {
                    line = read.readLine();
                    break;
                } else if (line.startsWith("[Events]")) {
                    break;
                }
            }
            line = line.substring(8, line.length());
            do {
                styleFilter.add(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
            } while (line.contains(","));
            styleFilter.add(line);
        }
        for (String string : styleFilter) {
            if (string.contains(request)) {
                return styleFilter.indexOf(string);
            }
        }
        return -1;
    }
    /***************************************************************************************************************************************************************************** */
    public String revString(String request) {
        rev = "";
        for (int i = request.length() - 1; i >= 0; i--) {
            rev += request.charAt(i);
        }
        return rev;
    }
    /***************************************************************************************************************************************************************************** */
    public void view() {
        lab1.setVisible(true);
    }
    /***************************************************************************************************************************************************************************** */
    public void hide() {
        lab1.setVisible(false);
    }
    /***************************************************************************************************************************************************************************** */
    public void view2() {
        lab2.setVisible(true);
    }
    /***************************************************************************************************************************************************************************** */
    public void hide2() {
        lab2.setVisible(false);
    }
    /****************************************************************** */
    public void view3() {
        lab3.setVisible(true);
    }
    /****************************************************************** */
    public void hide3() {
        lab3.setVisible(false);
    }
    /***************************************************************************************************************************************************************************** */
    public boolean valid(String line) {
        count = 0;
        digits = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                count++;
            } else if (isNumber(line.charAt(i) + "")) {
                digits++;
            }
        }
        return (count / line.length() < 0.5 && line.length() <= 50) || digits < 20;
    }
    /***************************************************************************************************************************************************************************** */
    public void setReader() throws UnsupportedEncodingException, FileNotFoundException {
        reader = new InputStreamReader(new FileInputStream(file), "UTF8");
        read = new BufferedReader(reader);
    }
    /***************************************************************************************************************************************************************************** */
    public void write(String con) throws IOException {
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), StandardCharsets.UTF_8));
        writer.write(con);
        writer.close();
    }
    /***************************************************************************************************************************************************************************** */
    public void writeToTextArea(String str) {
        textArea.setEditable(true);
        if (filesCount == finished) {
            textArea.setText(filesCount + "\\" + finished
                    + "                                                                   [DONE!]" + "\n" + str);
        } else {
            textArea.setText(filesCount + "\\" + finished + "\n" + str);
        }
        textArea.setEditable(false);
    }
    /***************************************************************************************************************************************************************************** */
    public void check() throws Exception {
        if (file.isFile()) {
            filesCount=1;
            try {
                foldar = new File(
                        (file.toPath() + "").substring(0, file.getPath().lastIndexOf("\\") + 1) + "Sync Foldar");
                if (!foldar.exists()) {
                    foldar.mkdir();
                }
                sync(file);
                finished=1;
                writeToTextArea(files);
            } catch (IOException e) {
            }
        } else if (file.isDirectory()) {
            multiFile();
        }
    }
    /***************************************************************************************************************************************************************************** */
    public double[] sepTime(String time) {
        hMs = new double[3];
        time = time.replaceAll(" ", "");
        time = time.replaceAll(",", ".");
        secc = "";
        minn = "";
        hrss = "";
        sec = 0;
        min = 0;
        hrs = 0;
        if (!time.contains(":")) {
            if (isNumber(time)) {
                sec = Double.valueOf(time);
            }
        } else if (time.indexOf(":") == time.lastIndexOf(":")) {
            minn = time.substring(0, time.indexOf(":"));
            secc = time.substring(time.indexOf(":") + 1);
            if (isNumber(secc) && isNumber(minn)) {
                sec = Double.valueOf(secc);
                min = Double.valueOf(minn);
            }
        } else if (time.lastIndexOf(":") - time.indexOf(":") <= 3) {
            hrss = time.substring(0, time.indexOf(":"));
            minn = time.substring(time.indexOf(":") + 1, time.lastIndexOf(":"));
            secc = time.substring(time.lastIndexOf(":") + 1);
            if (isNumber(secc) && isNumber(minn) && isNumber(hrss)) {
                sec = Double.valueOf(secc);
                min = Double.valueOf(minn);
                hrs = Double.valueOf(hrss);
            }
        }
        hMs[0] = hrs;
        hMs[1] = min;
        hMs[2] = sec;
        return hMs;
    }
    /***************************************************************************************************************************************************************************** */
    public void clearFilters() {
        eventFilter.clear();
        styleFilter.clear();
    }
    /***************************************************************************************************************************************************************************** */
    public void refreshFile() {
        dirTxt = dir.getText();
        dirTxt.replace("\\", "/");
        if (dirTxt.startsWith("\"") && dirTxt.endsWith("\"")) {
            dirTxt = dirTxt.substring(dirTxt.indexOf("\"") + 1, dirTxt.lastIndexOf("\""));
        }
        file = new File(dirTxt);
    }
    /***************************************************************************************************************************************************************************** */
    public boolean checkIfSub(File file) {
        if (file.isFile() && (file.getName().endsWith(".srt") || file.getName().endsWith(".ass"))) {
            return true;
        }
        return false;
    }
    /***************************************************************************************************************************************************************************** */
    public void checkSyncedFiles(String type) throws IOException {
        String fileName;
        if (!file.getName().substring(0, file.getName().indexOf(type)).endsWith("(synced)")) {
            fileName=foldar + "\\"
            + file.getName().substring(0, file.getName().toLowerCase().indexOf(type)) + " --" + syncNum
            + "--  (synced)" + type;
        } else {
            syncNum = Integer.valueOf(
                    file.getName().substring(file.getName().indexOf(" --") + 3, file.getName().indexOf("-- ")));
                    fileName=foldar + "\\"
                    + file.getName().substring(0, file.getName().toLowerCase().indexOf(" --")) + " --"
                    + (syncNum + 1) + "-- " + " (synced)" + type;
        }
        if(currentClass.equals("Convert")){
            fileName=fileName.replace(".ass", ".srt");
        }
        newFile = new File(fileName);
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
    }
    public static boolean isTime(String str) {
        String sec, min, hr;
        if (!str.contains(":")) {
            sec = str;
            if (isNumber(str) && Double.valueOf(sec) >= 0 && Double.valueOf(sec) <= 60) {
                return true;
            }
        } else if (str.indexOf(":") == str.lastIndexOf(":")) {
            min = str.substring(0, str.indexOf(":"));
            sec = str.substring(str.indexOf(":") + 1);
            if (isNumber(min) && Double.valueOf(min) >= 0
                    && Double.valueOf(min) <= 60 && isNumber(sec) && Double.valueOf(sec) >= 0
                    && Double.valueOf(sec) <= 60) {
                return true;
            }
        } else if (str.indexOf(":") != str.lastIndexOf(":")) {
            hr = str.substring(0, str.indexOf(":"));
            min = str.substring(str.indexOf(":") + 1, str.lastIndexOf(":"));
            sec = str.substring(str.lastIndexOf(":") + 1);
            if (isNumber(min) && Double.valueOf(min) >= 0
                    && Double.valueOf(min) <= 60 && isNumber(sec) && Double.valueOf(sec) >= 0
                    && Double.valueOf(sec) <= 60 && Double.valueOf(hr) >= 0 && Double.valueOf(hr) <= 24) {
                return true;
            }
        }
        return false;
    }
}
