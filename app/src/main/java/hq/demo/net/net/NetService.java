package hq.demo.net.net;


import java.util.Map;

import hq.demo.net.model.Movies;
import hq.demo.net.model.WeatherBeans;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface NetService {


    @GET
    Call<Movies> requestMovies(@Url String url);


    @GET("?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
    Call<WeatherBeans> requestWeatherBeans();

    @GET("?")
    Call<WeatherBeans> requestWeather(@Query("app") String app, @Query("weaid") int weaid, @Query("appkey") int appkey, @Query("sign") String sign, @Query("format") String format);


    @GET("?")
    Call<WeatherBeans> requestWeather(@QueryMap Map<String, String> paramas);


    @POST("/")
    @FormUrlEncoded
    Call<WeatherBeans> requestWeatherBeans(
            @Field("app") String app,
            @Field("weaid") String weaid,
            @Field("appkey") String appkey,
            @Field("sign") String sign,
            @Field("format") String format);

    @POST("/")
    @FormUrlEncoded
    Call<WeatherBeans> requestWeatherBeans(@FieldMap Map<String, String> fields);


    @POST("/")
    Call<WeatherBeans> requestWeatherBeans(@Body RequestParams parama);


    class RequestParams {
        public String app;
        public int weaid;
        public int appkey;
        public String sign;
        public String format;
    }

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=360000"})
    @HTTP(method = "GET", path = "?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json", hasBody = false)
    Call<WeatherBeans> requestWeatherBeansByHeaders();

//    @GET("login")
//    Call<UserInfo> login(@Header("Authorization") String authorization);


    @HTTP(method = "GET", path = "?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json", hasBody = false)
    Call<WeatherBeans> requestWeatherBeansByHttpGet();

    @HTTP(method = "POST", path = "?", hasBody = true)
    @FormUrlEncoded
    Call<WeatherBeans> requestWeatherBeansByHttpPost(@FieldMap Map<String, String> paramas);

    /**
     * 上传图文
     * @param description
     * @param file
     * @return
     */
    @Multipart
    @POST("web/shrink")
    Call<ResponseBody> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    /**
     * 上传一张图片
     * @param file
     * @return
     */
    @Multipart
    @POST("web/shrink")
    Call<ResponseBody> uploadFile(@Part() RequestBody file);

    /**
     * 上传一张图片 另一种写法
     * @param file
     * @return
     */
    @Multipart
    @POST()
    Call<ResponseBody> uploadFile(@Url String url, @Part() RequestBody file);

    /**
     * 上传数量确定的多张图片
     * @param description
     * @param img1
     * @param img2
     * @param img3
     * @return
     */
    @POST("web/shrink")
    Call<ResponseBody> uploadFiles(@Part("filename") String description,
                                   @Part("img\"; name=\"img1.png") RequestBody img1,
                                   @Part("img\"; name=\"img2.png") RequestBody img2,
                                   @Part("img\"; name=\"img3.png") RequestBody img3);

    /**
     * 上报数量不定的多张图片 版本1
     * @param params
     * @return
     */
    @Multipart
    @POST("web/shrink")
    Call<ResponseBody> uploadFile(@PartMap Map<String, RequestBody> params);

    /**
     * 上报数量不定的多张图片 版本2
     * @param url
     * @param maps
     * @return
     */
    @Multipart
    @POST()
    Call<ResponseBody> uploadFiles( @Url String url, @PartMap() Map<String, RequestBody> maps);

    @OPTIONS("/")
    Call<ResponseBody> options();

    @GET("back_pic/03/70/72/5257b6c12d89875.jpg!r850/fw/{id}")
    Call<ResponseBody> getImage(@Path("id") int id);

    @GET
    @Streaming
    Call<ResponseBody> downloadImage(@Url String url);



    @GET("?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
    Observable<WeatherBeans> requestWeatherBeansRX();

    @POST("/")
    Observable<WeatherBeans> requestWeatherBeansRX(@Body RequestParams parama);

}
