package com.mycompany.mavenproject1;

import java.util.Collections;
import java.util.Comparator;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class Convert extends Common {
    // Class Variables
    /***************************************************************************************************************************************************************************** */
    String dirTxt;
    double[] start1, start2;
    String startTime, endTime, dialogue,temp;
    int count = 1;
    double syncNum = 1, start1Srcs, start2Sec;
    int start, end, text;
    String[] str = new String[3];
    boolean hasnext,rep;
    ArrayList<String[]> lines = new ArrayList<String[]>();
    ArrayList<String[]> dialogues = new ArrayList<String[]>();
    /***************************************************************************************************************************************************************************** */
    public void sync(File file) throws IOException {
        lines.clear();
        if (file.toString().endsWith(".ass")) {
            start = filterEvents("Start");
            end = filterEvents("End");
            text = filterEvents("Text");
            setReader();
            checkSyncedFiles(".ass");
            files += file.getName() + "\n";
            while ((line = read.readLine()) != null) {
                str=new String[3];
                startTime = "";
                endTime = "";
                if (line.startsWith("Dialogue:")) {
                    startTime = line.substring(findIndex(line, ',', start) + 1, findIndex(line, ',', start + 1));
                    endTime = line.substring(findIndex(line, ',', end) + 1, findIndex(line, ',', end + 1));
                    if (text == eventFilter.size() - 1) {
                        dialogue = line.substring(findIndex(line, ',', text) + 1);
                    } else {
                        dialogue = line.substring(findIndex(line, ',', text) + 1, findIndex(line, ',', text + 1));
                    }
                    while (dialogue.contains("{") && dialogue.contains("}")) {
                        dialogue = dialogue.substring(dialogue.indexOf("}") + 1);
                    }
                    str [0]= startTime;
                    str [1]= endTime;
                    str [2]= dialogue;
                    lines.add(str);
                }
            }
            Collections.sort(lines, new Comparator<String[]>() {
                @Override
                public int compare(String[] o1, String[] o2) {
                    start1 = sepTime(o1[0]);
                    start2 = sepTime(o2[0]);
                    start1Srcs = start1[2] + start1[1] * 60 + start1[0] * 3600;
                    start2Sec = start2[2] + start2[1] * 60 + start2[0] * 3600;
                    if (start1Srcs > start2Sec) {
                        return 1;
                    } else if (start1Srcs < start2Sec) {
                        return -1;
                    }
                    return 0;
                }
            });
            hasnext = false;
            rep = false;
            con = "";
            count=0;
            for (String[] strings : lines) {
                    for (String[] string : dialogues) {
                        if (strings[0].equals(string[0]) && strings[1].equals(string[1]) && strings[2].equals(string[2])) {
                            rep = true;
                            break;
                        }
                    }
                    if (!rep&&valid(strings[2])) {
                        temp = strings[2].toLowerCase();
                        hasnext = temp.contains("\\n");
                        con += count + "\n" + strings[0] + " --> " + strings[1] + "\n";
                        if (hasnext) {
                            while (hasnext) {
                                con += temp.substring(0, temp.indexOf("\\n")) + "\n";
                                temp = temp.substring(temp.indexOf("\\n") + 2);
                                hasnext = temp.contains("\\n");
                            }
                            con += temp + "\n";
                        } else {
                            con += strings[2] + "\n";
                        }
                        con += "\n";
                        count++;
                        dialogues.add(strings);
                    }
                    rep = false;
                
            }
            write(con);
            con = "";
            count = 1;
        }
    }
}
