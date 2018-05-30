package cn.edu.tsinghua.cs.energytool.util;

/**
 * CellularNetworkType
 * Created by zhangdi on 1/24/14.
 */
public class CellularNetworkType {

    private static final String[] networkTypeStr = {
            "Unknown",
            "GPRS",
            "EDGE",
            "UMTS",
            "CDMA",
            "EVDO_0",
            "EVDO_A",
            "1xRTT",
            "HSDPA",
            "HSUPA",
            "HSPA",
            "iDen",
            "EVDO_B",
            "LTE",
            "eHRPD",
            "HSPA+"
    };

    public static String getNetworkTypeStr(int networkTypeId) {

        if (networkTypeId >= 0 && networkTypeId < networkTypeStr.length) {
            return networkTypeStr[networkTypeId];
        } else {
            return "ERROR_TYPE";
        }
    }

}
