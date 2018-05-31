/**
 * MIT License
 * <p>
 * Copyright (c) 2018 Di Zhang
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.edu.tsinghua.cs.energytool.log;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ETCommonLogger {

    private static final StringBuilder logStr = new StringBuilder(1024);
    private static final char char_str[] = new char[1024];
    private FileOutputStream fileOutputStream;
    private OutputStreamWriter outputStreamWriter;
    private BufferedWriter bufferedWriter;
    private String logFileName;
    private boolean isLoggerStarted;
    // only for not allocation again
    private boolean isLogged;

    protected ETCommonLogger(ETLoggerType loggerType) {
        this.isLoggerStarted = false;
        this.logFileName = loggerType.getType() + ".log";
    }

    /**
     * startLog
     * if it is started, do nothing
     */
    protected boolean startLog() {
        if (!isLoggerStarted) {
            startLogger();
        }

        return isLoggerStarted;
    }

    /**
     * Log string
     * if logger is not started, start it first and then log the string
     *
     * @param str log string
     */
    protected boolean log(char[] str, int len) {
        return isLoggerStarted && logger(str, len);
    }

    /**
     * flushLog
     * if logger is not started, start it only
     * no need to flush
     */
    protected void flushLog() {
        if (isLoggerStarted) {
            flushLogger();
        }
    }

    /**
     * stopLogger
     */
    protected void stopLog() {
        if (isLoggerStarted) {
            try {
                bufferedWriter.flush();
                outputStreamWriter.flush();
                fileOutputStream.flush();
                bufferedWriter.close();
                outputStreamWriter.close();
                fileOutputStream.close();
            } catch (IOException e) {
                Log.e("EnergyTool", "ETCommonLogger stop error");
                e.printStackTrace();
            }
            isLoggerStarted = false;
        }

        Log.v("EnergyTool", "logger stopped");
    }

    protected boolean deleteLogFile() {
        return !isLoggerStarted && deleteLoggerFile();
    }

    private void startLogger() {
        String logDir = ETLoggerUtil.getLogDir();

        if (logDir == null) {
            isLoggerStarted = false;
            Log.e("EnergyTool", "ETCommonLogger Started Error for External Storage is unmounted");
        } else {
            File logFile = new File(logDir + File.separator + logFileName);

            try {
                fileOutputStream = new FileOutputStream(logFile, false);
                outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                bufferedWriter = new BufferedWriter(outputStreamWriter);
                isLoggerStarted = true;
            } catch (FileNotFoundException e) {
                Log.e("EnergyTool", "ETCommonLogger initialize error");
                e.printStackTrace();
            }
        }
    }

    /**
     * private logger method
     *
     * @param str log string
     */
    private boolean logger(char[] str, int len) {
        isLogged = false;

        //Date date = new Date();
        // delete the readable time format
        //str = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",	Locale.US)).format(date)
        //+ "\t" + date.getTime() + "\t" + str + "\r\n";

        logStr.setLength(0);
        logStr.append(str, 0, len);
        logStr.append("\r\n");

        logStr.getChars(0, logStr.length(), char_str, 0);

        try {
            bufferedWriter.write(char_str, 0, logStr.length());
            isLogged = true;
        } catch (IOException e) {
            Log.e("EnergyTool", "ETCommonLogger log error");
            e.printStackTrace();
        }

        return isLogged;
    }

    /**
     * private flush logger method
     */
    private void flushLogger() {
        try {
            bufferedWriter.flush();
            outputStreamWriter.flush();
            fileOutputStream.flush();
        } catch (IOException e) {
            Log.e("EnergyTool", "ETCommonLogger flush error");
            e.printStackTrace();
        }
    }

    /*
     * for delete unlogged log file
     */
    private boolean deleteLoggerFile() {
        String logDir = ETLoggerUtil.getLogDir();
        boolean isDeleted = false;

        if (logDir == null) {
            Log.e("EnergyTool", "Delete logger file error for External Storage is unmounted");
        } else {
            File logFile = new File(logDir + File.separator + logFileName);

            if (logFile.exists()) {
                isDeleted = logFile.delete();
                Log.v("EnergyTool", "Delete log file: " + logFileName);
            } else {
                Log.v("EnergyTool", "Don't need delete for logFile: " + logFileName + ", doesn't exist");
                isDeleted = true;
            }
        }

        return isDeleted;
    }
}
