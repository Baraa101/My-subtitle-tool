package com.mycompany.mavenproject1;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class Sync extends Common implements Initializable {
    // FXML Variables
    /************************************************ */
    @FXML
    private TextField firstDi;
    @FXML
    private TextField seg;
    /******************************************** */
    int s, syncNum = 1, hrs, mins, count, skip;
    double timeInSec, difInSec, segLines, secs;
    String time;
    double[] entered;
    boolean first;
    double[] start, end;
    String startTime, endTime, dirTxt, str, sec, min, hr;
    double startTimeInSec, endTimeInSec;

    /***************************************************************************************************************************************************************************** */
    public void sync(File file) throws IOException {
        setReader();
        if (file.toString().endsWith(".ass")) {
            syncAss();
        } else if (file.toString().endsWith(".srt")) {
            syncSrt();
        }
    }

    /***************************************************************************************************************************************************************************** */
    public String turnToString(double t) {
        hrs = 0;
        mins = 0;
        secs = 0;
        while (t / 3600 >= 1) {
            hrs++;
            t -= 3600;
        }
        while (t / 60 >= 1) {
            mins++;
            t -= 60;
        }
        while (t >= 1) {
            secs++;
            t--;
        }
        if (t > 0) {
            secs += t;
        }
        str = form.format(secs);
        return "" + (hrs / 10 >= 1 ? hrs : "0" + hrs) + ":" + (mins / 10 >= 1 ? mins : "0" + mins) + ":"
                + (secs / 10 >= 1 ? str : "0" + str);
    }

    /***************************************************************************************************************************************************************************** */
    public boolean hasFoure(String line) {
        count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ':') {
                count++;
            }
        }
        if (count == 4) {
            return true;
        } else {
            return false;
        }
    }

    /***************************************************************************************************************************************************************************** */

    /***************************************************************************************************************************************************************************** */
    public void syncAss() throws IOException {
            time = firstDi.getText();
            entered = sepTime(time);
            timeInSec = entered[2] + entered[1] * 60 + entered[0] * 3600;
        if (isNumber(seg.getText()) && !seg.getText().equals("")) {
            segLines = Integer.valueOf(seg.getText());
        } else {
            segLines = 0;
        }
        checkSyncedFiles(".ass");
        files += file.getName() + "\n";
        skip = 0;
        first = true;
        setReader();
        while ((line = read.readLine()) != null) {
            if (line.startsWith("Dialogue:")) {
                while (skip < segLines) {
                    if (line != null) {
                        con += line + "\n";
                    }
                    line = read.readLine();
                    skip++;
                }
                start = new double[3];
                end = new double[3];
                startTime = "";
                endTime = "";
                startTimeInSec = 0;
                endTimeInSec = 0;
                if (line.startsWith("Dialogue:")) {
                    startTime = line.substring(line.indexOf(",") + 1, findIndex(line, ',', 2));
                    endTime = line.substring(findIndex(line, ',', 2) + 1, findIndex(line, ',', 3));
                    start = sepTime(startTime);
                    end = sepTime(endTime);
                    startTimeInSec = start[2] + start[1] * 60 + start[0] * 3600;
                    endTimeInSec = end[2] + end[1] * 60 + end[0] * 3600;
                    if (first) {
                        difInSec = timeInSec - startTimeInSec;
                        first = false;
                    }
                    startTimeInSec += difInSec;
                    endTimeInSec += difInSec;
                    line = line.substring(0, line.indexOf(",") + 1) + turnToString(startTimeInSec)
                            + ","
                            + turnToString(endTimeInSec)
                            + line.substring(findIndex(line, ',', 3));
                }
            }
            if (line != null) {
                if (x == 0) {
                    con = "";
                    x++;
                }
                con += line + "\n";
            }

        }
        x = 0;
        write(con);
        con = "";
    }

    /***************************************************************************************************************************************************************************** */
    public void syncSrt() throws IOException {
            time = firstDi.getText();
            entered = sepTime(time);
            timeInSec = entered[2] + entered[1] * 60 + entered[0] * 3600;
        if (isNumber(seg.getText()) && !seg.getText().equals("")) {
            segLines = Integer.valueOf(seg.getText());
        } else {
            segLines = 0;
        }
        checkSyncedFiles(".srt");
        files += file.getName() + "\n";
        skip = 0;
        first = true;
        while ((line = read.readLine()) != null) {
            while (skip < segLines * 4) {
                if (line != null) {
                    con += line + "\n";
                }
                line = read.readLine();
                skip++;
            }
            start = new double[3];
            end = new double[3];
            startTime = "";
            endTime = "";
            startTimeInSec = 0;
            endTimeInSec = 0;
            if (line.contains(" --> ") && line.length() >= 22) {
                startTime = line.substring(0, line.indexOf("-") - 1);
                endTime = line.substring(line.indexOf(">") + 2);
                start = sepTime(startTime);
                end = sepTime(endTime);
                startTimeInSec = start[2] + start[1] * 60 + start[0] * 3600;
                endTimeInSec = end[2] + end[1] * 60 + end[0] * 3600;
                if (first) {
                    difInSec = timeInSec - startTimeInSec;
                    first = false;
                }
                startTimeInSec += difInSec;
                endTimeInSec += difInSec;
                line = turnToString(startTimeInSec) + line.substring(line.indexOf("-") - 1, line.indexOf(">") + 2)
                        + turnToString(endTimeInSec);
                line = line.replace(".", ",");
            }
            if (line != null) {
                con += line + "\n";
            }
        }
        write(con);
        con = "";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        seg.textProperty().addListener((obsarvable, oldValue, newValue) -> {
            if (newValue.length() >= 1) {
                if (!isNumber(newValue.substring(newValue.length() - 1))
                        || newValue.substring(newValue.length() - 1).equals(".")) {
                    seg.setText(oldValue);
                }else if (Integer.valueOf(newValue) > 50) {
                    seg.setText(oldValue);
                }
            }
        });
        firstDi.textProperty().addListener((obsarvable, oldValue, newValue) -> {
            if (newValue.length() >= 1) {
            if (!(isNumber(newValue.substring(newValue.length() - 1))
                    || newValue.substring(newValue.length() - 1).equals(":"))) {
                firstDi.setText(oldValue);
            }
        }
        });

    }
}
