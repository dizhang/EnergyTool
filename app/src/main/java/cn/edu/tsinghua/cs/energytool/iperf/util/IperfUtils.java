package cn.edu.tsinghua.cs.energytool.iperf.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.tsinghua.cs.energytool.R;
import cn.edu.tsinghua.cs.energytool.iperf.IperfOptions;
import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResult;
import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResultType;

/**
 * IperfUtils
 * Created by zhangdi on 3/11/14.
 */
public class IperfUtils {

    private static final String Iperf_TAG = "Iperf";

    /**
     * create iperf binary from raw to data directory
     * and change its attribute to be executed
     *
     * @param context context
     */
    public static void createIperf(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.iperf3);
            FileOutputStream fos = context.openFileOutput("iperf3", Context.MODE_PRIVATE);

            int len;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }

            is.close();
            fos.close();

            Runtime.getRuntime().exec(
                    new String[]{
                            "chmod",
                            "755",
                            context.getFilesDir().getAbsolutePath() + File.separator + "iperf3"});

        } catch (Exception e) {
            Log.e(Iperf_TAG, "Exception in creating iperf, error message = " + e.getMessage());
        }
    }

    /**
     * run iperf command and collect the output string for usage
     *
     * @param context context
     * @param iperfOptions iperf options
     * @return iperf output, if ok return the normal result, if contains error return null
     */
    public static IperfResult runIperf(Context context, IperfOptions iperfOptions) {
        IperfResult iperfResult = new IperfResult();
        iperfResult.setResultType(IperfResultType.RIGHT);

        String str = "";

        try {
            //Process process = Runtime.getRuntime().exec(
            // new String[] {
            //"su",
            //"-c",
            //context.getFilesDir().getAbsolutePath() + File.separator + "iperf3 "
            // + iperfOptions.toString()});
            Process process = Runtime.getRuntime().exec(context.getFilesDir().getAbsolutePath() + File.separator + "iperf3 "
                    + iperfOptions.toString());

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                str += line;
            }

            br.close();

            // waits for the command to finish
            process.waitFor();

            // if output contains error, the returned result is error message
            if (str.equals("")) {
                iperfResult.setResultType(IperfResultType.IS_NULL);
            } else if (!str.equals("") && str.contains("error")) {
                iperfResult.setResultType(IperfResultType.GET_ERROR);
            }

        } catch (IOException e) {
            Log.e(Iperf_TAG, "Error running iperf, error msg = " + e.getMessage());
            str = e.getMessage();
            iperfResult.setResultType(IperfResultType.GET_EXCEPTION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        iperfResult.setResult(str);

        return iperfResult;
    }

    /**
     * find the pid of iperf by using and parsing the ps command output
     *
     * @return pid of iperf
     */
    public static String getIperfPID() {
        String pid = null;

        try {
            // run ps to get the process list
            Process process = Runtime.getRuntime().exec("ps");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // find the pid column, for it varies with different versions of ps
            int pidColumn = 0;
            int i = 0;
            String line;

            // split first line by regular expression, which matches one or many whitespaces
            line = br.readLine();
            String[] columnNames = line.split("\\s+");
            for (String col : columnNames) {
                if (col.equalsIgnoreCase("PID")) {
                    pidColumn = i;
                    break;
                }
                i++;
            }

            // parse process list to find iperf entry
            while ((line = br.readLine()) != null) {
                if (line.contains("iperf3") && line.contains("cn.dizhangcs.crowdsignal.bandwidthtester")
                        && !line.contains("sh -c")) {

                    // split the line by white space
                    String strs[] = line.split("\\s+");
                    pid = strs[pidColumn];
                    break;
                }
            }

            br.close();

        } catch (IOException ie) {
            Log.e(Iperf_TAG, "Error getting tcpdump pid, error msg = " + ie.getMessage());
        }

        return pid;
    }

    /**
     * Check to see if iperf is running
     *
     * @return whether iperf is running
     */
    public static boolean isIperfRunning() {
        return getIperfPID() != null;
    }

    public static void stopIperf() {
        try {
            String pid = getIperfPID();

            // iperf is not running
            if (pid == null) {
                return;
            }

            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "kill " + pid});
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            //check errors
            String line;
            while ((line = br.readLine()) != null) {
                Log.e(Iperf_TAG, "kill tcpdump error, error msg = " + line);
            }

            br.close();

        } catch (IOException e) {
            Log.e(Iperf_TAG, "kill iperf error, error msg = " + e.getMessage());
        }
    }
}
