package cn.edu.tsinghua.cs.energytool.attrreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

public class BatteryAttrReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(128);
    private static final char char_str[] = new char[128];
    private Bundle bundle;
    private long currentTime;
    private long current;
    private long voltage;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();

        currentTime = bundle.getLong("CurrentTime");
        current = bundle.getLong("Current");
        voltage = bundle.getLong("Voltage");

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        logStr.append(current);
        logStr.append("\t");
        logStr.append(voltage);

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.BATLOG);
    }
}
