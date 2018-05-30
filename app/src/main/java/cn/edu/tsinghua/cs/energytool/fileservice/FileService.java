package cn.edu.tsinghua.cs.energytool.fileservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutionException;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

/**
 * FileService
 * Created by zhangdi on 1/2/14.
 */
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
