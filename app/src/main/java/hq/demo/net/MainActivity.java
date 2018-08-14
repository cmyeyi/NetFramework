package hq.demo.net;

import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView mImageView;
    private NetworkImageView mNetworkImageView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
//                    doHttpConnectionPost();
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    doGet();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
//                    doPost();
//                    testJsonRequest();
//                    testImageRequest();
//                    testImageLoader();
                    testNetImageView();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkImageView = (NetworkImageView) findViewById(R.id.id_network_image);
        mImageView = (ImageView) findViewById(R.id.id_image);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    /*############################GET请求 tart##################################################*/
    private AndroidHttpClient createHttpClient() {
        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");
        HttpParams httpParams = androidHttpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);//设置连接超时
        HttpConnectionParams.setSoTimeout(httpParams, 15000);//设置请求超时
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(httpParams, true);
        return androidHttpClient;
    }

    private String converStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }

    private void httpGet(String url) {
        HttpGet mHttpGet = new HttpGet(url);
        mHttpGet.addHeader("Connection", "Keep-Alive");
        try {
            AndroidHttpClient httpClient = createHttpClient();
            HttpResponse httpResponse = httpClient.execute(mHttpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            int code = httpResponse.getStatusLine().getStatusCode();
            if (httpEntity != null) {
                InputStream is = httpEntity.getContent();
                String response = converStreamToString(is);
                Log.i("#请求结果#", "code:" + code + "\n response:" + response);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doGet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpGet("http://api.k780.com/?app=weather.history&weaid=1&date=2015-07-20&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json");
            }
        }).start();

    }
/*############################ GET请求 end ##################################################*/

/*############################ POST请求 begin ##################################################*/

    private void httpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Connection", "Keep-Alive");
        try {
            HttpClient httpClient = createHttpClient();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("weaid", "1"));
            params.add(new BasicNameValuePair("date", "2015-07-20"));
            params.add(new BasicNameValuePair("appkey", "10003"));
            params.add(new BasicNameValuePair("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4"));
            params.add(new BasicNameValuePair("format", "json"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            int code = httpResponse.getStatusLine().getStatusCode();
            if (httpEntity != null) {
                InputStream is = httpEntity.getContent();
                String response = converStreamToString(is);
                Log.i("#请求结果#", "code:" + code + "\n response:" + response);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //http://api.k780.com/?app=weather.history&weaid=1&date=2015-07-20&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
                httpPost("http://api.k780.com/?app=weather.history");
            }
        }).start();

    }
/*############################ POST请求 end ##################################################*/


    private void useHttpPost(String url) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = UrlConnectionMananger.getHttpURLConnection(url);
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("weaid", "1"));
            params.add(new BasicNameValuePair("date", "2015-07-20"));
            params.add(new BasicNameValuePair("appkey", "10003"));
            params.add(new BasicNameValuePair("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4"));
            params.add(new BasicNameValuePair("format", "json"));

            UrlConnectionMananger.postParams(httpURLConnection.getOutputStream(), params);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            int code = httpURLConnection.getResponseCode();
            String response = converStreamToString(inputStream);
            Log.i("#请求结果#", "code:" + code + "\n response:" + response);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doHttpConnectionPost() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                useHttpPost("http://api.k780.com/?app=weather.history");
            }
        }).start();
    }










    /*############################ Volley ##################################################*/

    private void testStringRequest() {
        String url = "http://api.k780.com/?app=weather.history&weaid=1&date=2015-07-20&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response:", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        }
        );
        queue.add(stringRequest);
    }


    private void testJsonRequest() {
        String url = "http://api.k780.com/?app=weather.history&weaid=1&date=2015-07-20&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                WeatherList weatherList = new Gson().fromJson(response.toString(), WeatherList.class);
                if (weatherList != null) {
                    Log.d("##getSuccess##", "city:" + weatherList.getSuccess() + "\n");
                    for (Result r : weatherList.getResult()) {
                        Log.d("##result##", "city:" + r.getCitynm() + "weather:" + r.getWeather() + "\n");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    private void testImageRequest() {
        String url = "http://img3.imgtn.bdimg.com/it/u=2568996661,777819818&fm=27&gp=0.jpg";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest jsonObjectRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                if(response != null) {
                    mImageView.setImageBitmap(response);
                }

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }

    private void testImageLoader() {
        String url = "http://img3.imgtn.bdimg.com/it/u=2568996661,777819818&fm=27&gp=0.jpg";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        };
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(mImageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        ImageLoader imageLoader = new ImageLoader(queue,imageCache);
        imageLoader.get(url,imageListener);
    }

    private void testNetImageView() {
        String url = "http://img3.imgtn.bdimg.com/it/u=2568996661,777819818&fm=27&gp=0.jpg";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        };
        ImageLoader imageLoader = new ImageLoader(queue,imageCache);
        mNetworkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setImageUrl(url,imageLoader);
    }
}
