package hq.demo.net.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hq.demo.net.R;
import hq.demo.net.model.WeatherBeans;
import hq.demo.net.net.NetService;
import hq.demo.net.net.OKHttpManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RetrofitFragment";
    private TextView resultView;
    private Button getView;
    private Button postView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrofit, container, false);
        getView = (Button) view.findViewById(R.id.id_retrofit_get_button);
        postView = (Button) view.findViewById(R.id.id_retrofit_post_button);
        resultView = (TextView) view.findViewById(R.id.id_result_retrofit_view);

        initListener();
        return view;
    }


    private void initListener() {
        getView.setOnClickListener(this);
        postView.setOnClickListener(this);
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
            case R.id.id_retrofit_get_button:
                doGet();
                break;
            case R.id.id_retrofit_post_button:
                doPost();
                break;
        }
    }

    private void doGet() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<WeatherBeans> call = netService.requestWeatherBeans();
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {


                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("cityName", bean.getCitynm());
                        Log.d("days", bean.getDays());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    private void doPost() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<WeatherBeans> call = netService.requestWeatherBeans("weather.future","1","10003","b59bc3ef6191eb9f747dd4e83c99f2a4","json");
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {


                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("cityName", bean.getCitynm());
                        Log.d("days", bean.getDays());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }
}
