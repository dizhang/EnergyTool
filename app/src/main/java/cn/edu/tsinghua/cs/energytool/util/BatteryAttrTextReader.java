package cn.edu.tsinghua.cs.energytool.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class BatteryAttrTextReader {

    public static Long getValue(File file, String attributeField) {

        String text;
        Long value = null;

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            while (line != null) {

                if (line.contains(attributeField)) {
                    text = line.substring(line.indexOf(attributeField)
                            + attributeField.length());
                    text = text.substring(0, text.length() - 1);

                    try {
                        value = Long.parseLong(text);
                        if (value != 0)
                            break;
                    } catch (NumberFormatException nfe) {
                        Log.e("EnergyTool", nfe.getMessage(), nfe);
                    }
                }

                line = br.readLine();
            }

            br.close();
            fr.close();

        } catch (Exception ex) {
            Log.e("EnergyTool", ex.getMessage(), ex);
        }

        return value;
    }
}
