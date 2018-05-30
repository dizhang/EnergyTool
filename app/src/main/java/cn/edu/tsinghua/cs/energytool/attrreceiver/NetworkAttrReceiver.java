package cn.edu.tsinghua.cs.energytool.attrreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

public class NetworkAttrReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(128);
    private static final char char_str[] = new char[128];
    private Bundle bundle;
    private long rxSpeed;
    private long txSpeed;
    private long currentTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();
        currentTime = bundle.getLong("CurrentTime");
        rxSpeed = bundle.getLong("RxSpeed");
        txSpeed = bundle.getLong("TxSpeed");

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        logStr.append(rxSpeed);
        logStr.append("\t");
        logStr.append(txSpeed);

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.NETLOG);

    }

}
