package cn.edu.tsinghua.cs.energytool;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * EnergyToolSetting
 * Created by zhangdi on 5/14/14.
 */
public class EnergyToolSetting extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PrefFragment prefFragment = new PrefFragment();
        fragmentTransaction.replace(android.R.id.content, prefFragment);
        fragmentTransaction.commit();
    }

    public static class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        private CheckBoxPreference batteryServiceCB, cellServiceCB, networkServiceCB;
        private EditTextPreference cellServiceIntervalET, batteryServiceIntervalET, networkServiceIntervalET;
        private CheckBoxPreference fileServiceCB, sensorServiceCB, connectivityServiceCB, iperfServiceCB;
        private EditTextPreference fileServiceIntervalET, connectivityServiceIntervalET, iperfServiceIntervalET;
        private CheckBoxPreference subwayEventCB;
        private CheckBoxPreference cellStatesDisplayCB, connectivityStatesDisplayCB;
        private ListPreference fileServiceFileSizeLP;
        private EditTextPreference fileServiceServerIPET, fileServiceServerPortET;
        private EditTextPreference iperfServiceServerIPET, iperfServiceServerPortET;
        private CheckBoxPreference iperfServiceIsReverseCB;
        private SharedPreferences sharedPreferences;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            PreferenceManager preferenceManager = getPreferenceManager();
            preferenceManager.setSharedPreferencesName("preferences");
            preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);
            sharedPreferences = preferenceManager.getSharedPreferences();

            batteryServiceCB = (CheckBoxPreference) findPreference("IsBatteryServiceEnable");
            batteryServiceCB.setOnPreferenceChangeListener(this);
            batteryServiceIntervalET = (EditTextPreference) findPreference("BatteryServiceInterval");
            batteryServiceIntervalET.setOnPreferenceChangeListener(this);

            cellServiceCB = (CheckBoxPreference) findPreference("IsCellServiceEnable");
            cellServiceCB.setOnPreferenceChangeListener(this);
            cellServiceIntervalET = (EditTextPreference) findPreference("CellServiceInterval");
            cellServiceIntervalET.setOnPreferenceChangeListener(this);
            cellStatesDisplayCB = (CheckBoxPreference) findPreference("IsCellStatesDisplay");
            cellStatesDisplayCB.setOnPreferenceChangeListener(this);

            networkServiceCB = (CheckBoxPreference) findPreference("IsNetworkServiceEnable");
            networkServiceCB.setOnPreferenceChangeListener(this);
            networkServiceIntervalET = (EditTextPreference) findPreference("NetworkServiceInterval");
            networkServiceIntervalET.setOnPreferenceChangeListener(this);

            connectivityServiceCB = (CheckBoxPreference) findPreference("IsConnectivityServiceEnable");
            connectivityServiceCB.setOnPreferenceChangeListener(this);
            connectivityServiceIntervalET = (EditTextPreference) findPreference("ConnectivityServiceInterval");
            connectivityServiceIntervalET.setOnPreferenceChangeListener(this);
            connectivityStatesDisplayCB = (CheckBoxPreference) findPreference("IsConnectivityStatesDisplay");
            connectivityStatesDisplayCB.setOnPreferenceChangeListener(this);

            fileServiceCB = (CheckBoxPreference) findPreference("IsFileServiceEnable");
            fileServiceCB.setOnPreferenceChangeListener(this);
            fileServiceIntervalET = (EditTextPreference) findPreference("FileServiceInterval");
            fileServiceIntervalET.setOnPreferenceChangeListener(this);
            fileServiceFileSizeLP = (ListPreference) findPreference("FileServiceFileSize");
            fileServiceFileSizeLP.setOnPreferenceChangeListener(this);
            fileServiceServerIPET = (EditTextPreference) findPreference("FileServiceServerIP");
            fileServiceServerIPET.setOnPreferenceChangeListener(this);
            fileServiceServerPortET = (EditTextPreference) findPreference("FileServiceServerPort");
            fileServiceServerPortET.setOnPreferenceChangeListener(this);

            iperfServiceCB = (CheckBoxPreference) findPreference("IsIperfServiceEnable");
            iperfServiceCB.setOnPreferenceChangeListener(this);
            iperfServiceIntervalET = (EditTextPreference) findPreference("IperfServiceInterval");
            iperfServiceIntervalET.setOnPreferenceChangeListener(this);
            iperfServiceServerIPET = (EditTextPreference) findPreference("IperfServiceServerIP");
            iperfServiceServerIPET.setOnPreferenceChangeListener(this);
            iperfServiceServerPortET = (EditTextPreference) findPreference("IperfServiceServerPort");
            iperfServiceServerPortET.setOnPreferenceChangeListener(this);
            iperfServiceIsReverseCB = (CheckBoxPreference) findPreference("IperfServiceIsReverse");
            iperfServiceIsReverseCB.setOnPreferenceChangeListener(this);

            sensorServiceCB = (CheckBoxPreference) findPreference("IsSensorServiceEnable");
            sensorServiceCB.setOnPreferenceChangeListener(this);

            subwayEventCB = (CheckBoxPreference) findPreference("IsSubwayEventEnable");
            subwayEventCB.setOnPreferenceChangeListener(this);

            // set preference
            boolean isBatteryServiceEnabled = sharedPreferences.getBoolean("IsBatteryServiceEnable", true);
            batteryServiceCB.setChecked(isBatteryServiceEnabled);
            String batteryServiceIntervalValue = sharedPreferences.getString("BatteryServiceInterval",
                    getString(R.string.default_battery_service_interval));
            batteryServiceIntervalET.setText(batteryServiceIntervalValue);
            batteryServiceIntervalET.setSummary(
                    getString(R.string.battery_service_interval_summary) +
                            " " + batteryServiceIntervalValue + " ms");
            batteryServiceIntervalET.setEnabled(isBatteryServiceEnabled);

            boolean isCellServiceEnabled = sharedPreferences.getBoolean("IsCellServiceEnable", true);
            cellServiceCB.setChecked(isCellServiceEnabled);
            cellStatesDisplayCB.setChecked(sharedPreferences.getBoolean("IsCellStatesDisplay", false));
            cellStatesDisplayCB.setEnabled(isCellServiceEnabled);
            String cellServiceIntervalValue = sharedPreferences.getString("CellServiceInterval",
                    getString(R.string.default_cell_service_interval));
            cellServiceIntervalET.setText(cellServiceIntervalValue);
            cellServiceIntervalET.setSummary(
                    getString(R.string.cell_service_interval_summary) +
                            " " + cellServiceIntervalValue + " ms");
            cellServiceIntervalET.setEnabled(isCellServiceEnabled);

            boolean isNetworkServiceEnabled = sharedPreferences.getBoolean("IsNetworkServiceEnable", true);
            networkServiceCB.setChecked(isNetworkServiceEnabled);
            String networkServiceIntervalValue = sharedPreferences.getString("NetworkServiceInterval",
                    getString(R.string.default_network_service_interval));
            networkServiceIntervalET.setText(networkServiceIntervalValue);
            networkServiceIntervalET.setSummary(
                    getString(R.string.network_service_interval_summary) +
                            " " + networkServiceIntervalValue + " ms");
            networkServiceIntervalET.setEnabled(isNetworkServiceEnabled);

            boolean isFileServiceEnabled = sharedPreferences.getBoolean("IsFileServiceEnable", true);
            fileServiceCB.setChecked(isFileServiceEnabled);
            String fileServiceIntervalValue = sharedPreferences.getString("FileServiceInterval",
                    getString(R.string.default_file_service_interval));
            fileServiceIntervalET.setText(fileServiceIntervalValue);
            fileServiceIntervalET.setSummary(
                    getString(R.string.file_service_interval_summary) +
                            " " + fileServiceIntervalValue + " ms");
            fileServiceIntervalET.setEnabled(isFileServiceEnabled);
            String fileServiceFileSizeValue = sharedPreferences.getString("FileServiceFileSize",
                    getString(R.string.default_filesize));
            fileServiceFileSizeLP.setValue(fileServiceFileSizeValue);
            fileServiceFileSizeLP.setSummary(
                    getString(R.string.file_service_file_size_summary) +
                            " " + fileServiceFileSizeLP.getEntry());
            fileServiceFileSizeLP.setEnabled(isFileServiceEnabled);
            String fileServiceServerIPValue = sharedPreferences.getString("FileServiceServerIP",
                    getString(R.string.default_file_service_server_ip));
            fileServiceServerIPET.setText(fileServiceServerIPValue);
            fileServiceServerIPET.setSummary(
                    getString(R.string.file_service_server_ip_summary) +
                            " " + fileServiceServerIPValue);
            fileServiceServerIPET.setEnabled(isFileServiceEnabled);
            String fileServiceServerPortValue = sharedPreferences.getString("FileServiceServerPort",
                    getString(R.string.default_file_service_server_port));
            fileServiceServerPortET.setText(fileServiceServerPortValue);
            fileServiceServerPortET.setSummary(
                    getString(R.string.file_service_server_port_summary) +
                            " " + fileServiceServerPortValue);
            fileServiceServerPortET.setEnabled(isFileServiceEnabled);

            boolean isIperfServiceEnabled = sharedPreferences.getBoolean("IsIperfServiceEnable", false);
            iperfServiceCB.setChecked(isIperfServiceEnabled);
            String iperfServiceIntervalValue = sharedPreferences.getString("IperfServiceInterval",
                    getString(R.string.default_iperf_service_interval));
            iperfServiceIntervalET.setText(iperfServiceIntervalValue);
            iperfServiceIntervalET.setSummary(
                    getString(R.string.iperf_service_interval_summary) +
                            " " + iperfServiceIntervalValue + " ms");
            iperfServiceIntervalET.setEnabled(isIperfServiceEnabled);
            String iperfServiceServerIPValue = sharedPreferences.getString("IperfServiceServerIP",
                    getString(R.string.default_iperf_service_server_ip));
            iperfServiceServerIPET.setText(iperfServiceServerIPValue);
            iperfServiceServerIPET.setSummary(getString(R.string.iperf_service_server_ip_summary) +
                    " " + iperfServiceServerIPValue);
            iperfServiceServerIPET.setEnabled(isIperfServiceEnabled);
            String iperfServiceServerPortValue = sharedPreferences.getString("IperfServiceServerPort",
                    getString(R.string.default_iperf_service_server_port));
            iperfServiceServerPortET.setText(iperfServiceServerPortValue);
            iperfServiceServerPortET.setSummary(getString(R.string.iperf_service_server_port_summary) +
                    " " + iperfServiceServerPortValue);
            iperfServiceServerPortET.setEnabled(isIperfServiceEnabled);
            iperfServiceIsReverseCB.setChecked(sharedPreferences.getBoolean("IperfServiceIsReverse", true));
            iperfServiceIsReverseCB.setEnabled(isIperfServiceEnabled);

            sensorServiceCB.setChecked(sharedPreferences.getBoolean("IsSensorServiceEnable", false));

            boolean isConnectivityServiceEnabled = sharedPreferences.getBoolean("IsConnectivityServiceEnable", false);
            connectivityServiceCB.setChecked(isConnectivityServiceEnabled);
            String connectivityServiceIntervalValue = sharedPreferences.getString("ConnectivityServiceInterval",
                    getString(R.string.default_connectivity_service_interval));
            connectivityServiceIntervalET.setText(connectivityServiceIntervalValue);
            connectivityServiceIntervalET.setSummary(
                    getString(R.string.connectivity_service_interval_summary) +
                            " " + connectivityServiceIntervalValue + " ms");
            connectivityServiceIntervalET.setEnabled(isConnectivityServiceEnabled);
            connectivityStatesDisplayCB.setChecked(sharedPreferences.getBoolean("IsConnectivityStatesDisplay", false));
            connectivityStatesDisplayCB.setEnabled(isConnectivityServiceEnabled);

            subwayEventCB.setChecked(sharedPreferences.getBoolean("IsSubwayEventEnable", false));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object object) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (preference == batteryServiceCB) {
                boolean isBatteryServiceEnabled = (Boolean) object;
                editor.putBoolean("IsBatteryServiceEnable", isBatteryServiceEnabled);
                batteryServiceIntervalET.setEnabled(isBatteryServiceEnabled);
            } else if (preference == cellServiceCB) {
                boolean isCellServiceEnabled = (Boolean) object;
                editor.putBoolean("IsCellServiceEnable", isCellServiceEnabled);
                cellStatesDisplayCB.setEnabled(isCellServiceEnabled);
                cellServiceIntervalET.setEnabled(isCellServiceEnabled);
            } else if (preference == networkServiceCB) {
                boolean isNetworkServiceEnabled = (Boolean) object;
                editor.putBoolean("IsNetworkServiceEnable", isNetworkServiceEnabled);
                networkServiceIntervalET.setEnabled(isNetworkServiceEnabled);
            } else if (preference == fileServiceCB) {
                boolean isFileServiceEnabled = (Boolean) object;
                editor.putBoolean("IsFileServiceEnable", isFileServiceEnabled);
                fileServiceIntervalET.setEnabled(isFileServiceEnabled);
                fileServiceFileSizeLP.setEnabled(isFileServiceEnabled);
                fileServiceServerIPET.setEnabled(isFileServiceEnabled);
                fileServiceServerPortET.setEnabled(isFileServiceEnabled);
            } else if (preference == iperfServiceCB) {
                boolean isIperfServiceEnabled = (Boolean) object;
                editor.putBoolean("IsIperfServiceEnable", isIperfServiceEnabled);
                iperfServiceIntervalET.setEnabled(isIperfServiceEnabled);
                iperfServiceServerIPET.setEnabled(isIperfServiceEnabled);
                iperfServiceServerPortET.setEnabled(isIperfServiceEnabled);
                iperfServiceIsReverseCB.setEnabled(isIperfServiceEnabled);
            } else if (preference == sensorServiceCB) {
                editor.putBoolean("IsSensorServiceEnable", (Boolean) object);
            } else if (preference == connectivityServiceCB) {
                boolean isConnectivityServiceEnabled = (Boolean) object;
                editor.putBoolean("IsConnectivityServiceEnable", isConnectivityServiceEnabled);
                connectivityServiceIntervalET.setEnabled(isConnectivityServiceEnabled);
                connectivityStatesDisplayCB.setEnabled(isConnectivityServiceEnabled);
            } else if (preference == subwayEventCB) {
                editor.putBoolean("IsSubwayEventEnable", (Boolean) object);
            } else if (preference == cellStatesDisplayCB) {
                editor.putBoolean("IsCellStatesDisplay", (Boolean) object);
            } else if (preference == connectivityStatesDisplayCB) {
                editor.putBoolean("IsConnectivityStatesDisplay", (Boolean) object);
            } else if (preference == fileServiceFileSizeLP) {
                editor.putString("FileServiceFileSize", object.toString());
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setValue(object.toString());
                preference.setSummary(
                        getString(R.string.file_service_file_size_summary)
                                + " " + listPreference.getEntry());
            } else if (preference == fileServiceServerIPET) {
                editor.putString("FileServiceServerIP", object.toString());
                preference.setSummary(
                        getString(R.string.file_service_server_ip_summary)
                                + " " + object.toString());
            } else if (preference == fileServiceServerPortET) {
                editor.putString("FileServiceServerPort", object.toString());
                preference.setSummary(
                        getString(R.string.file_service_server_port_summary)
                                + " " + object.toString());
            } else if (preference == iperfServiceServerIPET) {
                editor.putString("IperfServiceServerIP", object.toString());
                preference.setSummary(
                        getString(R.string.iperf_service_server_ip_summary)
                                + " " + object.toString());
            } else if (preference == iperfServiceServerPortET) {
                editor.putString("IperfServiceServerPort", object.toString());
                preference.setSummary(
                        getString(R.string.iperf_service_server_port_summary)
                                + " " + object.toString());
            } else if (preference == cellServiceIntervalET) {
                editor.putString("CellServiceInterval", object.toString());
                preference.setSummary(
                        getString(R.string.cell_service_interval_summary) +
                                " " + object.toString() + " ms");
            } else if (preference == batteryServiceIntervalET) {
                editor.putString("BatteryServiceInterval", object.toString());
                preference.setSummary(
                        getString(R.string.battery_service_interval_summary) +
                                " " + object.toString() + " ms");
            } else if (preference == networkServiceIntervalET) {
                editor.putString("NetworkServiceInterval", object.toString());
                preference.setSummary(
                        getString(R.string.network_service_interval_summary) +
                                " " + object.toString() + " ms");
            } else if (preference == connectivityServiceIntervalET) {
                editor.putString("ConnectivityServiceInterval", object.toString());
                preference.setSummary(
                        getString(R.string.connectivity_service_interval_summary) +
                                " " + object.toString() + " ms");
            } else if (preference == fileServiceIntervalET) {
                editor.putString("FileServiceInterval", object.toString());
                preference.setSummary(
                        getString(R.string.file_service_interval_summary) +
                                " " + object.toString() + " ms");
            } else if (preference == iperfServiceIntervalET) {
                editor.putString("IperfServiceInterval", object.toString());
                preference.setSummary(
                        getString(R.string.iperf_service_interval_summary) +
                                " " + object.toString() + " ms");
            } else if(preference == iperfServiceIsReverseCB) {
                editor.putBoolean("IperfServiceIsReverse", (Boolean) object);
            }

            return editor.commit();
        }
    }
}

