package cn.edu.tsinghua.cs.energytool.iperf.result;

/**
 * IperfResultType:
 * start, right, null, error, exception
 * Created by zhangdi on 3/15/14.
 */
public enum IperfResultType {
    // type: 0 - start, 1 - right, 2 - result is null, 3 - contains error, 4 - exception
    START("start", 0), RIGHT("right", 1), IS_NULL("null", 2), GET_ERROR("error", 3), GET_EXCEPTION("exception", 4), UNKNOWN("unknown", 5);

    private String typeString;
    private int type;


    IperfResultType(String typeString, int type) {
        this.typeString = typeString;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
