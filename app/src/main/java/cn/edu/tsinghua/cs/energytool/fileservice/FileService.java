/**
 Copyright (c) 2018 Di Zhang

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package cn.edu.tsinghua.cs.energytool.fileservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

public class FileService extends Service {
    private String serviceIp;
    private int servicePort;
    private String fileSizeName;
    private boolean isThreadDisable = false;

    private EnergyToolSharedPreferences dataCollectorSharedPreferences;
    private int fileServiceInterval;

    @Override
    public void onCreate() {
        super.onCreate();

        dataCollectorSharedPreferences = new EnergyToolSharedPreferences();

        serviceIp = dataCollectorSharedPreferences.getFileServiceServerIP();
        servicePort = dataCollectorSharedPreferences.getFileServiceServerPort();
        fileSizeName = dataCollectorSharedPreferences.getFileServiceFileSize();

        fileServiceInterval = dataCollectorSharedPreferences.getFileServiceInterval();

        new Thread(new Runnable() {
            private FileClient fileClient;

            @Override
            public void run() {
                while (!isThreadDisable) {

                    if (fileClient != null && fileClient.getStatus() == AsyncTask.Status.RUNNING) {

                        try {
                            fileClient.get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        /*
                         * Sleep 60 seconds
                         */
                        Thread.sleep(fileServiceInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    fileClient = new FileClient(serviceIp, servicePort, fileSizeName, FileService.this);
                    fileClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

                    Log.d("EnergyTool", "FileClient done!");
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadDisable = true;
        Log.v("EnergyTool", "FileService destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notifyFileStatus(int type, boolean isOk, String msg) {

        Intent intent = new Intent();
        intent.putExtra("CurrentTime", System.currentTimeMillis());
        intent.putExtra("FileSize", fileSizeName);
        intent.putExtra("Type", type);
        intent.putExtra("FileStatus", isOk);

        if (type == 1) {
            intent.putExtra("FileMsg", msg);
        } else if (type == 2) {
            if (!isOk) {
                intent.putExtra("FileMsg", msg);
            } else {
                intent.putExtra("FileMsg", "end");
            }
        }

        intent.setAction("FileService");
        sendBroadcast(intent);
    }
}
