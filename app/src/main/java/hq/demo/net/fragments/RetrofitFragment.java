package hq.demo.net.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
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

import com.tech.aile.permission.Permission;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hq.demo.net.PermissionManager;
import hq.demo.net.R;
import hq.demo.net.model.Movies;
import hq.demo.net.model.WeatherBeans;
import hq.demo.net.net.NetService;
import hq.demo.net.net.OKHttpManager;
import hq.demo.net.utils.FileHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Part;


public class RetrofitFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RetrofitFragment";
    private TextView resultView;
    private Button getView;
    private Button postView;
    private Button putView;
    private Button httpView;
    private Button httpPostView;
    private Button uploadView;
    private Button optionsView;
    private Button downloadView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrofit, container, false);
        getView = (Button) view.findViewById(R.id.id_retrofit_get_button);
        postView = (Button) view.findViewById(R.id.id_retrofit_post_button);
        putView = (Button) view.findViewById(R.id.id_retrofit_put_button);
        httpView = (Button) view.findViewById(R.id.id_retrofit_http_get_button);
        httpPostView = (Button) view.findViewById(R.id.id_retrofit_http_post_button);
        uploadView = (Button) view.findViewById(R.id.id_retrofit_upload_button);
        optionsView = (Button) view.findViewById(R.id.id_retrofit_options_button);
        downloadView = (Button) view.findViewById(R.id.id_retrofit_download_button);
        resultView = (TextView) view.findViewById(R.id.id_result_retrofit_view);

        initListener();
        return view;
    }


    private void initListener() {
        getView.setOnClickListener(this);
        postView.setOnClickListener(this);
        putView.setOnClickListener(this);
        httpView.setOnClickListener(this);
        httpPostView.setOnClickListener(this);
        uploadView.setOnClickListener(this);
        optionsView.setOnClickListener(this);
        downloadView.setOnClickListener(this);
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
//                doGet1();
//                getRequestMovieList();
                doGetImage();
                break;
            case R.id.id_retrofit_post_button:
//                doPost1();
                doPostWithBody();
            case R.id.id_retrofit_put_button:
                doPut();
                break;
            case R.id.id_retrofit_http_get_button:
                doHttpGet();
                break;
            case R.id.id_retrofit_http_post_button:
                doHttpPost();
                break;
            case R.id.id_retrofit_upload_button:
                doUpload();
                break;
            case R.id.id_retrofit_options_button:
                doOptions();
                break;
            case R.id.id_retrofit_download_button:
                downloadImage();
                break;
        }
    }

    public final static String requestUrl = "https://api.douban.com/v2/movie/top250?start=0&count=10";

    private void getRequestMovieList() {
        Log.d("---------->", "doGet0");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<Movies> call = netService.requestMovies(requestUrl);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    Movies beans = response.body();
                    String title = beans.getTitle();
                    Log.d("##########title", "" + title);
                    List<Movies.Movie> movies = beans.getSubjects();
                    for (Movies.Movie movie : movies) {
                        String name = movie.getTitle();
                        Log.d("##########name", "" + name);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
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

    private void doGetImage() {
        Log.d("#####step", "#doGetImage#");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://bpic.588ku.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<ResponseBody> call = netService.getImage(800);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("#####step", "#isSuccessful#");
                } else {
                    Log.d("#####step", "#Failure#");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("#####step", "#onFailure#");
            }
        });
    }


    private void doGet1() {
        Log.d("#####step", "#doGet1#");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<WeatherBeans> call = netService.requestWeather("weather.future", 1, 10003, "b59bc3ef6191eb9f747dd4e83c99f2a4", "json");
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {

                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("temperature", bean.getDays() + "|" + bean.getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    private void doGet2() {
        Log.d("#####step", "#doGet2#");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Map paramas = new HashMap<>();
        paramas.put("app", "weather.future");
        paramas.put("weaid", "1");
        paramas.put("appkey", "10003");
        paramas.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        paramas.put("format", "json");

        Call<WeatherBeans> call = netService.requestWeather(paramas);
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {

                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("temperature", bean.getDays() + "|" + bean.getTemperature());
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
        Call<WeatherBeans> call = netService.requestWeatherBeans("weather.future", "1", "10003", "b59bc3ef6191eb9f747dd4e83c99f2a4", "json");
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

    private void doPost1() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);

        Map paramas = new HashMap<>();
        paramas.put("app", "weather.future");
        paramas.put("weaid", "1");
        paramas.put("appkey", "10003");
        paramas.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        paramas.put("format", "json");

        Call<WeatherBeans> call = netService.requestWeatherBeans(paramas);
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {


                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("weather：", bean.getCitynm() + "|" + bean.getDays() + "|" + bean.getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    private void doPostWithBody() {
        Log.d("##########", "doPostWithBody");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);

        //创建以@Body注解post请求参数
        NetService.RequestParams params = new NetService.RequestParams();
        params.app = "weather.future";
        params.weaid = 1;
        params.appkey = 10003;
        params.sign = "b59bc3ef6191eb9f747dd4e83c99f2a4";
        params.format = "json";

        Call<WeatherBeans> call = netService.requestWeatherBeans(params);
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {
                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("weather：", bean.getCitynm() + "|" + bean.getDays() + "|" + bean.getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    private void doPut() {
        Log.d("##########", "doPut");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);

        //创建以@Body注解post请求参数
        NetService.RequestParams params = new NetService.RequestParams();
        params.app = "weather.future";
        params.weaid = 1;
        params.appkey = 10003;
        params.sign = "b59bc3ef6191eb9f747dd4e83c99f2a4";
        params.format = "json";

        Call<WeatherBeans> call = netService.requestWeatherBeans(params);
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {
                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("weather：", bean.getCitynm() + "|" + bean.getDays() + "|" + bean.getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    private void doHttpGet() {
        Log.d("##########", "doHttpGet");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);


        Call<WeatherBeans> call = netService.requestWeatherBeansByHttpGet();
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {
                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("weather：", bean.getCitynm() + "|" + bean.getDays() + "|" + bean.getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    private void doHttpPost() {
        Log.d("##########", "doHttpPost");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);

        Map paramas = new HashMap<>();
        paramas.put("app", "weather.future");
        paramas.put("weaid", "1");
        paramas.put("appkey", "10003");
        paramas.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        paramas.put("format", "json");

        Call<WeatherBeans> call = netService.requestWeatherBeansByHttpPost(paramas);
        call.enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {
                if (response.isSuccessful()) {
                    WeatherBeans beans = response.body();
                    for (WeatherBeans.ResultBean bean : beans.getResult()) {
                        Log.d("weather：", bean.getCitynm() + "|" + bean.getDays() + "|" + bean.getTemperature());
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }


    private void doUpload() {
        Log.d("---------->", "doUpload");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tinypng.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);

        File file = getFile();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和服务端约定好Key，这里的part name是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        // 添加上传文件描述
        String descriptionString = "文件描述:这是一张照片";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = netService.uploadFile(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody responseBody = response.body();
                        Log.d("doUpload responseBody", responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("doUpload responseBody", "failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("doUpload responseBody", "onFailure");
            }
        });
    }

    private void doOptions() {
        Log.d("---------->", "doOptions");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tinypng.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<ResponseBody> call = netService.options();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody responseBody = response.body();
                        Log.d("responseBody.string", responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("responseBody.string", "failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void downloadImage() {
        checkPermission();

        final String imageUrl = "http://img.7160.com/uploads/allimg/180802/12-1PP2143259-52.jpg";
        Log.d("---------->", "downloadImage");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picjumbo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Call<ResponseBody> call = netService.downloadImage(imageUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                String rootPath = FileHelper.getPicSavePath(getContext());
                String fileName = FileHelper.getFileNameFromUrl(imageUrl);
                final File file = new File(rootPath, fileName);
                Log.d(TAG, "image load save path" + file.getPath());

                saveFile(response.body().byteStream(), file);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean writtenToDisk = writeResponseBodyToDisk(response.body(), file.getPath());
                        if (writtenToDisk) {
                            Log.d(TAG, "download success");
                        } else {
                            Log.d(TAG, "download failure");
                        }
                    }
                }).start();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    /**
     * 存储retrofit下载的文件
     *
     * @param body
     * @param filePath
     * @return
     */
    public boolean writeResponseBodyToDisk(ResponseBody body, String filePath) {
        Log.e("securities", "writeResponseBodyToDisk body" + body.contentLength());
        try {
            File futureStudioIconFile = new File(filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[1024];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                Log.e("securities", "writeResponseBodyToDisk fail e" + e.toString());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.e("securities", "writeResponseBodyToDisk fail e" + e.toString());
            return false;
        }
    }

    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    private void saveFile(InputStream in, File target) {
        Log.e(TAG, "target：" + target.getPath());
        try {
            BufferedSink sink = Okio.buffer(Okio.sink(target));
            sink.writeAll(Okio.source(in));
            sink.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanPhoto(target);
        }
        if (!target.exists()) {
        }
    }

    private File getFile() {
        String imagePath = "/storage/emulated/0";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            imagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
        return new File(imagePath, System.currentTimeMillis() + ".jpg");
    }

    private void checkPermission() {
        if (!PermissionManager.isHasPermission(getActivity(), Permission.Group.STORAGE)) {
            PermissionManager.requestPermission(getActivity(), Permission.Group.STORAGE);
        }
    }


}
