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

package cn.edu.tsinghua.cs.energytool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.tsinghua.cs.energytool.iperf.result.IperfResultType;
import cn.edu.tsinghua.cs.energytool.iperf.util.IperfUtils;
import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.SaveLogAsUtil;
import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;
import cn.edu.tsinghua.cs.energytool.subwayevent.SubwayEventButtonListener;
import cn.edu.tsinghua.cs.energytool.util.CellularNetworkType;
import cn.edu.tsinghua.cs.energytool.util.DataActivityType;
import cn.edu.tsinghua.cs.energytool.util.DataConnectionStateType;
import cn.edu.tsinghua.cs.energytool.util.SubwayEventType;

public class EnergyTool extends Activity {

    private static final StringBuilder stringBuilder = new StringBuilder(128);
    private TextView networkTypeTv;
    private TextView isGsmTv, gsmSignalStrengthTv, gsmBitErrorRateTv;
    private TextView cdmaDbmTv, cdmaEcioTv, evdoDbmTv, evdoEcioTv, evdoSnrTv;
    private TextView lteSignalStrengthTv, lteRsrpTv, lteRsrqTv, lteRssnrTv, lteCqiTv;
    private TextView cellIdTv, rncIdTv, pscTv, cellLocationTv;
    private TextView dataDirectionTv, dataStateTv;
    private TextView connDetailedStateTv, connAvailableTv;
    private TextView connConnectedTv, connConnectedOrConnectingTv;
    private TextView fileStatusTv, fileMsgTv, fileMsgOkTv, fileMsgErrorTv;
    private TextView iperfStatusTv, iperfOkTv, iperfErrorTv, iperfMsgTv;
    private TextView helpMessageTv;
    private Button arSwBut, enSwBut, leSwBut, exSwBut, swStartBut, swStopBut;
    private LinearLayout fileLinearLayout, iperfLinearLayout;
    private LinearLayout subwayButtonLinearLayout1, subwayButtonLinearLayout2;
    private LinearLayout cellStatusLinearLayout, connStatusLinearLayout;
    private CellStatusReceiver cellStatusReceiver;
    private ConnectivityStatusActivityReceiver connectivityStatusActivityReceiver;
    private FileStatusActivityReceiver fileStatusActivityReceiver;
    private IperfStatusReceiver iperfStatusReceiver;

    private EnergyToolSharedPreferences dataCollectorSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check to see if iperf binary is present
        try {
            FileInputStream fis = openFileInput("iperf3");
            fis.close();
        } catch (FileNotFoundException e) {
            // If iperf3 file not found, need to create
            IperfUtils.createIperf(this);
        } catch (IOException ie) {
            Log.e("EnergyTool", "Error in creating iperf3, msg = " + ie.getMessage());
        }

        setContentView(R.layout.energytool);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*
         * The log should be started in the activity and stopped in the service
         */
        ETLogger.startLog();

        // textView
        networkTypeTv = (TextView) findViewById(R.id.network_type);

        isGsmTv = (TextView) findViewById(R.id.is_gsm);
        gsmSignalStrengthTv = (TextView) findViewById(R.id.gsm_signal_strength);
        gsmBitErrorRateTv = (TextView) findViewById(R.id.gsm_bit_error_rate);
        cdmaDbmTv = (TextView) findViewById(R.id.cdma_dbm);
        cdmaEcioTv = (TextView) findViewById(R.id.cdma_ecio);
        evdoDbmTv = (TextView) findViewById(R.id.evdo_dbm);
        evdoEcioTv = (TextView) findViewById(R.id.evdo_ecio);
        evdoSnrTv = (TextView) findViewById(R.id.evdo_snr);
        lteSignalStrengthTv = (TextView) findViewById(R.id.lte_signal_strength);
        lteRsrpTv = (TextView) findViewById(R.id.lte_rsrp);
        lteRsrqTv = (TextView) findViewById(R.id.lte_rsrq);
        lteRssnrTv = (TextView) findViewById(R.id.lte_rssnr);
        lteCqiTv = (TextView) findViewById(R.id.lte_cqi);

        cellIdTv = (TextView) findViewById(R.id.cell_id);
        rncIdTv = (TextView) findViewById(R.id.rnc_id);
        pscTv = (TextView) findViewById(R.id.psc);

        cellLocationTv = (TextView) findViewById(R.id.cell_location);

