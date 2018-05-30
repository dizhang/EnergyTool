package cn.edu.tsinghua.cs.energytool.attrservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

/**
 * ConnectivityAttributeService
 * Created by zhangdi on 2/18/14.
 */
public class ConnectivityAttributeService extends Service {
    private boolean isThreadDisable = false;

    private ConnectivityManager connectivityManager;
    private NetworkInfo.DetailedState detailedState;
    private boolean isAvailable;
    private boolean isConnected;
    private boolean isConnectedOrConnecting;

    private long currentTime;
    @Override
    public void onCreate() {
        super.onCreate();

        // connectivity state
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        new Thread(new Runnable() {
            private NetworkInfo networkInfo;
            private Intent intent;
            private EnergyToolSharedPreferences dataCollectorSharedPreferences;
            private int connectivityServiceInterval;

            @Override
            public void run() {
                dataCollectorSharedPreferences = new EnergyToolSharedPreferences();
                connectivityServiceInterval = dataCollectorSharedPreferences.getNetworkServiceInterval();

                while (!isThreadDisable) {
                    try {
                        Thread.sleep(connectivityServiceInterval);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    currentTime = System.currentTimeMillis();

                    // connectivity state
                    networkInfo = connectivityManager.getNetworkInfo(
                            ConnectivityManager.TYPE_MOBILE);

                    detailedState = networkInfo.getDetailedState();
                    isAvailable = networkInfo.isAvailable();
                    isConnected = networkInfo.isConnected();
                    isConnectedOrConnecting = networkInfo.isConnectedOrConnecting();

                    intent = new Intent();
                    intent.putExtra("CurrentTime", currentTime);
                    intent.putExtra("DetailedState", detailedState);
                    intent.putExtra("IsAvailable", isAvailable);
                    intent.putExtra("IsConnected", isConnected);
                    intent.putExtra("IsConnectedOrConnecting", isConnectedOrConnecting);

                    intent.setAction("ConnectivityAttributeService");
                    sendBroadcast(intent);

                }

            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadDisable = true;
        Log.v("EnergyTool", "ConnectivityAttributeService Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
