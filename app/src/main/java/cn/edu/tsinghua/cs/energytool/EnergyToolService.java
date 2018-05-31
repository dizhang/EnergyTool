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

package cn.edu.tsinghua.cs.energytool;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import cn.edu.tsinghua.cs.energytool.attrreceiver.BatteryAttrReceiver;
import cn.edu.tsinghua.cs.energytool.attrreceiver.CellAttrReceiver;
import cn.edu.tsinghua.cs.energytool.attrreceiver.ConnectivityAttrReceiver;
import cn.edu.tsinghua.cs.energytool.attrreceiver.NetworkAttrReceiver;
import cn.edu.tsinghua.cs.energytool.attrservice.BatteryAttributeService;
import cn.edu.tsinghua.cs.energytool.attrservice.CellAttributeService;
import cn.edu.tsinghua.cs.energytool.attrservice.ConnectivityAttributeService;
import cn.edu.tsinghua.cs.energytool.attrservice.NetworkAttributeService;
import cn.edu.tsinghua.cs.energytool.fileservice.FileService;
import cn.edu.tsinghua.cs.energytool.fileservice.FileStatusReceiver;
import cn.edu.tsinghua.cs.energytool.iperf.IperfService;
import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResultReceiver;
import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;
import cn.edu.tsinghua.cs.energytool.screen.ScreenReceiver;
import cn.edu.tsinghua.cs.energytool.sensors.AccelerometerSensorListener;
import cn.edu.tsinghua.cs.energytool.sensors.LinearAccelerometerSensorListener;

public class EnergyToolService extends Service {

    private static final int NOTIFY_DATACOLLECTOR_ID = 1600;

    private BatteryAttrReceiver batteryAttrReceiver;
    private CellAttrReceiver cellAttrReceiver;
    private NetworkAttrReceiver networkAttrReceiver;
    private FileStatusReceiver fileStatusReceiver;
    private ConnectivityAttrReceiver connectivityAttrReceiver;
    private IperfResultReceiver iperfResultReceiver;

    // screen receive
    private ScreenReceiver screenReceiver;

    private SensorManager sensorManager;
    private SensorEventListener acceSensorListener;
    private SensorEventListener lacSensorListener;

