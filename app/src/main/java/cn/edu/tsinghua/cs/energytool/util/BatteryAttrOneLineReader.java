/**
 Copyright (c) 2018 Di Zhang

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

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
