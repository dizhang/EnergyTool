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

package cn.edu.tsinghua.cs.energytool.iperf;

import android.content.Context;
import android.os.AsyncTask;

import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResult;
import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResultType;
import cn.edu.tsinghua.cs.energytool.iperf.util.IperfUtils;

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
