package hq.demo.net.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wwxd on 2018/8/29.
 */

public class OKHttpManager {

    public static final String TAG = "OKHttpManager";
    public static final long DEFAULT_TIMEOUT = 60; //默认的超时时间,单位：秒
    private Handler mHandler;
    private OkHttpClient okHttpClient;

    private static volatile OKHttpManager mInstance;//注意这里的使用volatile修饰变量，
    private ResponseCallBack callBack;

    private OKHttpManager() {
        mHandler = new Handler(Looper.getMainLooper());
        okHttpClient = settings();
    }

    /**
     * 使用静态内部类的机制，构造单例
     *
     * @return OKHttpManager
     */
    public static OKHttpManager getInstance() {
        return OKHttpManagerHolder.okHttpManager;
    }

    public static class OKHttpManagerHolder {
        private static OKHttpManager okHttpManager = new OKHttpManager();
    }


    /**
     * 设置超时
     */
    private OkHttpClient settings() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //注意这里本来是可以采用链式调用的
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return builder.build();
    }

    public void doGet(String url, ResponseCallBack callBack) {
        this.callBack = callBack;
        Request request = new Request.Builder().url(url).get().build();
        execute(request);
    }

    public void doPost(String url, RequestBody body, ResponseCallBack callBack) {
        this.callBack = callBack;

        okhttp3.Request request = new okhttp3.Request.Builder()
                .tag(url.hashCode())//通过访问url的hashcode打标签
                .url(url)
                .post(body)
                .build();

        execute(request);

    }

    private void execute(final Request request) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //注意这里的string()方法只能被调用一次，否者后面会报错，因为流已经被关闭，所以这里注释掉日志
//                Log.d(TAG,response.body().string());
                callBack.onSuccess(response);
            }
        });

    }

    public Handler getHandler() {
        return mHandler;
    }

    public void runOnUIThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void cancelWithTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    public void cancelAll(OkHttpClient client) {
        if (client == null) {
            return;
        }

        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }

        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }

    }
}
