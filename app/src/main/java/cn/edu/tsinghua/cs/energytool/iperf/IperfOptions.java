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

package cn.edu.tsinghua.cs.energytool.iperf;

public class IperfOptions {

    private String serverIP;
    private int serverPort;
    private boolean isReverse, isOutputInJsonFormat;
    private int lengthOfBuffer;
    private int time;

    public IperfOptions() {
        isReverse = true;
        isOutputInJsonFormat = true;
        lengthOfBuffer = 128;
        time = 8;
        serverPort = 5201;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public boolean isReverse() {
        return isReverse;
    }

    public void setReverse(boolean isReverse) {
        this.isReverse = isReverse;
    }

    public boolean isOutputInJsonFormat() {
        return isOutputInJsonFormat;
    }

    public void setOutputInJsonFormat(boolean isOutputInJsonFormat) {
        this.isOutputInJsonFormat = isOutputInJsonFormat;
    }

    public int getLengthOfBuffer() {
        return lengthOfBuffer;
    }

    public void setLengthOfBuffer(int lengthOfBuffer) {
        this.lengthOfBuffer = lengthOfBuffer;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("-c ");
        builder.append(serverIP);
        builder.append(" -p ");
        builder.append(serverPort);
        builder.append(" ");

        if (isReverse) {
            builder.append("-R ");
        }

        if (isOutputInJsonFormat) {
            builder.append("-J ");
        }

        builder.append("-l ");
        builder.append(lengthOfBuffer);
        builder.append("K -t ");
        builder.append(time);
        builder.append(" -");
        //builder.append(" -O 2 -");// omit the first two seconds

        return builder.toString();
    }
}
