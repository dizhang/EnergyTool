package cn.edu.tsinghua.cs.energytool.iperf.result;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

/**
 * IperfResultReceiver
 * Created by zhangdi on 3/14/14.
 */
public class IperfResultReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(1024);
    private static final StringBuilder logStr2 = new StringBuilder(1024);
    private static final char char_str[] = new char[1024];
    private static final char char_str2[] = new char[1024];
    private Bundle bundle;
    private IperfResultType iperfResultType;
    private String result;
    private String detailResult;
    private long currentTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();

        currentTime = bundle.getLong("CurrentTime");
        iperfResultType = (IperfResultType) bundle.get("Type");
        result = bundle.getString("Result");
        detailResult = bundle.getString("DetailResult");

        if (result == null) {
            result = "null";
        }

        if (detailResult == null) {
            detailResult = "null";
        }

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        logStr.append(result);

        logStr.getChars(0, logStr.length(), char_str, 0);

        logStr2.setLength(0);
        logStr2.append(currentTime);
        logStr2.append("\t");
        logStr2.append(detailResult);

        logStr2.getChars(0, logStr2.length(), char_str2, 0);

        if (iperfResultType == IperfResultType.RIGHT) {
            ETLogger.log(char_str, logStr.length(), ETLoggerType.IPFLOG);
            ETLogger.log(char_str2, logStr2.length(), ETLoggerType.IPDLOG);
        } else {
            ETLogger.log(char_str, logStr.length(), ETLoggerType.IPELOG);
        }
    }
}
