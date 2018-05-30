package cn.edu.tsinghua.cs.energytool.util;

/**
 * DataActivityType
 * Created by zhangdi on 1/24/14.
 */
public class DataActivityType {

    private static final String[] dataActivityTypeStr = {
            "NONE",
            "IN",
            "OUT",
            "INOUT",
            "DORMANT"
    };

    public static String getDataActivityTypeStr(int dataActivityTypeId) {

        if (dataActivityTypeId >= 0 && dataActivityTypeId < dataActivityTypeStr.length) {
            return dataActivityTypeStr[dataActivityTypeId];
        } else {
            return "ERROR_TYPE";
        }
    }
}
