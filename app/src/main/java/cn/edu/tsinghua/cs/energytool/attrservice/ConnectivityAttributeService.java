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

package cn.edu.tsinghua.cs.energytool.attrservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

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
