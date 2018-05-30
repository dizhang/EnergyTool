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

package cn.edu.tsinghua.cs.energytool.preference;

import android.content.Context;
import android.content.SharedPreferences;

import cn.edu.tsinghua.cs.energytool.EnergyToolApplication;
import cn.edu.tsinghua.cs.energytool.R;

public class EnergyToolSharedPreferences {
    private boolean isBatteryServiceEnable;
    private boolean isCellServiceEnable;
    private boolean isNetworkServiceEnable;
    private boolean isFileServiceEnable;
    private boolean isSensorServiceEnable;
    private boolean isConnectivityServiceEnable;
    private boolean isIperfServiceEnable;
    private boolean isSubwayEventEnable;
    private boolean isCellStatesDisplay;
    private boolean isConnectivityStatesDisplay;

    private String fileServiceFileSize;
    private String fileServiceServerIP;
    private int fileServiceServerPort;
    private String iperfServiceServerIP;
    private int iperfServiceServerPort;
    private boolean iperfServerIsReverse;

    private int batteryServiceInterval;
    private int cellServiceInterval;
    private int networkServiceInterval;
    private int connectivityServiceInterval;
    private int fileServiceInterval;
    private int iperfServiceInterval;

    public EnergyToolSharedPreferences() {
        Context context = EnergyToolApplication.getAppContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        isBatteryServiceEnable = sharedPreferences.getBoolean("IsBatteryServiceEnable", true);
        isCellServiceEnable = sharedPreferences.getBoolean("IsCellServiceEnable", true);
        isNetworkServiceEnable = sharedPreferences.getBoolean("IsNetworkServiceEnable", true);
        isFileServiceEnable = sharedPreferences.getBoolean("IsFileServiceEnable", true);
        isSensorServiceEnable = sharedPreferences.getBoolean("IsSensorServiceEnable", false);
        isConnectivityServiceEnable = sharedPreferences.getBoolean("IsConnectivityServiceEnable", false);
        isIperfServiceEnable = sharedPreferences.getBoolean("IsIperfServiceEnable", false);
        isSubwayEventEnable = sharedPreferences.getBoolean("IsSubwayEventEnable", false);
        isCellStatesDisplay = sharedPreferences.getBoolean("IsCellStatesDisplay", false);
        isConnectivityStatesDisplay = sharedPreferences.getBoolean("IsConnectivityStatesDisplay", false);

        fileServiceFileSize = sharedPreferences.getString("FileServiceFileSize",
                context.getString(R.string.default_filesize));
        fileServiceServerIP = sharedPreferences.getString("FileServiceServerIP",
                context.getString(R.string.default_file_service_server_ip));
        fileServiceServerPort = Integer.parseInt(sharedPreferences.getString("FileServiceServerPort",
                context.getString(R.string.default_file_service_server_port)));
        iperfServiceServerIP = sharedPreferences.getString("IperfServiceServerIP",
                context.getString(R.string.default_iperf_service_server_ip));
        iperfServiceServerPort = Integer.parseInt(sharedPreferences.getString("IperfServiceServerPort",
                context.getString(R.string.default_iperf_service_server_port)));
        iperfServerIsReverse = sharedPreferences.getBoolean("IperfServiceIsReverse", true);

        batteryServiceInterval = Integer.parseInt(sharedPreferences.getString("BatteryServiceInterval",
                context.getString(R.string.default_battery_service_interval)));
        cellServiceInterval = Integer.parseInt(sharedPreferences.getString("CellServiceInterval",
                context.getString(R.string.default_cell_service_interval)));
        networkServiceInterval = Integer.parseInt(sharedPreferences.getString("NetworkServiceInterval",
                context.getString(R.string.default_network_service_interval)));
        connectivityServiceInterval = Integer.parseInt(sharedPreferences.getString("ConnectivityServiceInterval",
                context.getString(R.string.default_connectivity_service_interval)));
        fileServiceInterval = Integer.parseInt(sharedPreferences.getString("FileServiceInterval",
                context.getString(R.string.default_file_service_interval)));
        iperfServiceInterval = Integer.parseInt(sharedPreferences.getString("IperfServiceInterval",
                context.getString(R.string.default_iperf_service_interval)));
    }

    public boolean isBatteryServiceEnable() {
        return isBatteryServiceEnable;
    }

    public boolean isCellServiceEnable() {
        return isCellServiceEnable;
    }

    public boolean isNetworkServiceEnable() {
        return isNetworkServiceEnable;
    }

    public boolean isFileServiceEnable() {
        return isFileServiceEnable;
    }

    public boolean isSensorServiceEnable() {
        return isSensorServiceEnable;
    }

    public boolean isConnectivityServiceEnable() {
        return isConnectivityServiceEnable;
    }

    public boolean isIperfServiceEnable() {
        return isIperfServiceEnable;
    }

    public boolean isIperfServerIsReverse() {
        return iperfServerIsReverse;
    }

    public boolean isSubwayEventEnable() {
        return isSubwayEventEnable;
    }

    public boolean isCellStatesDisplay() {
        return isCellStatesDisplay;
    }

    public boolean isConnectivityStatesDisplay() {
        return isConnectivityStatesDisplay;
    }

    public String getFileServiceFileSize() {
        return fileServiceFileSize;
    }

    public String getFileServiceServerIP() {
        return fileServiceServerIP;
    }

    public int getFileServiceServerPort() {
        return fileServiceServerPort;
    }

    public String getIperfServiceServerIP() {
        return iperfServiceServerIP;
    }

    public int getIperfServiceServerPort() {
        return iperfServiceServerPort;
    }

    public int getBatteryServiceInterval() {
        return batteryServiceInterval;
    }

    public int getCellServiceInterval() {
        return cellServiceInterval;
    }

    public int getNetworkServiceInterval() {
        return networkServiceInterval;
    }

    public int getConnectivityServiceInterval() {
        return connectivityServiceInterval;
    }

    public int getFileServiceInterval() {
        return fileServiceInterval;
    }

    public int getIperfServiceInterval() {
        return iperfServiceInterval;
    }
}
