/**
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

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

public class ETLoggerFactory {

    private ETCommonLogger[] logger;
    private boolean[] isLogEnabled;
    private int num;
    private boolean isPaused;

    // only for not allocation again
    private int status;

    /**
     * init logger for different types of log
     */
    public ETLoggerFactory() {
        isPaused = false;
        num = ETLoggerType.values().length;
        logger = new ETCommonLogger[num];
        isLogEnabled = new boolean[num];

        int i = 0;
        for (ETLoggerType type : ETLoggerType.values()) {
            logger[i] = new ETCommonLogger(type);
            isLogEnabled[i] = false;
            i++;
        }
    }

    /**
     * start every logger
     */
    protected void startLog() {
        isPaused = false;
        initLoggersPreferences();
        startLoggers();
    }

    /**
     * @param logStr log string
     * @param type   log type
     * @return log status,
     * 0: log error
     * 1: log successfully
     * 2: log is paused
     */
    protected int log(char[] logStr, int len, ETLoggerType type) {

        if (isPaused) {
            status = 2; // 2 presents loggerFactory is paused
        } else {
            if (isLogEnabled[type.getIndex()]) {
                if (logger[type.getIndex()].log(logStr, len)) {
                    status = 1; // 1 presents loggerFactory do log operation successfully
                } else {
                    status = 0; // 0 presents loggerFactory do log operation failed
                }
            } else {
                status = 3; // 3 presents log is not enabled
            }
        }

        return status;
    }

    /**
     * Pause log need below steps:
     * (1) set isPaused = true;
     * (2) flush log
     * (3) stop log
     * if the logger is paused, logger can also use log method, but is does not
     * do log action.
     */
    protected void pauseLog() {
        if (!isPaused) {
            isPaused = true;
            flushLoggers();
            stopLoggers();
            Log.v("EnergyTool", "ETLoggerFactory PauseLog");
        }
    }

    /**
     * Resume log is just doing start operation
     */
    protected void resumeLog() {
        if (isPaused) {
            startLoggers();
            isPaused = false;
            Log.v("EnergyTool", "ETLoggerFactory ResumeLog");
        }
    }

    /**
     * stop loggers
     */
    protected void stopLog() {
        if (!isPaused) {
            stopLoggers();
        }
    }

    /**
     * init log preferences
     */
    private void initLoggersPreferences() {

        /*
         * init all as false
         */
        for (int i = 0; i < num; i++) {
            isLogEnabled[i] = false;
        }

        // check if log is enabled
        // below must be correspond with specific type

        EnergyToolSharedPreferences sharedPreferences = new EnergyToolSharedPreferences();

        if (sharedPreferences.isBatteryServiceEnable()) {
            isLogEnabled[ETLoggerType.BATLOG.getIndex()] = true;
        }

        if (sharedPreferences.isCellServiceEnable()) {
            isLogEnabled[ETLoggerType.CELLOG.getIndex()] = true;
            isLogEnabled[ETLoggerType.NCLLOG.getIndex()] = true;
        }

        if (sharedPreferences.isNetworkServiceEnable()) {
            isLogEnabled[ETLoggerType.NETLOG.getIndex()] = true;
        }

        if (sharedPreferences.isFileServiceEnable()) {
            isLogEnabled[ETLoggerType.FILELOG.getIndex()] = true;
        }

        if (sharedPreferences.isSensorServiceEnable()) {
            isLogEnabled[ETLoggerType.ACCLOG.getIndex()] = true;
            isLogEnabled[ETLoggerType.LACLOG.getIndex()] = true;
        }

        if (sharedPreferences.isConnectivityServiceEnable()) {
            isLogEnabled[ETLoggerType.CONLOG.getIndex()] = true;
        }

        if (sharedPreferences.isIperfServiceEnable()) {
            isLogEnabled[ETLoggerType.IPFLOG.getIndex()] = true;
            isLogEnabled[ETLoggerType.IPDLOG.getIndex()] = true;
            isLogEnabled[ETLoggerType.IPELOG.getIndex()] = true;
        }

        if (sharedPreferences.isSubwayEventEnable()) {
            isLogEnabled[ETLoggerType.SBWLOG.getIndex()] = true;
        }

        // enable screen log
        isLogEnabled[ETLoggerType.SCNLOG.getIndex()] = true;
    }

    /**
     * start all loggers
     */
    private void startLoggers() {
        for (int i = 0; i < num; i++) {
            if (isLogEnabled[i]) {
                if (!logger[i].startLog()) {
                    Log.e("EnergyTool", "Start log error, the log index is: " + i);
                }
            } else {
                if (!logger[i].deleteLogFile()) {
                    Log.e("EnergyTool", "Delete logFile error, the log index is: " + i);
                }
            }
        }

        Log.v("EnergyTool", "ETLoggerFactory StartLog");
    }

    /**
     * flush all loggers
     */
    private void flushLoggers() {
        for (int i = 0; i < num; i++) {
            if (isLogEnabled[i]) {
                logger[i].flushLog();
            }
        }
    }

    /**
     * stop all loggers
     */
    private void stopLoggers() {
        for (int i = 0; i < num; i++) {
            if (isLogEnabled[i]) {
                logger[i].stopLog();
            }
        }

        Log.v("EnergyTool", "ETLoggerFactory Stopped");
    }
}
