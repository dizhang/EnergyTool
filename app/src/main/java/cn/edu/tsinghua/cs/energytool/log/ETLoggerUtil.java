package cn.edu.tsinghua.cs.energytool.log;

import android.os.Environment;

import java.io.File;

public class ETLoggerUtil {

    protected static String getLogDir() {
        String logDir = null;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();

            logDir = sdCardDir + File.separator + "EnergyTool";

            File logDirFile = new File(logDir);
            if (!logDirFile.exists()) {
                if (!logDirFile.mkdir()) {
                    logDir = null;
                }
            }
        }

        return logDir;
    }
}
