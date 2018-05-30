package cn.edu.tsinghua.cs.energytool.attrservice;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.IBinder;
import android.util.Log;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

public class NetworkAttributeService extends Service {
    private boolean isThreadDisable = false;

    /*
     * Network States
     */
    private long beforeMobileRxBytes;
    private long beforeMobileTxBytes;
    private long mobileRxBytes;
    private long mobileTxBytes;
    /*
     * Receive Speed and Transmit Speed in bytes/s
     */
    private long rxSpeed;
    private long txSpeed;

    private long currentTime;

    @Override
    public void onCreate() {
        super.onCreate();

        mobileRxBytes = TrafficStats.getMobileRxBytes();
        mobileTxBytes = TrafficStats.getMobileTxBytes();
        beforeMobileRxBytes = mobileRxBytes;
        beforeMobileTxBytes = mobileTxBytes;

        new Thread(new Runnable() {
            private Intent intent;
            private EnergyToolSharedPreferences dataCollectorSharedPreferences;
            private int networkServiceInterval;

            @Override
            public void run() {
                dataCollectorSharedPreferences = new EnergyToolSharedPreferences();
                networkServiceInterval = dataCollectorSharedPreferences.getNetworkServiceInterval();

                while (!isThreadDisable) {
                    try {
                        Thread.sleep(networkServiceInterval);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    currentTime = System.currentTimeMillis();

                    // network speed of downLink and upLink
                    mobileRxBytes = TrafficStats.getMobileRxBytes();
                    mobileTxBytes = TrafficStats.getMobileTxBytes();
                    /*
                     * 10 = 1/0.1, and 0.1 = 100/1000
					 */
                    rxSpeed = (mobileRxBytes - beforeMobileRxBytes) * 10;
                    txSpeed = (mobileTxBytes - beforeMobileTxBytes) * 10;

					/*
					 * reset current bytes as before bytes
					 */
                    beforeMobileRxBytes = mobileRxBytes;
                    beforeMobileTxBytes = mobileTxBytes;

                    intent = new Intent();
                    intent.putExtra("CurrentTime", currentTime);
                    intent.putExtra("RxSpeed", rxSpeed);
                    intent.putExtra("TxSpeed", txSpeed);
                    intent.setAction("NetworkAttributeService");
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadDisable = true;
        Log.v("EnergyTool", "NetworkAttributeService Destroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