        dataDirectionTv = (TextView) findViewById(R.id.data_direction);
        dataStateTv = (TextView) findViewById(R.id.data_state);

        connDetailedStateTv = (TextView) findViewById(R.id.conn_detailed_state);
        connAvailableTv = (TextView) findViewById(R.id.conn_available);
        connConnectedTv = (TextView) findViewById(R.id.conn_connected);
        connConnectedOrConnectingTv = (TextView) findViewById(R.id.conn_connected_or_connecting);

        fileStatusTv = (TextView) findViewById(R.id.file_status);
        fileMsgTv = (TextView) findViewById(R.id.file_msg);
        fileMsgOkTv = (TextView) findViewById(R.id.file_msg_ok_total);
        fileMsgErrorTv = (TextView) findViewById(R.id.file_msg_error_total);

        iperfStatusTv = (TextView) findViewById(R.id.iperf_status);
        iperfOkTv = (TextView) findViewById(R.id.iperf_ok_total);
        iperfErrorTv = (TextView) findViewById(R.id.iperf_error_total);
        iperfMsgTv = (TextView) findViewById(R.id.iperf_msg);

        helpMessageTv = (TextView) findViewById(R.id.help_message);

        arSwBut = (Button) findViewById(R.id.ar_sw_but);
        enSwBut = (Button) findViewById(R.id.en_sw_but);
        leSwBut = (Button) findViewById(R.id.le_sw_but);
        exSwBut = (Button) findViewById(R.id.ex_sw_but);
        swStartBut = (Button) findViewById(R.id.sw_start_but);
        swStopBut = (Button) findViewById(R.id.sw_stop_but);

        fileLinearLayout = (LinearLayout) findViewById(R.id.file_linearLayout);
        iperfLinearLayout = (LinearLayout) findViewById(R.id.iperf_linearLayout);
        subwayButtonLinearLayout1 = (LinearLayout) findViewById(R.id.subway_button_linearLayout1);
        subwayButtonLinearLayout2 = (LinearLayout) findViewById(R.id.subway_button_linearLayout2);
        cellStatusLinearLayout = (LinearLayout) findViewById(R.id.cell_status_linearLayout);
        connStatusLinearLayout = (LinearLayout) findViewById(R.id.conn_status_linearLayout);

        startService(new Intent(EnergyTool.this, EnergyToolService.class));

        dataCollectorSharedPreferences = new EnergyToolSharedPreferences();

        if ((dataCollectorSharedPreferences.isCellServiceEnable() && dataCollectorSharedPreferences.isCellStatesDisplay())
                || dataCollectorSharedPreferences.isFileServiceEnable()
                || (dataCollectorSharedPreferences.isConnectivityServiceEnable() && dataCollectorSharedPreferences.isConnectivityStatesDisplay())
                || dataCollectorSharedPreferences.isIperfServiceEnable()
                || dataCollectorSharedPreferences.isSubwayEventEnable()) {
            helpMessageTv.setVisibility(View.GONE);
        }

        // button
        if (dataCollectorSharedPreferences.isSubwayEventEnable()) {
            arSwBut.setOnClickListener(new SubwayEventButtonListener(
                    EnergyTool.this, SubwayEventType.ARRSBW));
            enSwBut.setOnClickListener(new SubwayEventButtonListener(
                    EnergyTool.this, SubwayEventType.ENTSBW));
            leSwBut.setOnClickListener(new SubwayEventButtonListener(
                    EnergyTool.this, SubwayEventType.LEVSBW));
            exSwBut.setOnClickListener(new SubwayEventButtonListener(
                    EnergyTool.this, SubwayEventType.EXTSBW));
            swStartBut.setOnClickListener(new SubwayEventButtonListener(
                    EnergyTool.this, SubwayEventType.SBWSRT));
            swStopBut.setOnClickListener(new SubwayEventButtonListener(
                    EnergyTool.this, SubwayEventType.SBWSTP));
        } else {
            subwayButtonLinearLayout1.setVisibility(View.GONE);
            subwayButtonLinearLayout2.setVisibility(View.GONE);
        }

