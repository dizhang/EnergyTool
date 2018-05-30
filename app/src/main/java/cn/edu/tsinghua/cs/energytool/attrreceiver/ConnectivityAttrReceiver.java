package cn.edu.tsinghua.cs.energytool.attrreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

/**
 * ConnectivityAttrReceiver
 * Created by zhangdi on 2/18/14.
 */
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
