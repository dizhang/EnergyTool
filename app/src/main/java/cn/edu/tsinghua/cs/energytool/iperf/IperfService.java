package cn.edu.tsinghua.cs.energytool.iperf;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResult;
import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResultType;
import cn.edu.tsinghua.cs.energytool.iperf.util.IperfJsonUtil;
import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

/**
 * IperfService
 * Created by zhangdi on 3/13/14.
 */
public class IperfService extends Service {
    private boolean isThreadDisable = false;
    private IperfOptions iperfOptions;

    private EnergyToolSharedPreferences dataCollectorSharedPreferences;
    private int iperfServiceInterval;

    @Override
    public void onCreate() {
        super.onCreate();

        dataCollectorSharedPreferences = new EnergyToolSharedPreferences();

        iperfOptions = new IperfOptions();
        iperfOptions.setServerIP(dataCollectorSharedPreferences.getIperfServiceServerIP());
        iperfOptions.setServerPort(dataCollectorSharedPreferences.getIperfServiceServerPort());
        iperfOptions.setReverse(dataCollectorSharedPreferences.isIperfServerIsReverse());
        iperfOptions.setLengthOfBuffer(128);
        iperfOptions.setOutputInJsonFormat(true);
        iperfOptions.setTime(4);

        iperfServiceInterval = dataCollectorSharedPreferences.getIperfServiceInterval();

        new Thread(new Runnable() {
            private IperfTask iperfTask;

            @Override
            public void run() {
                while (!isThreadDisable) {

                    if (iperfTask != null && iperfTask.getStatus() == AsyncTask.Status.RUNNING) {
                        try {
                            iperfTask.get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    /*
                     * sleep 10 seconds before another test
                     */
                    try {
                        Thread.sleep(iperfServiceInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    iperfTask = new IperfTask(IperfService.this, iperfOptions, IperfService.this);
                    iperfTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadDisable = true;
        Log.v("BandwidthTester", "IperfService destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notifyIperfResult(IperfResult iperfResult) {

        List<String> listResult;
        String aggregatedResult;
        String detailResult;

        if (iperfResult.getResultType() == IperfResultType.RIGHT) {
            listResult = IperfJsonUtil.parserIperfResult(iperfResult.getResult());
            aggregatedResult = listResult.get(0);
            detailResult = listResult.get(1);
        } else if (iperfResult.getResultType() == IperfResultType.GET_ERROR) {
            aggregatedResult = IperfJsonUtil.parserIperfError(iperfResult.getResult());
            detailResult = "Error";
        } else {
            aggregatedResult = iperfResult.getResult();
            detailResult = "Unknown";
        }

        Intent intent = new Intent();
        intent.putExtra("CurrentTime", System.currentTimeMillis());
        intent.putExtra("Type", iperfResult.getResultType());
        intent.putExtra("Result", aggregatedResult);
        intent.putExtra("DetailResult", detailResult);
        intent.setAction("IperfService");
        sendBroadcast(intent);
    }
}
