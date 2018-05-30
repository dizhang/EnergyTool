package cn.edu.tsinghua.cs.energytool.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

/**
 * Created by zhangdi on 11/29/15.
 */
public class ScreenReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(128);
    private static final char char_str[] = new char[128];

    private boolean isScreenOff;
    private long currentTime;

    @Override
    public void onReceive(Context context, Intent intent) {

        currentTime = System.currentTimeMillis();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            isScreenOff = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            isScreenOff = false;
        }

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        if (isScreenOff) {
            logStr.append("off");
        } else {
            logStr.append("on");
        }

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.SCNLOG);
    }
}
