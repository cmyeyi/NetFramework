package hq.demo.net.fragments;

import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.aile.permission.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import hq.demo.net.PermissionManager;
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
    private Button uploadView;
    private Button downloadView;
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_okhttp, container, false);
        getView = (Button) view.findViewById(R.id.id_http_get_button);
        postView = (Button) view.findViewById(R.id.id_http_post_button);
        postJsonView = (Button) view.findViewById(R.id.id_http_post_json_button);
        uploadView = (Button) view.findViewById(R.id.id_http_post_upload_button);
        downloadView = (Button) view.findViewById(R.id.id_http_post_download_button);
        getSynchronizedView = (Button) view.findViewById(R.id.id_http_get_synchronized_button);
        postSynchronizedView = (Button) view.findViewById(R.id.id_http_post_synchronized_button);

        resultView = (TextView) view.findViewById(R.id.id_result_okhttp_view);
        mImageView = (ImageView) view.findViewById(R.id.id_image);
        initListener();
        return view;
    }

    private void initListener() {
        getView.setOnClickListener(this);
        postView.setOnClickListener(this);
        postJsonView.setOnClickListener(this);
        uploadView.setOnClickListener(this);
        downloadView.setOnClickListener(this);
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
        OkHttpClient okHttpClient = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();//.get()可以不要，Builder的默认构造方法里面就是get请求
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
            case R.id.id_http_post_upload_button:
                uploadImage();
                //TODO
                break;
            case R.id.id_http_post_download_button:
                download();
                //TODO
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

    private void download() {
        if (!PermissionManager.isHasPermission(getActivity(), Permission.Group.STORAGE)) {
            PermissionManager.requestPermission(getActivity(), Permission.Group.STORAGE);
        }

        String imageUrl = "https://picjumbo.com/wp-content/uploads/abstract-free-photo-1570x1047.jpg";
        Request request = new Request.Builder().url(imageUrl).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, response.message());
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                String filePath = "";

                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Log.d(TAG, "get file path#1#");
                        filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    } else {
                        filePath = getActivity().getFilesDir().getAbsolutePath();
                        Log.d(TAG, "get file path#2#");
                    }
                    Log.d(TAG, "filepath = " + filePath);
                    File file = new File(filePath, "abstract-free-photo-1570x1047.jpg");
                    if (!file.exists()) {
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        while ((length = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, length);
                        }
                        fileOutputStream.flush();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                }

            }
        });

    }

    public final static MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/png");
    public final static String UPLOAD_URL = "https://tinypng.com/web/shrink";
    public final static String DEFAULT_PATH = "/storage/emulated/0";


    /**
     * MediaType是什么
     * MediaType在网络协议的消息头里面叫做Content-Type
     * 使用两部分的标识符来确定一个类型
     * 所以我们用的时候其实就是为了表明我们传的东西是什么类型
     * 比如
     * application/json：JSON格式的数据，在RFC 4627中定义
     * application/javascript：JavaScript，在RFC 4329中定义但是不被IE8以及之前的版本支持
     * audio/mp4：MP4音频
     * audio/mpeg：MP3 或者MPEG音频，在RFC 3003中定义
     * image/jpeg：JPEG 和JFIF格式，在RFC 2045 和 RFC 2046中定义
     * image/png：png格式，在 RFC 2083中定义
     * text/html：HTML格式，在RFC 2854中定义
     * text/javascript ：JavaScript在已经废弃的RFC 4329中定义，现在推荐使用“application/javascript”。然而“text/javascript”允许在HTML 4 和5 中使用。并且与“application/javascript”不同，它是可以跨浏览器支持的
     *
     * @param parent 被上传文件的父路径
     * @param child  被上传文件名
     */
    private void upload(String parent, String child) {
        Log.d(TAG, "#begin upload#");
        File file = new File(parent, child);
        Request request = new Request.Builder().url(UPLOAD_URL).post(RequestBody.create(MEDIA_TYPE_IMAGE, file)).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"failure:" + e.getLocalizedMessage());
                Log.d(TAG,"#upload over#");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"response message|" + response.message());
                if(response.isSuccessful()){
                    Log.d(TAG,"upload success,response|" + response.body().string());
                } else {
                    Log.d(TAG,"upload failure,response|" + response.message());
                }
                Log.d(TAG,"#upload over#");
            }
        });


    }

    private void uploadImage() {
        String imageName = "abstract-free-photo-1570x1047.jpg";
        String imagePath = DEFAULT_PATH;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            imagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return;
        }

        upload(imagePath, imageName);
    }
}