        if (dataCollectorSharedPreferences.isCellServiceEnable() &&
                dataCollectorSharedPreferences.isCellStatesDisplay()) {
            cellStatusReceiver = new CellStatusReceiver();
        } else {
            cellStatusLinearLayout.setVisibility(View.GONE);
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable() &&
                dataCollectorSharedPreferences.isConnectivityStatesDisplay()) {
            connectivityStatusActivityReceiver = new ConnectivityStatusActivityReceiver();
        } else {
            connStatusLinearLayout.setVisibility(View.GONE);
        }

        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            fileStatusActivityReceiver = new FileStatusActivityReceiver();
        } else {
            fileLinearLayout.setVisibility(View.GONE);
        }

        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            iperfStatusReceiver = new IperfStatusReceiver();
        } else {
            iperfLinearLayout.setVisibility(View.GONE);
        }

        Log.v("EnergyTool", "EnergyTool on create");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_log_as:
                final EditText et = new EditText(EnergyTool.this);
                et.setText("EnergyTool_" + (new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US)).format(new Date()));
                new AlertDialog.Builder(this).setTitle("Please input file name:")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setView(et).setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Editable saveAsFolderNameEdit = et.getText();
                        String saveAsFolderName = "";

                        if (saveAsFolderNameEdit != null) {
                            saveAsFolderName = saveAsFolderNameEdit.toString();
                        }

                        if (!saveAsFolderName.equals("")) {
                            int saveLogAsStatus = SaveLogAsUtil.saveLogAs(saveAsFolderName);
                            String str = "Save Log File into folder: [" + saveAsFolderName + "]";

                            switch (saveLogAsStatus) {
                                case 0:
                                    str += "ERROR!";
                                    break;
                                case 1:
                                    str += "Successfully!";
                                    break;
                                case 2:
                                    str += "Failed due to dir name already existing!";
                                    break;
                                case 3:
                                    str += "No log need to save!";
                                    break;
                                case 4:
                                    str += "Make save as dir error!";
                                    break;
                                default:
                                    str += "Unknown status!";
                            }

                            Toast.makeText(EnergyTool.this, str, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EnergyTool.this, "Folder Name cannot be null!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("Cancel", null).show();
                break;
            case R.id.settings:
                Intent intent = new Intent();
                intent.setClass(EnergyTool.this, EnergyToolSetting.class);
                startActivity(intent);
                break;
            case R.id.exit:
                EnergyTool.this.finish();
                /*
                 * stop data collector service
                 */
                stopService(new Intent(EnergyTool.this,
                        EnergyToolService.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("EnergyTool", "EnergyTool on destroy");
    }

    @Override
    protected void onResume() {

        if (dataCollectorSharedPreferences.isCellServiceEnable() &&
                dataCollectorSharedPreferences.isCellStatesDisplay()) {
            IntentFilter networkIntentFilter = new IntentFilter();
            networkIntentFilter.addAction("CellAttributeService");
            registerReceiver(cellStatusReceiver, networkIntentFilter);
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable() &&
                dataCollectorSharedPreferences.isConnectivityStatesDisplay()) {
            IntentFilter connectIntentFilter = new IntentFilter();
            connectIntentFilter.addAction("ConnectivityAttributeService");
            registerReceiver(connectivityStatusActivityReceiver, connectIntentFilter);
        }

        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            IntentFilter fileStatusActivityIntentFilter = new IntentFilter();
            fileStatusActivityIntentFilter.addAction("FileService");
            registerReceiver(fileStatusActivityReceiver, fileStatusActivityIntentFilter);
        }

        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            IntentFilter iperfIntentFilter = new IntentFilter();
            iperfIntentFilter.addAction("IperfService");
            registerReceiver(iperfStatusReceiver, iperfIntentFilter);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {

        if (dataCollectorSharedPreferences.isCellServiceEnable() &&
                dataCollectorSharedPreferences.isCellStatesDisplay()) {
            unregisterReceiver(cellStatusReceiver);
        }

        if (dataCollectorSharedPreferences.isConnectivityServiceEnable() &&
                dataCollectorSharedPreferences.isConnectivityStatesDisplay()) {
            unregisterReceiver(connectivityStatusActivityReceiver);
        }

        if (dataCollectorSharedPreferences.isFileServiceEnable()) {
            unregisterReceiver(fileStatusActivityReceiver);
        }

        if (dataCollectorSharedPreferences.isIperfServiceEnable()) {
            unregisterReceiver(iperfStatusReceiver);
        }

        super.onPause();
    }

    public class CellStatusReceiver extends BroadcastReceiver {
        private final char net_c[] = new char[128];
        private final char isg_c[] = new char[128];
        private final char gss_c[] = new char[128];
        private final char ger_c[] = new char[128];
        private final char cdm_c[] = new char[128];
        private final char cde_c[] = new char[128];
        private final char evd_c[] = new char[128];
        private final char eve_c[] = new char[128];
        private final char evs_c[] = new char[128];
        private final char lss_c[] = new char[128];
        private final char lrp_c[] = new char[128];
        private final char lrq_c[] = new char[128];
        private final char lrs_c[] = new char[128];
        private final char lcq_c[] = new char[128];
        private final char cid_c[] = new char[128];
        private final char rnc_c[] = new char[128];
        private final char psc_c[] = new char[128];
        private final char clo_c[] = new char[128];
        private final char ddt_c[] = new char[128];
        private final char dst_c[] = new char[128];
        private int networkType;

        private boolean isGsm, preIsGsm;
        private int gsmSignalStrength, preGsmSignalStrength;
        private int gsmBitErrorRate, preGsmBitErrorRate;
        private int cdmaDbm, preCdmaDbm;
        private int cdmaEcio, preCdmaEcio;
        private int evdoDbm, preEvdoDbm;
        private int evdoEcio, preEvdoEcio;
        private int evdoSnr, preEvdoSnr;
        private int lteSignalStrength, preLteSignalStrength;
        private int lteRsrp, preLteRsrp;
        private int lteRsrq, preLteRsrq;
        private int lteRssnr, preLteRssnr;
        private int lteCqi, preLteCqi;

        private int cellId, rncId, psc, cellLocation, dataDirection, dataState;
        private int preNetworkType, preCellId, preRncId, prePsc, preCellLocation;
        private int preDataDirection, preDataState;
        private int char_len;
        private boolean isAlreadyDisplayed = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            networkType = bundle.getInt("NetworkType");

            isGsm = bundle.getBoolean("IsGsm");
            gsmSignalStrength = bundle.getInt("GsmSignalStrength");
            gsmBitErrorRate = bundle.getInt("GsmBitErrorRate");
            cdmaDbm = bundle.getInt("CdmaDbm");
            cdmaEcio = bundle.getInt("CdmaEcio");
            evdoDbm = bundle.getInt("EvdoDbm");
            evdoEcio = bundle.getInt("EvdoEcio");
            evdoSnr = bundle.getInt("EvdoSnr");
            lteSignalStrength = bundle.getInt("LteSignalStrength");
            lteRsrp = bundle.getInt("LteRsrp");
            lteRsrq = bundle.getInt("LteRsrq");
            lteRssnr = bundle.getInt("LteRssnr");
            lteCqi = bundle.getInt("LteCqi");

            cellId = bundle.getInt("CellId");
            rncId = bundle.getInt("RNCId");
            psc = bundle.getInt("PSC");
            cellLocation = bundle.getInt("CellLocation");
            dataDirection = bundle.getInt("DataDirection");
            dataState = bundle.getInt("DataState");

            if (networkType != preNetworkType || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("NetworkType: ");
                stringBuilder.append(CellularNetworkType.getNetworkTypeStr(networkType));

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, net_c, 0);
                networkTypeTv.setText(net_c, 0, char_len);

                preNetworkType = networkType;
            }

            if (isGsm != preIsGsm || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("IsGsm: ");
                stringBuilder.append(isGsm);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, isg_c, 0);
                isGsmTv.setText(isg_c, 0, char_len);

                preIsGsm = isGsm;
            }

            if (gsmSignalStrength != preGsmSignalStrength || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("GsmSignalStrength: ");
                stringBuilder.append(gsmSignalStrength);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, gss_c, 0);
                gsmSignalStrengthTv.setText(gss_c, 0, char_len);

                preGsmSignalStrength = gsmSignalStrength;
            }

            if (gsmBitErrorRate != preGsmBitErrorRate || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("GsmBitErrorRate: ");
                stringBuilder.append(gsmBitErrorRate);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, ger_c, 0);
                gsmBitErrorRateTv.setText(ger_c, 0, char_len);

                preGsmBitErrorRate = gsmBitErrorRate;
            }

            if (cdmaDbm != preCdmaDbm || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("CdmaDbm: ");
                stringBuilder.append(cdmaDbm);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, cdm_c, 0);
                cdmaDbmTv.setText(cdm_c, 0, char_len);

                preCdmaDbm = cdmaDbm;
            }

            if (cdmaEcio != preCdmaEcio || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("CdmaEcio: ");
                stringBuilder.append(cdmaEcio);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, cde_c, 0);
                cdmaEcioTv.setText(cde_c, 0, char_len);

                preCdmaEcio = cdmaEcio;
            }

            if (evdoDbm != preEvdoDbm || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("EvdoDbm: ");
                stringBuilder.append(evdoDbm);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, evd_c, 0);
                evdoDbmTv.setText(evd_c, 0, char_len);

                preEvdoDbm = evdoDbm;
            }

            if (evdoEcio != preEvdoEcio || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("EvdoEcio: ");
                stringBuilder.append(evdoEcio);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, eve_c, 0);
                evdoEcioTv.setText(eve_c, 0, char_len);

                preEvdoEcio = evdoEcio;
            }

            if (evdoSnr != preEvdoSnr || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("EvdoSnr: ");
                stringBuilder.append(evdoSnr);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, evs_c, 0);
                evdoSnrTv.setText(evs_c, 0, char_len);

                preEvdoSnr = evdoSnr;
            }

            if (lteSignalStrength != preLteSignalStrength || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("LteSignalStrength: ");
                stringBuilder.append(lteSignalStrength);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, lss_c, 0);
                lteSignalStrengthTv.setText(lss_c, 0, char_len);

                preLteSignalStrength = lteSignalStrength;
            }

            if (lteRsrp != preLteRsrp || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("LteRsrp: ");
                stringBuilder.append(lteRsrp);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, lrp_c, 0);
                lteRsrpTv.setText(lrp_c, 0, char_len);

                preLteRsrp = lteRsrp;
            }

            if (lteRsrq != preLteRsrq || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("LteRsrq: ");
                stringBuilder.append(lteRsrq);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, lrq_c, 0);
                lteRsrqTv.setText(lrq_c, 0, char_len);

                preLteRsrq = lteRsrq;
            }

            if (lteRssnr != preLteRssnr || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("LteRssnr: ");
                stringBuilder.append(lteRssnr);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, lrs_c, 0);
                lteRssnrTv.setText(lrs_c, 0, char_len);

                preLteRssnr = lteRssnr;
            }

            if (lteCqi != preLteCqi || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("LteCqi: ");
                stringBuilder.append(lteCqi);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, lcq_c, 0);
                lteCqiTv.setText(lcq_c, 0, char_len);

                preLteCqi = lteCqi;
            }

            if (cellId != preCellId || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("CellId: ");
                stringBuilder.append(cellId);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, cid_c, 0);
                cellIdTv.setText(cid_c, 0, char_len);

                preCellId = cellId;
            }

            if (rncId != preRncId || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("RNCId: ");
                stringBuilder.append(rncId);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, rnc_c, 0);
                rncIdTv.setText(rnc_c, 0, char_len);

                preRncId = rncId;
            }

            if (psc != prePsc || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("PSC: ");
                stringBuilder.append(psc);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, psc_c, 0);
                pscTv.setText(psc_c, 0, char_len);

                prePsc = psc;
            }

            if (cellLocation != preCellLocation || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("CellLocation: ");
                stringBuilder.append(cellLocation);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, clo_c, 0);
                cellLocationTv.setText(clo_c, 0, char_len);

                preCellLocation = cellLocation;
            }

            if (dataDirection != preDataDirection || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("DataDirection: ");
                stringBuilder.append(DataActivityType.getDataActivityTypeStr(dataDirection));

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, ddt_c, 0);
                dataDirectionTv.setText(ddt_c, 0, char_len);

                preDataDirection = dataDirection;
            }

            if (dataState != preDataState || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("DataState: ");
                stringBuilder.append(DataConnectionStateType.getDataConnectionStateTypeStr(dataState));

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, dst_c, 0);
                dataStateTv.setText(dst_c, 0, char_len);

                preDataState = dataState;
            }

            isAlreadyDisplayed = true;
        }
    }

    public class ConnectivityStatusActivityReceiver extends BroadcastReceiver {
        private NetworkInfo.DetailedState detailedState, preDetailedState;
        private boolean isAvailable, isConnected, isConnectedOrConnecting;
        private boolean preIsAvailable, preIsConnected, preIsConnectedOrConnecting;

        private int char_len;
        private char dts_c[] = new char[128];
        private char iav_c[] = new char[128];
        private char ict_c[] = new char[128];
        private char icc_c[] = new char[128];

        private boolean isAlreadyDisplayed = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            detailedState = (NetworkInfo.DetailedState) bundle.get("DetailedState");
            isAvailable = bundle.getBoolean("IsAvailable");
            isConnected = bundle.getBoolean("IsConnected");
            isConnectedOrConnecting = bundle.getBoolean("IsConnectedOrConnecting");

            if (detailedState != preDetailedState || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("DetailedState: ");
                stringBuilder.append(detailedState);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, dts_c, 0);
                connDetailedStateTv.setText(dts_c, 0, char_len);

                preDetailedState = detailedState;
            }

            if (isAvailable != preIsAvailable || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("IsAvailable: ");
                stringBuilder.append(isAvailable);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, iav_c, 0);
                connAvailableTv.setText(iav_c, 0, char_len);

                preIsAvailable = isAvailable;
            }

            if (isConnected != preIsConnected || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("IsConnected: ");
                stringBuilder.append(isConnected);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, ict_c, 0);
                connConnectedTv.setText(ict_c, 0, char_len);

                preIsConnected = isConnected;
            }

            if (isConnectedOrConnecting != preIsConnectedOrConnecting || !isAlreadyDisplayed) {
                stringBuilder.setLength(0);
                stringBuilder.append("IsConnectedOrConnecting: ");
                stringBuilder.append(isConnectedOrConnecting);

                char_len = stringBuilder.length();
                stringBuilder.getChars(0, char_len, icc_c, 0);
                connConnectedOrConnectingTv.setText(icc_c, 0, char_len);

                preIsConnectedOrConnecting = isConnectedOrConnecting;
            }

            isAlreadyDisplayed = true;
        }
    }

    public class FileStatusActivityReceiver extends BroadcastReceiver {
        private int totalError = 0;
        private int totalOk = 0;

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();

            int type = bundle.getInt("Type");
            boolean isTransferOk = bundle.getBoolean("FileStatus");
            String fileMsg = bundle.getString("FileMsg");

            if (type == 2) {
                if (isTransferOk) {
                    totalOk++;
                    fileMsgOkTv.setText("OK: " + totalOk);
                    fileStatusTv.setTextColor(Color.GREEN);
                } else {
                    totalError++;
                    fileMsgErrorTv.setText("ERROR: " + totalError);
                    fileStatusTv.setTextColor(Color.RED);
                }
            }

            if (!fileStatusTv.getText().toString().equals("FileStatus: " + isTransferOk)) {
                fileStatusTv.setText("FileStatus: " + isTransferOk);
            }

            if (!fileMsgTv.getText().toString().equals("FileStatus: " + fileMsg)) {
                fileMsgTv.setText("FileMsg: " + fileMsg);
            }
        }
    }

    public class IperfStatusReceiver extends BroadcastReceiver {
        private int totalOk = 0;
        private int totalError = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            IperfResultType resultType = (IperfResultType) bundle.get("Type");
            String result = bundle.getString("Result");

            if (resultType == null) {
                resultType = IperfResultType.UNKNOWN;
            }

            if (resultType == IperfResultType.RIGHT) {
                totalOk++;
                iperfOkTv.setText("OK: " + totalOk);
            } else if (resultType == IperfResultType.GET_ERROR) {
                totalError++;
                iperfErrorTv.setText("ERROR: " + totalError);
            }

            if (!iperfStatusTv.getText().toString().equals("IperfStatus: " + resultType.getTypeString())) {
                iperfStatusTv.setText("IperfStatus: " + resultType.getTypeString());

                if (resultType == IperfResultType.START) {
                    iperfStatusTv.setTextColor(Color.LTGRAY);
                } else if (resultType == IperfResultType.RIGHT) {
                    iperfStatusTv.setTextColor(Color.GREEN);
                } else if (resultType == IperfResultType.IS_NULL) {
                    iperfStatusTv.setTextColor(Color.YELLOW);
                } else {
                    iperfStatusTv.setTextColor(Color.RED);
                }
            }

            if (!iperfMsgTv.getText().toString().equals("IperfMsg: " + result)) {
                iperfMsgTv.setText("IperfMsg: " + result);
            }
        }
    }
}
