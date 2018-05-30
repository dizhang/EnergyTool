package cn.edu.tsinghua.cs.energytool.util;

public enum SubwayEventType {
    ARRSBW(1, "Arrive_Station"), ENTSBW(2, "Enetr_Car"), LEVSBW(3, "Leave_Station"),
    SBWSRT(4, "Car_Start"), SBWSTP(5, "Car_Stop"), EXTSBW(6, "Exit_Car");

    private int type;
    private String description;

    SubwayEventType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
