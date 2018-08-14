package hq.demo.net;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Description: <p>
 * <p>
 * @Project: workspace-android
 * @Files: ${CLASS_NAME}
 * @Author: HQ
 * @Version: 0.0.1
 * @Date: 2018/8/10 下午12:42
 * @Copyright:
 */
public class UrlConnectionMananger {

    public static HttpURLConnection getHttpURLConnection(String urlStr) {
        HttpURLConnection httpURLConnection = null;
        try {

            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(30000);//设置连接超时
            httpURLConnection.setReadTimeout(30000);//设置读取超时
            httpURLConnection.setRequestMethod("POST");//设置请求方式
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");//设置头
            httpURLConnection.setDoInput(true);//接收输入流
            httpURLConnection.setDoOutput(true);//通过url进行输出
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpURLConnection;
    }


    public static void postParams(OutputStream outputStream, List<NameValuePair> paramsList) throws IOException {

        StringBuilder builder = new StringBuilder();
        for (NameValuePair nameValuePair : paramsList) {
            if (!TextUtils.isEmpty(builder)) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(nameValuePair.getName(),"UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(nameValuePair.getValue(),"UTF-8"));
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
        Log.i("#######",builder.toString());
        writer.write(builder.toString());
        writer.flush();
        writer.close();
    }

}
