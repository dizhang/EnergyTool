package cn.edu.tsinghua.cs.energytool.iperf;

import android.content.Context;
import android.os.AsyncTask;

import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResult;
import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResultType;
import cn.edu.tsinghua.cs.energytool.iperf.util.IperfUtils;

/**
 * IperfTask
 * Created by zhangdi on 3/13/14.
 */
public class IperfTask extends AsyncTask<Object, Object, Object> {

    private Context context;
    private IperfOptions iperfOptions;
    private IperfResult iperfResult;
    private IperfService iperfService;

    public IperfTask(Context context, IperfOptions iperfOptions, IperfService iperfService) {
        this.context = context;
        this.iperfOptions = iperfOptions;
        this.iperfService = iperfService;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        IperfResult start = new IperfResult(IperfResultType.START, "start");

        // notify iperf is starting
        iperfService.notifyIperfResult(start);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        // notify iperf running done
        iperfService.notifyIperfResult(iperfResult);

        synchronized (this) {
            this.notify();
        }
    }

    @Override
    protected Object doInBackground(Object... objects) {

        iperfResult = IperfUtils.runIperf(context, iperfOptions);

        return null;
    }
}
