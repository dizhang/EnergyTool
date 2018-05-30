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
