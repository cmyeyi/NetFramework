package hq.demo.net.fragments;

import android.os.Bundle;
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
import hq.demo.net.net.OKHttpManager;
import hq.demo.net.net.ResponseCallBack;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NewOkHttpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NewOkHttpFragment";
    private TextView resultView;
    private Button getView;
    private Button postView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_okhttp_new, container, false);
        getView = (Button) view.findViewById(R.id.id_http_get_button);
        postView = (Button) view.findViewById(R.id.id_http_post_button);
        resultView = (TextView) view.findViewById(R.id.id_result_okhttp_view);
        initListener();
        return view;
    }

    private void initListener() {
        getView.setOnClickListener(this);
        postView.setOnClickListener(this);
    }


    /**
     * 测试okhttp的get方法
     */
    private void testOkhttpGet() {
        String url = "http://api.k780.com/?app=weather.history";
        OKHttpManager.getInstance().doGet(url, new ResponseCallBack() {
            @Override
            public void onFailure(Throwable e) {
                showResult(e.getMessage());
            }

            @Override
            public void onSuccess(Response response) {
                try {
                    showResult(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 测试okhttp的post方法
     */
    private void testOkhttpPost() {
        String url = "http://api.k780.com/?app=weather.history";
        RequestBody body = new FormBody.Builder()
                .add("weaid", "1")
                .add("date", "2018-08-13")
                .add("appkey", "10003")
                .add("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4")
                .add("format", "json")
                .build();


        OKHttpManager.getInstance().doPost(url, body, new ResponseCallBack() {
            @Override
            public void onFailure(Throwable e) {
                showResult(e.getMessage());
            }

            @Override
            public void onSuccess(Response response) {
                try {
                    showResult(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void showResult(final String content) {
        OKHttpManager.getInstance().runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (resultView != null) {
                    if (TextUtils.isEmpty(content)) {
                        resultView.setText("null");
                    } else {
                        resultView.setText(content);
                    }
                }
            }
        });

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
        }
    }


}
