package eu.marcocattaneo.instantlibrary.connection.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {

    /**
     * Create string params
     * @param params
     * @return
     */
    public static String getEncodedParams(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (String var : params.keySet()) {
            String key = var;
            String param = params.get(key);
            String value = null;
            try {
                value = URLEncoder.encode(param, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

}
