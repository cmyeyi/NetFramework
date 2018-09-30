package hq.demo.net.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import hq.demo.net.R;
import hq.demo.net.model.WeatherBeans;
import hq.demo.net.net.NetService;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.SchedulerWhen;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RxJavaFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NewOkHttpFragment";
    private TextView resultView;
    private Button getView;
    private Button postView;
    private Button fromView;
    private Button createView;
    private Button flowableView;
    private Button completableView;
    private Button maybeView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rxjava, container, false);
        getView = (Button) view.findViewById(R.id.id_rx_get_button);
        postView = (Button) view.findViewById(R.id.id_rx_post_button);
        fromView = (Button) view.findViewById(R.id.id_rx_from_button);
        createView = (Button) view.findViewById(R.id.id_rx_create_button);
        flowableView = (Button) view.findViewById(R.id.id_rx_flowable_button);
        completableView = (Button) view.findViewById(R.id.id_rx_completable_button);
        maybeView = (Button) view.findViewById(R.id.id_rx_maybe_button);
        resultView = (TextView) view.findViewById(R.id.id_result_rx_view);
        initListener();
        return view;
    }

    private void initListener() {
        getView.setOnClickListener(this);
        postView.setOnClickListener(this);
        fromView.setOnClickListener(this);
        createView.setOnClickListener(this);
        flowableView.setOnClickListener(this);
        completableView.setOnClickListener(this);
        maybeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_rx_get_button:
                doGetWithRx();
                break;
            case R.id.id_rx_post_button:
                doPostWithRx();
                break;
            case R.id.id_rx_from_button:
                doFrom();
                break;
            case R.id.id_rx_create_button:
                doCreate(1);
                doConsumer();
                break;
            case R.id.id_rx_flowable_button:
//                doJust("Hello");
                doJust("hello", "world");
            case R.id.id_rx_completable_button:
                doCompletable();
                break;
            case R.id.id_rx_maybe_button:
                doMaybe();
                break;
        }
    }

    private void doGetWithRx() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);
        Observable<WeatherBeans> observable = netService.requestWeatherBeansRX();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherBeans>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe run");
                    }

                    @Override
                    public void onNext(WeatherBeans beans) {
                        StringBuilder sb = new StringBuilder();
                        if (beans != null) {
                            for (WeatherBeans.ResultBean bean : beans.getResult()) {
                                Log.d(TAG, "cityName:" + bean.getCitynm());
                                sb.append(bean.getCitynm()).append(", " + bean.getDays() + ", " + bean.getTemperature() + "\n");
                            }
                            resultView.setText(sb.toString());

                        } else {
                            Log.d(TAG, "OnNext beans is null");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "OnError run");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete run");
                    }
                });
    }

    private void doPostWithRx() {
        Log.d(TAG, "doPostWithRx RxJava");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.k780.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        NetService netService = retrofit.create(NetService.class);

        //创建以@Body注解post请求参数
        NetService.RequestParams params = new NetService.RequestParams();
        params.app = "weather.future";
        params.weaid = 1;
        params.appkey = 10003;
        params.sign = "b59bc3ef6191eb9f747dd4e83c99f2a4";
        params.format = "json";

        Observable<WeatherBeans> observable = netService.requestWeatherBeansRX(params);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherBeans>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe run");
                    }

                    @Override
                    public void onNext(WeatherBeans beans) {
                        if (beans != null && beans.getResult() != null) {
                            for (WeatherBeans.ResultBean bean : beans.getResult()) {
                                Log.d(TAG, "cityName:" + bean.getCitynm());
                            }

                        } else {
                            Log.d(TAG, "doOnNext accept beans is null");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError message:" + e.getMessage());
                        Log.d(TAG, "onError localizedMessage:" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "doOnComplete run");
                    }
                });
    }

    private void doFrom() {
        String[] items = {"item1", "item2", "item3"};
        Observable.fromArray(items).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    private void doCreate() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "Observable emit 1" + "\n");
                e.onNext(1);

                Log.d(TAG, "Observable emit 2" + "\n");
                e.onNext(2);

                Log.d(TAG, "Observable emit 3" + "\n");
                e.onNext(3);

                e.onComplete();

                Log.d(TAG, "Observable emit 4" + "\n");
                e.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe : " + d.isDisposed() + "\n");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext : value : " + integer + "\n");
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    Log.d(TAG, "onNext : isDisposable : " + mDisposable.isDisposed() + "\n");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError : value : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete" + "\n");
            }
        });

    }


    Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
            Log.d(TAG, "Observable emitter 100");
            e.onNext(100);

            Log.d(TAG, "Observable emitter 200");
            e.onNext(200);

            Log.d(TAG, "Observable emitter 300");
            e.onNext(300);

            e.onComplete();

            Log.d(TAG, "Observable emitter 400");
            e.onNext(40);
        }
    });


    Observer observer = new Observer<Integer>() {
        private Disposable mDisposable;

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            Log.d(TAG, "onSubscribe: " + d.isDisposed());
            mDisposable = d;
        }

        @Override
        public void onNext(@NonNull Integer integer) {
            Log.d(TAG, "onNext , value: " + integer);
            if (integer == 200) {
                mDisposable.dispose();// RxJava2中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                Log.d(TAG, "onNext , isDisposable: " + mDisposable.isDisposed());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(TAG, "onError , value: " + e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete");
        }
    };

    private void doCreate(int value) {
        observable.subscribe(observer);
    }

    private void doConsumer() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

                        Integer result = 100;//数据库操作

                        e.onNext(result);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept，integer = " + integer);
                    }
                });
    }

    private void doJust(String s) {
        Flowable.just(s).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    private void doJust(String t1, String t2) {
        Flowable.just(t1, t2).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });

        Flowable.just(t1, t2).subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void doCompletable(String s) {
        Disposable d = Completable.complete()
                .delay(3, TimeUnit.SECONDS, Schedulers.io())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onStart() {
                        System.out.println("Started");
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });


        d.dispose();

    }

    private void doCompletable() {
        Completable.complete()
                .delay(3, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new CompletableObserver() {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }


                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });

    }


    private void doMaybe(String s) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Disposable d = Maybe.just("Hello World")
                        .delay(3, TimeUnit.SECONDS, Schedulers.io())
                        .subscribeWith(new DisposableMaybeObserver<String>() {
                            @Override
                            public void onStart() {
                                System.out.println("Started");
                            }

                            @Override
                            public void onSuccess(String value) {
                                System.out.println("Success: " + value);
                            }

                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                System.out.println("Done!");
                            }
                        });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                d.dispose();
            }
        }).start();

    }

    private void doMaybe() {
        Maybe.just("Hello World")
                .delay(3, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("Started");
                    }

                    @Override
                    public void onSuccess(String value) {
                        System.out.println("Success: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });

    }

}
