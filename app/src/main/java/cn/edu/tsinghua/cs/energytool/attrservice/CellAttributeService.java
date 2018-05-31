/**
 * MIT License
 *
 * Copyright (c) 2018 Di Zhang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.edu.tsinghua.cs.energytool.attrservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import cn.edu.tsinghua.cs.energytool.preference.EnergyToolSharedPreferences;

public class CellAttributeService extends Service {
    private static final StringBuilder stringBuilder = new StringBuilder(256);
    private boolean isThreadDisable = false;
    /*
     * Phone States includes:
     * Signal Strength - asu
     * Network Type - netType
     * CellID - cellId
     * CellLocation - cellLocation
     */
    private TelephonyManager telephonyManager;
    private EnergyToolPhoneStateListener dataCollectorPhoneStateListener;
    private int networkType;

    private long currentTime;

    // modify for monitoring more info
    private boolean isGsm;
    private int gsmSignalStrength; // Valid values are (0-31, 99) as defined in TS 27.007 8.5
    private int gsmBitErrorRate;   // bit error rate (0-7, 99) as defined in TS 27.007 8.5
    private int cdmaDbm;   // This value is the RSSI value
    private int cdmaEcio;  // This value is the Ec/Io
    private int evdoDbm;   // This value is the EVDO RSSI value
    private int evdoEcio;  // This value is the EVDO Ec/Io
    private int evdoSnr;   // Valid values are 0-8.  8 is the highest signal to noise ratio
    private int lteSignalStrength;
    private int lteRsrp;
    private int lteRsrq;
    private int lteRssnr;
    private int lteCqi;

    private int cellId;
    private int rncId;
    private int psc;
    private int cellLocation;
    private int dataDirection;
    private int dataState;
    private String neighboringCellInfo = "";
    private int neighboringCellInfoLength;

    @Override
    public void onCreate() {
        super.onCreate();

        // phone state
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        dataCollectorPhoneStateListener = new EnergyToolPhoneStateListener();
        telephonyManager.listen(dataCollectorPhoneStateListener,
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                        | PhoneStateListener.LISTEN_CELL_LOCATION
                        | PhoneStateListener.LISTEN_DATA_ACTIVITY
                        | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

        // request location update to init signal, cellId and cellLocation parameters
        //CellLocation.requestLocationUpdate();
        //neighboringCellInfo = getNeighboringCellInfo();

        //Log.d("EnergyTool", telephonyManager.getCellLocation().toString());

        new Thread(new Runnable() {
            private EnergyToolSharedPreferences dataCollectorSharedPreferences;
            private int cellServiceInterval;
            private Intent intent;

            @Override
            public void run() {
                dataCollectorSharedPreferences = new EnergyToolSharedPreferences();
                cellServiceInterval = dataCollectorSharedPreferences.getCellServiceInterval();

                while (!isThreadDisable) {
                    try {
                        Thread.sleep(cellServiceInterval);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    currentTime = System.currentTimeMillis();

                    // network type
                    networkType = telephonyManager.getNetworkType();

                    intent = new Intent();

                    intent.putExtra("CurrentTime", currentTime);

                    intent.putExtra("NetworkType", networkType);

                    intent.putExtra("IsGsm", isGsm);
                    intent.putExtra("GsmSignalStrength", gsmSignalStrength);
                    intent.putExtra("GsmBitErrorRate", gsmBitErrorRate);
                    intent.putExtra("CdmaDbm", cdmaDbm);
                    intent.putExtra("CdmaEcio", cdmaEcio);
                    intent.putExtra("EvdoDbm", evdoDbm);
                    intent.putExtra("EvdoEcio", evdoEcio);
                    intent.putExtra("EvdoSnr", evdoSnr);
                    intent.putExtra("LteSignalStrength", lteSignalStrength);
                    intent.putExtra("LteRsrp", lteRsrp);
                    intent.putExtra("LteRsrq", lteRsrq);
                    intent.putExtra("LteRssnr", lteRssnr);
                    intent.putExtra("LteCqi", lteCqi);

                    intent.putExtra("CellId", cellId);
                    intent.putExtra("RNCId", rncId);
                    intent.putExtra("PSC", psc);
                    intent.putExtra("CellLocation", cellLocation);

                    // new added in 20140124
                    intent.putExtra("DataDirection", dataDirection);
                    intent.putExtra("DataState", dataState);

                    intent.putExtra("NeighboringCellInfo", neighboringCellInfo);

                    intent.setAction("CellAttributeService");
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadDisable = true;
        telephonyManager.listen(dataCollectorPhoneStateListener,
                PhoneStateListener.LISTEN_NONE);
        Log.v("EnergyTool", "CellAttributeService Destroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private String getNeighboringCellInfo() {
        List<NeighboringCellInfo> neighboringCellInfoList = telephonyManager.getNeighboringCellInfo();

        stringBuilder.setLength(0);
        if (neighboringCellInfoList != null) {
            neighboringCellInfoLength = neighboringCellInfoList.size();

            stringBuilder.append(neighboringCellInfoLength);
            stringBuilder.append("\t");

            for (NeighboringCellInfo neighboringCellInfo : neighboringCellInfoList) {
                stringBuilder.append(neighboringCellInfo.getCid());
                stringBuilder.append("\t");
                stringBuilder.append(neighboringCellInfo.getLac());
                stringBuilder.append("\t");
                stringBuilder.append(neighboringCellInfo.getNetworkType());
                stringBuilder.append("\t");
                stringBuilder.append(neighboringCellInfo.getPsc());
                stringBuilder.append("\t");
                stringBuilder.append(neighboringCellInfo.getRssi());
                stringBuilder.append("\t");
            }
        } else {
            stringBuilder.append("-1");
            stringBuilder.append("\t");
            Log.v("EnergyTool", "NeighboringCellInfo is NULL");
        }

        return stringBuilder.toString();
    }

    /**
     * private class for PhoneStateListener to get the signal strength and cell
     * information
     *
     * @author zhangdi
     */
    private class EnergyToolPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            isGsm = signalStrength.isGsm();

            gsmSignalStrength = signalStrength.getGsmSignalStrength();
            gsmBitErrorRate = signalStrength.getGsmBitErrorRate();
            cdmaDbm = signalStrength.getCdmaDbm();
            cdmaEcio = signalStrength.getCdmaEcio();
            evdoDbm = signalStrength.getEvdoDbm();
            evdoEcio = signalStrength.getEvdoEcio();
            evdoSnr = signalStrength.getEvdoSnr();

            // Reflection code for let start
            try {
                Method[] methods = android.telephony.SignalStrength.class
                        .getMethods();
                for (Method m : methods) {
                    if (m.getName().equals("getLteSignalStrength")) {
                        lteSignalStrength = (int) m.invoke(signalStrength);
                    } else if (m.getName().equals("getLteRsrp")) {
                        lteRsrp = (int) m.invoke(signalStrength);
                    } else if (m.getName().equals("getLteRsrq")) {
                        lteRsrq = (int) m.invoke(signalStrength);
                    } else if (m.getName().equals("getLteRssnr")) {
                        lteRssnr = (int) m.invoke(signalStrength);
                    } else if (m.getName().equals("getLteCqi")) {
                        lteCqi = (int) m.invoke(signalStrength);
                    }
                }
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException |
                    InvocationTargetException e) {
                e.printStackTrace();
            }
            // Reflection code for lte end

            neighboringCellInfo = getNeighboringCellInfo();

            /*List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
            if (allCellInfo == null) {
                Log.v("EnergyTool", "all cell info is null");
            } else {
                Log.v("EnergyTool", "all cell info is not null" + allCellInfo.size());

                for (CellInfo cellInfo : allCellInfo) {
                    if (cellInfo instanceof CellInfoGsm) {
                        CellInfoGsm cellInfoGsm = (CellInfoGsm)cellInfo;
                        Log.v("EnergyTool", "GSM: " + cellInfoGsm.getCellSignalStrength().getDbm() + ", " +
                                cellInfoGsm.getCellIdentity().getCid());
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma)cellInfo;
                        Log.v("EnergyTool", "WCDMA: " + cellInfoWcdma.getCellSignalStrength().getDbm() + ", " +
                                cellInfoWcdma.getCellIdentity().getCid());
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte)cellInfo;
                        Log.v("EnergyTool", "LTE: " + cellInfoLte.getCellSignalStrength().getDbm() + ", " +
                                cellInfoLte.getCellIdentity().getCi());
                    } else if (cellInfo instanceof CellInfoCdma) {
                        CellInfoCdma cellInfoCdma = (CellInfoCdma)cellInfo;
                        Log.v("EnergyTool", "CDMA: " + cellInfoCdma.getCellSignalStrength().getDbm());
                    }
                }
            }*/
        }

        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            super.onCellInfoChanged(cellInfo);

            if (cellInfo != null)
                Log.v("EnergyTool@Test", "onCellInfoChanged length: " + cellInfo.size());
            else
                Log.v("EnergyTool@Test", "onCellInfoChanged is NULL");
        }

        /*
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);

            if (serviceState != null)
                Log.v("EnergyTool@Test", "serviceState: " + serviceState.toString());
            else
                Log.v("EnergyTool@Test", "serviceState is NULL");

        }*/

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            if (location instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) location;
                cellId = gsmCellLocation.getCid() % 65536;
                cellLocation = gsmCellLocation.getLac() % 65536;
                rncId = (gsmCellLocation.getCid() >> 16) % 65536;
                psc = gsmCellLocation.getPsc();

                //Log.v("EnergyTool", "cellId changed as: " + cellId + ", rncId: " + rncId
                //        + ", cellLocation: " + cellLocation + ", psc: " + psc);
            } else if (location instanceof CdmaCellLocation) {
                // not implementation
                Log.v("EnergyTool", "CdmaCellocation");
            }
        }

        @Override
        public void onDataActivity(int direction) {
            super.onDataActivity(direction);
            dataDirection = direction;
            Log.v("EnergyTool", "data_direction changed as: " + dataDirection);
        }

        @Override
        public void onDataConnectionStateChanged(int state) {
            super.onDataConnectionStateChanged(state);
            dataState = state;
            Log.v("EnergyTool", "data_state changed as: " + dataState);
        }
    }
}
