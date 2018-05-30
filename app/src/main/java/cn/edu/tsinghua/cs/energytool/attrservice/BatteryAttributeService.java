/**
 * Copyright (c) 2018 Di Zhang
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
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
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;
import cn.edu.tsinghua.cs.energytool.util.BatteryAttrReaderFactory;

public class BatteryAttributeService extends Service {
    private boolean isThreadDisable = false;

    /*
     * Battery States
     */
    private long current;
    private long voltage;
    private long currentTime;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            private Long curr;
            private Long vol;
            private Intent intent;
            private EnergyToolSharedPreferences dataCollectorSharedPreferences;
            private int batteryServiceInterval;

            @Override
            public void run() {
                dataCollectorSharedPreferences = new EnergyToolSharedPreferences();
                batteryServiceInterval = dataCollectorSharedPreferences.getBatteryServiceInterval();

                while (!isThreadDisable) {
                    try {
                        Thread.sleep(batteryServiceInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    currentTime = System.currentTimeMillis();

                    // reading Current
                    curr = BatteryAttrReaderFactory
                            .getLowLevelCurrentValue();

                    if (curr == null) {
                        current = -1;
                    } else {
                        current = curr;
                    }

                    // reading voltage
                    vol = BatteryAttrReaderFactory
                            .getLowLevelVoltageValue();

                    if (vol == null) {
                        voltage = -1;
                    } else {
                        voltage = vol;
                    }

                    intent = new Intent();
                    intent.putExtra("CurrentTime", currentTime);
                    intent.putExtra("Current", current);
                    intent.putExtra("Voltage", voltage);
                    intent.setAction("BatteryAttributeService");
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadDisable = true;

        Log.v("EnergyTool", "BatteryAttributeService destroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
