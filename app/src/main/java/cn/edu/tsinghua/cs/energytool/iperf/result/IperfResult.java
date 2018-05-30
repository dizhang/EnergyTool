package cn.edu.tsinghua.cs.energytool.iperf.result;

/**
 * Created by zhangdi on 3/14/14.
 */
public class IperfResult {
    private IperfResultType resultType;
    private String result;

    public IperfResult() {
    }

    public IperfResult(IperfResultType resultType, String result) {
        this.resultType = resultType;
        this.result = result;
    }

    public IperfResultType getResultType() {
        return resultType;
    }

    public void setResultType(IperfResultType resultType) {
        this.resultType = resultType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
