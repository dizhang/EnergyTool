package cn.edu.tsinghua.cs.energytool.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

/**
 * AccelerometerSensorListener
 * Created by zhangdi on 4/24/14.
 */
public class AccelerometerSensorListener implements SensorEventListener {
    private static final StringBuilder logStr = new StringBuilder(256);
    private static final char char_str[] = new char[256];
    private float[] values;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        values = sensorEvent.values;

        logStr.setLength(0);
        logStr.append(System.currentTimeMillis());
        logStr.append("\t");
        logStr.append(values[0]);
        logStr.append("\t");
        logStr.append(values[1]);
        logStr.append("\t");
        logStr.append(values[2]);

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.ACCLOG);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
