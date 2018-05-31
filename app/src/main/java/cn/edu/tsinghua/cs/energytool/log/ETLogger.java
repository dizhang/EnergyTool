/**
 * MIT License
 *
 * Copyright (c) 2018 Di Zhang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
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
