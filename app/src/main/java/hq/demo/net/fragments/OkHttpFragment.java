package hq.demo.net.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import hq.demo.net.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "OkHttpFragment";
    private TextView resultView;
    private Button getView;
    private Button postView;
    private Button postJsonView;
    private Button getSynchronizedView;
    private Button postSynchronizedView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_okhttp, container, false);
        getView = (Button) view.findViewById(R.id.id_http_get_button);
        postView = (Button) view.findViewById(R.id.id_http_post_button);
        postJsonView = (Button) view.findViewById(R.id.id_http_post_json_button);
        getSynchronizedView = (Button) view.findViewById(R.id.id_http_get_synchronized_button);
        postSynchronizedView = (Button) view.findViewById(R.id.id_http_post_synchronized_button);

        resultView = (TextView) view.findViewById(R.id.id_result_okhttp_view);
        initListener();
        return view;
    }

    private void initListener() {
        getView.setOnClickListener(this);
        postView.setOnClickListener(this);
        postJsonView.setOnClickListener(this);
        getSynchronizedView.setOnClickListener(this);
        postSynchronizedView.setOnClickListener(this);
    }

    private Handler mHandler;

    {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (msg.obj == null) {
                            resultView.setText("null");
                        } else {
                            resultView.setText("error:" + msg.obj.toString());
                        }
                        break;
                    case 1:
                        if (msg.obj == null) {
                            resultView.setText("null");
                        } else {
                            resultView.setText("response:" + msg.obj.toString());
                        }
                        break;

                }
            }
        };
    }


    /**
     * 测试okhttp的get方法
     */
    private void testOkhttpGet() {
        String url = "http://api.k780.com/?app=weather.history";
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                mHandler.sendMessage(message);
                Log.d(TAG, "onFailure: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                mHandler.sendMessage(message);
                Log.d(TAG, "response: " + message.obj.toString());
            }
        });

    }


    /**
     * 测试okhttp的post方法
     */
    private void testOkhttpPost() {
        String url = "http://api.k780.com/?app=weather.history";//
        // &weaid=1&date=2018-08-13&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("weaid", "1")
                .add("date", "2018-08-13")
                .add("appkey", "10003")
                .add("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4")
                .add("format", "json")
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                mHandler.sendMessage(message);
                Log.d(TAG, "response: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                mHandler.sendMessage(message);
                Log.d(TAG, "response: " + message.obj.toString());
            }
        });

    }

    /**
     * 测试同步okhttp的post方法，在使用的時候我們必须另起线程
     */
    private void testOkhttpPostJson() {
        String url = "http://api.k780.com/?app=weather.history";//
        // &weaid=1&date=2018-08-13&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";


        String json = "{\"format\":\"json\",\"weaid\":1,\"date\":\"2018-08-13\",\"appkey\":\"10003\",\"sign\":\"b59bc3ef6191eb9f747dd4e83c99f2a4\"}";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().post(body).url(url).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                message.obj = e.getMessage();
                mHandler.sendMessage(message);
                Log.d(TAG, "response: " + message.obj.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = response.body().string();//string不能调用两次 被调用一次就关闭了，这里调用两次会报异常
                mHandler.sendMessage(message);
                Log.d(TAG, "response: " + message.obj.toString());
            }
        });
    }

    /**
     * 测试同步okhttp的get方法，在使用的時候我們必须另起线程
     */
    private String testOkhttpGetSynchronized() throws IOException {
        String url = "http://api.k780.com/?app=weather.history";

        okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }


    /**
     * 测试同步okhttp的post方法，在使用的時候我們必须另起线程
     */
    private String testOkhttpPostSynchronized() throws IOException {
        String url = "http://api.k780.com/?app=weather.history";//
        // &weaid=1&date=2018-08-13&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";

        RequestBody body = new FormBody.Builder()
                .add("weaid", "1")
                .add("date", "2018-08-13")
                .add("appkey", "10003")
                .add("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4")
                .add("format", "json")
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_http_get_button:
                testOkhttpGet();
                break;
            case R.id.id_http_post_button:
                testOkhttpPost();
                break;
            case R.id.id_http_post_json_button:
                testOkhttpPostJson();
                break;
            case R.id.id_http_get_synchronized_button:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        try {
                            String result = testOkhttpGetSynchronized();
                            if (!TextUtils.isEmpty(result)) {
                                message.what = 1;
                                message.obj = result;
                            } else {
                                message.what = 0;
                                message.obj = null;
                            }

                        } catch (IOException e) {
                            message.what = 0;
                            message.obj = null;
                            e.printStackTrace();
                        } finally {
                            mHandler.sendMessage(message);
                        }
                    }
                }).start();

                break;
            case R.id.id_http_post_synchronized_button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        try {
                            String result = testOkhttpPostSynchronized();
                            if (!TextUtils.isEmpty(result)) {
                                message.what = 1;
                                message.obj = result;
                            } else {
                                message.what = 0;
                                message.obj = null;
                            }

                        } catch (IOException e) {
                            message.what = 0;
                            message.obj = null;
                            e.printStackTrace();
                        } finally {
                            mHandler.sendMessage(message);
                        }
                    }
                }).start();

                break;
        }
    }
}
