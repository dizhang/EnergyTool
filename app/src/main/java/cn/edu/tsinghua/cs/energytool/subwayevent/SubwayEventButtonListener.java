package cn.edu.tsinghua.cs.energytool.subwayevent;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;
import cn.edu.tsinghua.cs.energytool.util.SubwayEventType;

public class SubwayEventButtonListener implements OnClickListener {
    private static final StringBuilder logStr = new StringBuilder(128);
    private static final char char_str[] = new char[128];
    private Context context;
    private SubwayEventType eventType;

    public SubwayEventButtonListener(Context context, SubwayEventType eventType) {
        this.context = context;
        this.eventType = eventType;
    }

    @Override
    public void onClick(View v) {
        logStr.setLength(0);
        logStr.append(System.currentTimeMillis());
        logStr.append("\t");
        logStr.append(eventType.getType());
        logStr.append("\t");
        logStr.append(eventType.getDescription());

        logStr.getChars(0, logStr.length(), char_str, 0);

        int status = ETLogger.log(char_str, logStr.length(), ETLoggerType.SBWLOG);

        String msg;
        switch (status) {
            case 0:
                msg = "Log Event [ " + eventType.getDescription() + " ] Failed!";
                break;
            case 1:
                msg = "Log Event [ " + eventType.getDescription() + " ] Successfully!";
                break;
            case 2:
                msg = "Event [ " + eventType.getDescription() + " ] is not logged due to Logger is Paused!";
                break;
            default:
                msg = "Log Event [ " + eventType.getDescription() + " ] returns unknown status = " + status + " !";
        }

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
