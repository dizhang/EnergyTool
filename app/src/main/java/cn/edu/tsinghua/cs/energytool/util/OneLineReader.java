package cn.edu.tsinghua.cs.energytool.util;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by zhangdi on 5/12/14.
 */
public class OneLineReader {
    private RandomAccessFile randomAccessFile;
    private Long value;

    public OneLineReader(String filename) {
        try {
            randomAccessFile = new RandomAccessFile(filename, "r");

        } catch (FileNotFoundException e) {
            Log.e("EnergyTool", "File: " + filename + " not found, " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Long getValue() {
        value = null;

        if (randomAccessFile != null) {
            try {
                randomAccessFile.seek(0);

                value = Long.parseLong(randomAccessFile.readLine());

            } catch (IOException e) {
                Log.e("EnergyTool", "Read file exception, " + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException nfe) {
                Log.e("EnergyTool", nfe.getMessage());
                value = null;
            }
        }

        return value;
    }

    public void closeOneLineReader() {
        try {
            if (randomAccessFile != null) {
                randomAccessFile.close();
                randomAccessFile = null;
            }
        } catch (IOException e) {
            Log.e("EnergyTool", "File close error, " + e.getMessage());
            e.printStackTrace();
        }
    }
}
