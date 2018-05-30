package cn.edu.tsinghua.cs.energytool.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Not used for performance problem, use OneLineReader instead
 */
public class BatteryAttrOneLineReader {
    private static final char buffer[] = new char[1024];
    private static int len;

    public static Long getValue1(File file) {
        Long value = null;

        try {

            FileReader fileReader = new FileReader(file);
            len = 0;
            len = fileReader.read(buffer, 0, 1024);
            fileReader.close();

        } catch (FileNotFoundException e) {
            Log.e("EnergyTool", "File not found" + e.getMessage());
            e.printStackTrace();
        } catch (IOException ie) {
            Log.e("EnergyTool", "IOException" + ie.getMessage());
            ie.printStackTrace();
        }

        try {
            if (len != 0) {
                value = Long.parseLong(String.valueOf(buffer, 0, len - 1));
            }
        } catch (NumberFormatException nfe) {
            Log.e("EnergyTool", nfe.getMessage());
            value = null;
        }

        return value;
    }

    public static Long getValue(File file) {

        Long value = null;
        String text = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr, 16);

            text = br.readLine();

            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            Log.e("EnergyTool", e.getMessage());
            e.printStackTrace();
        }

        if (text != null) {
            try {
                value = Long.parseLong(text);
            } catch (NumberFormatException nfe) {
                Log.e("EnergyTool", nfe.getMessage());
                value = null;
            }
        }

        return value;
    }
}
