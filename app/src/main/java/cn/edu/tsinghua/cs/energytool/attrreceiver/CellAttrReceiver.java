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

package cn.edu.tsinghua.cs.energytool.attrreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.edu.tsinghua.cs.energytool.log.ETLogger;
import cn.edu.tsinghua.cs.energytool.log.ETLoggerType;

public class CellAttrReceiver extends BroadcastReceiver {
    private static final StringBuilder logStr = new StringBuilder(512);
    private static final StringBuilder logStr2 = new StringBuilder(512);
    private static final char char_str[] = new char[512];
    private static final char ncl_char_str[] = new char[512];
    private Bundle bundle;

    private long currentTime;

    private int networkType;

    private boolean isGsm;
    private int isGsmNumber;
    private int gsmSignalStrength;
    private int gsmBitErrorRate;
    private int cdmaDbm;
    private int cdmaEcio;
    private int evdoDbm;
    private int evdoEcio;
    private int evdoSnr;
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
    private String neighboringCellInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();
        currentTime = bundle.getLong("CurrentTime");

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

        neighboringCellInfo = bundle.getString("NeighboringCellInfo");

        isGsmNumber = isGsm ? 1 : 0;

        logStr.setLength(0);
        logStr.append(currentTime);
        logStr.append("\t");
        logStr.append(networkType);
        logStr.append("\t");
        logStr.append(isGsmNumber);
        logStr.append("\t");
        logStr.append(gsmSignalStrength);
        logStr.append("\t");
        logStr.append(gsmBitErrorRate);
        logStr.append("\t");
        logStr.append(cdmaDbm);
        logStr.append("\t");
        logStr.append(cdmaEcio);
        logStr.append("\t");
        logStr.append(evdoDbm);
        logStr.append("\t");
        logStr.append(evdoEcio);
        logStr.append("\t");
        logStr.append(evdoSnr);
        logStr.append("\t");
        logStr.append(lteSignalStrength);
        logStr.append("\t");
        logStr.append(lteRsrp);
        logStr.append("\t");
        logStr.append(lteRsrq);
        logStr.append("\t");
        logStr.append(lteRssnr);
        logStr.append("\t");
        logStr.append(lteCqi);
        logStr.append("\t");
        logStr.append(cellId);
        logStr.append("\t");
        logStr.append(rncId);
        logStr.append("\t");
        logStr.append(psc);
        logStr.append("\t");
        logStr.append(cellLocation);
        logStr.append("\t");
        logStr.append(dataDirection);
        logStr.append("\t");
        logStr.append(dataState);

        logStr.getChars(0, logStr.length(), char_str, 0);
        ETLogger.log(char_str, logStr.length(), ETLoggerType.CELLOG);

        logStr2.setLength(0);
        logStr2.append(currentTime);
        logStr2.append("\t");
        logStr2.append(neighboringCellInfo);


        logStr2.getChars(0, logStr2.length(), ncl_char_str, 0);
        ETLogger.log(ncl_char_str, logStr2.length(), ETLoggerType.NCLLOG);
    }
}
