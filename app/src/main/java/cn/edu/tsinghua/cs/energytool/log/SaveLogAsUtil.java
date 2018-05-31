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

import java.io.File;
import java.io.FileFilter;

public class SaveLogAsUtil {

    /**
     * @param folderName save log as folder name
     * @return saveLogAsStatus
     * 0: failed
     * 1: successful
     * 2: failed due to saveAsDir already exists
     * 3: no log files need to save
     * 4: make save as dir error
     */
    public static int saveLogAs(String folderName) {
        int saveLogAsStatus = 0;

        String logDir = ETLoggerUtil.getLogDir();

        if (logDir == null) {
            saveLogAsStatus = 0;
            Log.e("EnergyTool", "SaveLogAs Error for External Storage is unmounted");
        } else {
            try {
                File saveAsDir = new File(logDir + File.separator + folderName);
                if (saveAsDir.exists()) {
                    saveLogAsStatus = 2; // saveAsDir already exists
                } else {
                    File logFolder = new File(logDir);
                    File[] logFiles = logFolder.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            String name = file.getName();
                            return name.endsWith(".log") && file.isFile();
                        }
                    });

                    Log.i("EnergyTool", "LogFile. length = " + logFiles.length);
                    if (logFiles.length == 0) {
                        Log.i("EnergyTool", "No log files need to be saved!");
                        saveLogAsStatus = 3;
                    } else {
                        if (!saveAsDir.mkdir()) {
                            saveLogAsStatus = 4; // create save as directory error
                        } else {
                            // first pause log
                            ETLogger.pauseLog();

                            for (File logFile : logFiles) {
                                File file = new File(logDir + File.separator + logFile.getName());

                                if (file.renameTo(new File(logDir + File.separator + folderName
                                        + File.separator + logFile.getName()))) {
                                    saveLogAsStatus = 1;
                                } else {
                                    saveLogAsStatus = 0;
                                    Log.e("EnergyTool", "Rename log error!");
                                    break;
                                }
                            }

                            // last resume log
                            ETLogger.resumeLog();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("EnergyTool", "SaveLogAs Error");
                saveLogAsStatus = 0;
                ETLogger.resumeLog();
            }
        }

        return saveLogAsStatus;
    }
}
