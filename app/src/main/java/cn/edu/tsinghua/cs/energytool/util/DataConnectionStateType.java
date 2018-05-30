package cn.edu.tsinghua.cs.energytool.util;

/**
 * DataConnectionStateType
 * Created by zhangdi on 1/24/14.
 */
public class DataConnectionStateType {

    private static final String[] dataConnectionStateTypeStr = {
            "DISCONNECTED",
            "CONNECTING",
            "CONNECTED",
            "SUSPENDED"
    };

    public static String getDataConnectionStateTypeStr(int dataConnectionStateTypeId) {

        if (dataConnectionStateTypeId >= 0 &&
                dataConnectionStateTypeId < dataConnectionStateTypeStr.length) {
            return dataConnectionStateTypeStr[dataConnectionStateTypeId];
        } else {
            return "ERROR_TYPE";
        }
    }
}