    private EnergyToolSharedPreferences dataCollectorSharedPreferences;

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        powerManager = (PowerManager) this.getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "dataCollectorWakelock");
        wakeLock.acquire();

        dataCollectorSharedPreferences = new EnergyToolSharedPreferences();

        /*
         * start attribute service
         */
        if (dataCollectorSharedPreferences.isBatteryServiceEnable()) {
            startService(new Intent(EnergyToolService.this,
                    BatteryAttributeService.class));
            Log.v("EnergyTool", "Battery Service is started");
        }

        if (dataCollectorSharedPreferences.isCellServiceEnable()) {
            startService(new Intent(EnergyToolService.this,
                    CellAttributeService.class));
            Log.v("EnergyTool", "Cell Service is started");
        }

        if (dataCollectorSharedPreferences.isNetworkServiceEnable()) {
            startService(new Intent(EnergyToolService.this,
                    NetworkAttributeService.class));
            Log.v("EnergyTool", "Network Service is started");
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable()) {
            startService(new Intent(EnergyToolService.this,
                    ConnectivityAttributeService.class));
            Log.v("EnergyTool", "Connectivity Service is started");
        }

        /*
         * start iperf service
         */
        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            startService(new Intent(EnergyToolService.this,
                    IperfService.class));
            Log.v("EnergyTool", "Iperf Service is started");
        }

        /*
         * start file service
         */
        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            startService(new Intent(EnergyToolService.this,
                    FileService.class));
            Log.v("EnergyTool", "File Service is started");
        }

        /*
         * register attribute receiver
         */
        if (dataCollectorSharedPreferences.isBatteryServiceEnable()) {
            batteryAttrReceiver = new BatteryAttrReceiver();
            IntentFilter batteryIntentFilter = new IntentFilter();
            batteryIntentFilter.addAction("BatteryAttributeService");
            registerReceiver(batteryAttrReceiver, batteryIntentFilter);
        }

        if (dataCollectorSharedPreferences.isCellServiceEnable()) {
            cellAttrReceiver = new CellAttrReceiver();
            IntentFilter cellIntentFilter = new IntentFilter();
            cellIntentFilter.addAction("CellAttributeService");
            registerReceiver(cellAttrReceiver, cellIntentFilter);
        }

        if (dataCollectorSharedPreferences.isNetworkServiceEnable()) {
            networkAttrReceiver = new NetworkAttrReceiver();
            IntentFilter networkIntentFilter = new IntentFilter();
            networkIntentFilter.addAction("NetworkAttributeService");
            registerReceiver(networkAttrReceiver, networkIntentFilter);
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable()) {
            connectivityAttrReceiver = new ConnectivityAttrReceiver();
            IntentFilter connectIntentFilter = new IntentFilter();
            connectIntentFilter.addAction("ConnectivityAttributeService");
            registerReceiver(connectivityAttrReceiver, connectIntentFilter);
        }

        /*
         * register iperf status receiver
         */
        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            iperfResultReceiver = new IperfResultReceiver();
            IntentFilter iperfIntentFilter = new IntentFilter();
            iperfIntentFilter.addAction("IperfService");
            registerReceiver(iperfResultReceiver, iperfIntentFilter);
        }

        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            /*
             * register file status receiver
             */
            fileStatusReceiver = new FileStatusReceiver();
            IntentFilter fileStatusIntentFilter = new IntentFilter();
            fileStatusIntentFilter.addAction("FileService");
            registerReceiver(fileStatusReceiver, fileStatusIntentFilter);
        }

        /*
         * Sensors log
         */
        if (dataCollectorSharedPreferences.isSensorServiceEnable()) {
            sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

            Sensor lacSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            lacSensorListener = new LinearAccelerometerSensorListener();
            sensorManager.registerListener(lacSensorListener, lacSensor, SensorManager.SENSOR_DELAY_GAME);

            Sensor acceSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            acceSensorListener = new AccelerometerSensorListener();
            sensorManager.registerListener(acceSensorListener, acceSensor, SensorManager.SENSOR_DELAY_GAME);
        }

        /*
         * register screen receiver
         */
        screenReceiver = new ScreenReceiver();
        IntentFilter screenIntentFilter = new IntentFilter();
        screenIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, screenIntentFilter);

        /*
         * notification
         */
        Intent intent = new Intent(EnergyToolService.this,
                EnergyTool.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                EnergyToolService.this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.dc_notification)
                .setTicker("EnergyTool is running...")
                .setContentTitle("EnergyTool")
                .setContentText("EnergyTool is running")
                .setContentIntent(pendingIntent).build();
        notification.flags |= Notification.FLAG_NO_CLEAR;

        startForeground(NOTIFY_DATACOLLECTOR_ID, notification);

        Log.v("EnergyToolService", "EnergyToolService on create");
    }

    @Override
    public void onDestroy() {
        /*
         * unregister attribute receiver
         */
        if (dataCollectorSharedPreferences.isBatteryServiceEnable()) {
            unregisterReceiver(batteryAttrReceiver);
        }

        if (dataCollectorSharedPreferences.isCellServiceEnable()) {
            unregisterReceiver(cellAttrReceiver);
        }

        if (dataCollectorSharedPreferences.isNetworkServiceEnable()) {
            unregisterReceiver(networkAttrReceiver);
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable()) {
            unregisterReceiver(connectivityAttrReceiver);
        }

        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            unregisterReceiver(fileStatusReceiver);
        }

        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            unregisterReceiver(iperfResultReceiver);
        }

        /*
         * unregister screen receiver
         */
        unregisterReceiver(screenReceiver);

        /*
         * Stop attribute service
         */
        if (dataCollectorSharedPreferences.isBatteryServiceEnable()) {
            stopService(new Intent(EnergyToolService.this,
                    BatteryAttributeService.class));
        }

        if (dataCollectorSharedPreferences.isCellServiceEnable()) {
            stopService(new Intent(EnergyToolService.this,
                    CellAttributeService.class));
        }

        if (dataCollectorSharedPreferences.isNetworkServiceEnable()) {
            stopService(new Intent(EnergyToolService.this,
                    NetworkAttributeService.class));
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable()) {
            stopService(new Intent(EnergyToolService.this,
                    ConnectivityAttributeService.class));
        }

        /*
         * stop iperf service
         */
        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            stopService(new Intent(EnergyToolService.this,
                    IperfService.class));
        }

        /*
         * stop file service
         */
        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            stopService(new Intent(EnergyToolService.this,
                    FileService.class));
        }

        /*
         * stop sensor log
         */
        if (dataCollectorSharedPreferences.isSensorServiceEnable()) {
            sensorManager.unregisterListener(acceSensorListener);
            sensorManager.unregisterListener(lacSensorListener);
        }

        /*
         * Stop Logger
         */
        ETLogger.stopLog();

        /*
         * stop foreground
         */
        stopForeground(true);
        Log.v("EnergyToolService", "EnergyToolService on destroy");
        super.onDestroy();

        wakeLock.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("EnergyToolService", "EnergyToolService on start command");
        return super.onStartCommand(intent, flags, startId);
    }
}
