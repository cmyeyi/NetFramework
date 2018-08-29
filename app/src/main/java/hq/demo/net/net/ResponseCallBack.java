package hq.demo.net.net;

import okhttp3.Response;

/**
 * Created by wwxd on 2018/8/29.
 */
public interface ResponseCallBack {
    public void onFailure(Throwable e);

    public void onSuccess(Response response);

}
