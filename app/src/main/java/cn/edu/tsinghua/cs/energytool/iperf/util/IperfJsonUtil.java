package cn.edu.tsinghua.cs.energytool.iperf.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * IperfJsonUtil
 * Created by zhangdi on 3/14/14.
 */
public class IperfJsonUtil {

    public static List<String> parserIperfResult(String resultStr) {
        String parseResult;
        String detailResult;
        List<String> listResult = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            long time = jsonObject.getJSONObject("start").getJSONObject("timestamp").getLong("timesecs");

            JSONObject receiver = ((JSONObject) jsonObject.getJSONObject("end").getJSONArray("streams").get(0)).getJSONObject("receiver");
            String receiver_bytes = receiver.getString("bytes");
            String receiver_seconds = receiver.getString("seconds");
            String receiver_start = receiver.getString("start");
            String receiver_end = receiver.getString("end");
            String receiver_socket = receiver.getString("socket");
            String receiver_bits_per_second = receiver.getString("bits_per_second");
            String receiver_retransmits = "0";
            if (receiver.has("retransmits")) {
                receiver_retransmits = receiver.getString("retransmits");
            }

            JSONObject sender = ((JSONObject) jsonObject.getJSONObject("end").getJSONArray("streams").get(0)).getJSONObject("sender");
            String sender_bytes = receiver.getString("bytes");
            String sender_seconds = receiver.getString("seconds");
            String sender_start = receiver.getString("start");
            String sender_end = receiver.getString("end");
            String sender_socket = receiver.getString("socket");
            String sender_bits_per_second = receiver.getString("bits_per_second");
            String sender_retransmits = "0";
            if (sender.has("retransmits")) {
                sender_retransmits = sender.getString("retransmits");
            }

            parseResult = time + "\t" + receiver_socket + "\t" + receiver_start + "\t" + receiver_end + "\t"
                    + receiver_seconds + "\t" + receiver_bytes + "\t" + receiver_bits_per_second + "\t" + receiver_retransmits + "\t"
                    + sender_socket + "\t" + sender_start + "\t" + sender_end + "\t"
                    + sender_seconds + "\t" + sender_bytes + "\t" + sender_bits_per_second + "\t" + sender_retransmits + "\t";


            JSONArray intervals = jsonObject.getJSONArray("intervals");
            parseResult = parseResult + intervals.length() + "\t";

            detailResult = "";
            for (int i = 0; i < intervals.length(); i++) {
                JSONObject streams = (JSONObject) ((JSONObject) intervals.get(i)).getJSONArray("streams").get(0);
                String streams_socket = streams.getString("socket");
                String streams_start = streams.getString("start");
                String streams_end = streams.getString("end");
                String streams_seconds = streams.getString("seconds");
                String streams_bytes = streams.getString("bytes");
                String streams_bits_per_second = streams.getString("bits_per_second");
                String streams_omitted = streams.getString("omitted");
                detailResult = detailResult + streams_socket + "\t" + streams_start + "\t" + streams_end + "\t"
                        + streams_seconds + "\t" + streams_bytes + "\t"
                        + streams_bits_per_second + "\t" + streams_omitted + "\t";
            }

        } catch (JSONException e) {
            e.printStackTrace();
            parseResult = "Parse result error, and error msg = " + e.getMessage();
            detailResult = "Error";
        }

        listResult.add(parseResult);
        listResult.add(detailResult);

        return listResult;
    }

    public static String parserIperfError(String resultStr) {
        String parseError;

        try {
            JSONObject jsonObject = new JSONObject(resultStr);

            parseError = jsonObject.getString("error");
        } catch (JSONException e) {
            e.printStackTrace();
            parseError = "Parse error message error, and error msg = " + e.getMessage();
        }

        return parseError;
    }
}
