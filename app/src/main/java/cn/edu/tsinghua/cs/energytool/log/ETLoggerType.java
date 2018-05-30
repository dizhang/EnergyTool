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
