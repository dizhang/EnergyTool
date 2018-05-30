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
