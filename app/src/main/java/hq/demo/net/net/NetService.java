package hq.demo.net.net;


import hq.demo.net.model.WeatherBeans;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetService {

    @GET("?app=weather.future&weaid=1&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
    Call<WeatherBeans> requestWeatherBeans();

    @POST("/")
    @FormUrlEncoded
    Call<WeatherBeans> requestWeatherBeans(
            @Field("app") String app,
            @Field("weaid") String weaid,
            @Field("appkey") String appkey,
            @Field("sign") String sign,
            @Field("format") String format);


}
