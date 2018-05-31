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

package cn.edu.tsinghua.cs.energytool.log;

public enum ETLoggerType {
    BATLOG("ET_Battery", 0), CELLOG("ET_Cell", 1), NETLOG("ET_Network", 2),
    ACCLOG("ET_Sensor_Acc", 3), LACLOG("ET_Sensor_Linear_Acc", 4), SBWLOG("ET_Subway", 5),
    FILELOG("ET_FileClient", 6), CONLOG("ET_Connect", 7), NCLLOG("ET_Neighbour_Cell", 8),
    IPFLOG("ET_Iperf", 9), IPDLOG("ET_Iperf_detail", 10), IPELOG("ET_Iperf_action", 11),
    SCNLOG("ET_Screen", 12);

    private String type;
    private int index;

    ETLoggerType(String type, int index) {
        this.type = type;
        this.index = index;
    }

    public String getType() {
        return this.type;
    }

    public int getIndex() {
        return index;
    }
}
