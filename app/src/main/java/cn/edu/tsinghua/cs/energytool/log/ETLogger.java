package cn.edu.tsinghua.cs.energytool.log;

import android.util.Log;

public class ETLogger {
    private static final ETLoggerFactory loggerFactory = new ETLoggerFactory();
    private static boolean isLogStarted;

    static {
        isLogStarted = false;
    }

    public static void startLog() {
        if (!isLogStarted) {
            loggerFactory.startLog();
            isLogStarted = true;
            Log.v("EnergyTool", "ETLogger started");
        } else {
            Log.v("EnergyTool", "ETLogger has already started");
        }

    }

    public static int log(char[] logStr, int len, ETLoggerType type) {
        if (isLogStarted) {
            return loggerFactory.log(logStr, len, type);
        } else {
            return -1; // -1 represents log is not started
        }
    }

    public static void pauseLog() {
        if (isLogStarted) {
            loggerFactory.pauseLog();
        }
    }

    public static void resumeLog() {
        if (isLogStarted) {
            loggerFactory.resumeLog();
        }
    }

    public static void stopLog() {
        loggerFactory.stopLog();
        isLogStarted = false;
        Log.v("EnergyTool", "ETLogger stopped");
    }

}
