package cn.edu.tsinghua.cs.energytool.util;

import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.Locale;

public class BatteryAttrReaderFactory {

    private static final String BUILD_MODEL = Build.MODEL.toLowerCase(Locale.ENGLISH);
    private static String currentFileName, voltageFileName;
    private static File currentFile, voltageFile;
    private static boolean isCurrentFileExist, isVoltageFileExist;

    private static OneLineReader currentOneLineReader, voltageOneLineReader;

    static {
        Log.v("EnergyTool", BUILD_MODEL);

        if (BUILD_MODEL.contains("htc one")) { // HTC one (M7)
            currentFileName = "/sys/class/power_supply/battery/batt_current_now";
            currentFile = new File(currentFileName);

            voltageFileName = "/sys/class/power_supply/battery/batt_vol_now";
            voltageFile = new File(voltageFileName);

        } else if (BUILD_MODEL.contains("htc m8w")) {
            currentFileName = "/sys/class/power_supply/battery/batt_current_now";
            currentFile = new File(currentFileName);

            voltageFileName = "/sys/class/power_supply/battery/batt_vol_now";
            voltageFile = new File(voltageFileName);

        } else if (BUILD_MODEL.contains("gt-i9300")) {  // Samsung galaxy i9300
            currentFileName = "/sys/class/power_supply/battery/current_now";
            currentFile = new File(currentFileName);

            voltageFileName = "/sys/class/power_supply/battery/voltage_now";
            voltageFile = new File(voltageFileName);

        } else if (BUILD_MODEL.contains("lt26i")) { // Sony LT26i
            currentFileName = "/sys/class/power_supply/bq27520/current_now";
            currentFile = new File(currentFileName);

            voltageFileName = "/sys/class/power_supply/bq27520/voltage_now";
            voltageFile = new File(voltageFileName);
        } else { // default current file location
            currentFileName = "/sys/class/power_supply/battery/current_now";
            currentFile = new File(currentFileName);

            voltageFileName = "/sys/class/power_supply/battery/voltage_now";
            voltageFile = new File(voltageFileName);
        }

        if (currentFile.exists()) {
            isCurrentFileExist = true;
            currentOneLineReader = new OneLineReader(currentFileName);
        } else {
            isCurrentFileExist = false;
        }

        if (voltageFile.exists()) {
            isVoltageFileExist = true;
            voltageOneLineReader = new OneLineReader(voltageFileName);
        } else {
            isVoltageFileExist = false;
        }
    }

    public static Long getLowLevelCurrentValue() {
        if (isCurrentFileExist && currentOneLineReader != null) {
            return currentOneLineReader.getValue();
        }

        return null;
    }


    public static Long getLowLevelVoltageValue() {
        if (isVoltageFileExist && voltageOneLineReader != null) {
            return voltageOneLineReader.getValue();
        }

        return null;
    }

    public static Long getLowLevelCurrentValue2() {

        // htc one
        if (BUILD_MODEL.contains("htc one")) {
            File file = new File("/sys/class/power_supply/battery/batt_attr_text");
            if (file.exists()) {
                Long value = BatteryAttrTextReader.getValue(file, "IBAT(uA): ");

                if (value != null)
                    return value;
            }
        } else if (BUILD_MODEL.contains("gt-i9300")) {   // Samsung galaxy i9300
            File file = new File("/sys/class/power_supply/battery/uevent");
            if (file.exists()) {
                Long value = BatteryAttrTextReader.getValue(file, "POWER_SUPPLY_CURRENT_NOW=");

                if (value != null)
                    return value;
            }
        }

        return null;
    }

    public static Long getLowLevelVoltageValue2() {

        // htc one
        if (BUILD_MODEL.contains("htc one")) {
            File file = new File("/sys/class/power_supply/battery/batt_attr_text");

            if (file.exists()) {
                Long value = BatteryAttrTextReader.getValue(file, "VBAT(uV)");

                if (value != null) {
                    return value;
                }
            }
        } else if (BUILD_MODEL.contains("gt-i9300")) {  // Samsung galaxy i9300
            File file = new File("/sys/class/power_supply/battery/uevent");
            if (file.exists()) {
                Long value = BatteryAttrTextReader.getValue(file, "POWER_SUPPLY_VOLTAGE_NOW=");

                if (value != null)
                    return value;
            }
        }

        return null;
    }

    public static void closeAttrReaderFactory() {
        if (currentOneLineReader != null) {
            currentOneLineReader.closeOneLineReader();
            currentOneLineReader = null;
        }

        if (voltageOneLineReader != null) {
            voltageOneLineReader.closeOneLineReader();
            voltageOneLineReader = null;
        }
    }

}
