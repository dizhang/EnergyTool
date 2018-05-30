package cn.edu.tsinghua.cs.energytool.fileservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

/**
 * FileStatusReceiver
 * Created by zhangdi on 1/3/14.
 */
public class FileStatusReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(512);
    private static final char char_str[] = new char[512];
    private Bundle bundle;
    private String fileSize;
    private int type;
    private boolean isOk;
    private String msg;

    private long currentTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();

        currentTime = bundle.getLong("CurrentTime");
        fileSize = bundle.getString("FileSize");
        type = bundle.getInt("Type");
        isOk = bundle.getBoolean("FileStatus");
        msg = bundle.getString("FileMsg");

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        logStr.append(fileSize);
        logStr.append("\t");
        logStr.append(type);
        logStr.append("\t");
        logStr.append(isOk);
        logStr.append("\t");
        logStr.append(msg);

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.FILELOG);
    }
}
