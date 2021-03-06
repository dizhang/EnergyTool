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

package cn.edu.tsinghua.cs.energytool.attrreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

public class ConnectivityAttrReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(128);
    private static final char char_str[] = new char[128];
    private Bundle bundle;
    private NetworkInfo.DetailedState detailedState;
    private boolean isAvailable;
    private boolean isConnected;
    private boolean isConnectedOrConnecting;
    private long currentTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();
        currentTime = bundle.getLong("CurrentTime");
        detailedState = (NetworkInfo.DetailedState) bundle.get("DetailedState");
        isAvailable = bundle.getBoolean("IsAvailable");
        isConnected = bundle.getBoolean("IsConnected");
        isConnectedOrConnecting = bundle.getBoolean("IsConnectedOrConnecting");

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        logStr.append(detailedState);
        logStr.append("\t");
        logStr.append(isAvailable);
        logStr.append("\t");
        logStr.append(isConnected);
        logStr.append("\t");
        logStr.append(isConnectedOrConnecting);

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.CONLOG);
    }
}
