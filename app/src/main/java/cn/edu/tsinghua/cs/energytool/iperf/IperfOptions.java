package cn.edu.tsinghua.cs.energytool.iperf;

/**
 * Created by zhangdi on 3/12/14.
 * IperfOptions
 */
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
