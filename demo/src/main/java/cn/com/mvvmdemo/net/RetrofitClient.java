package cn.com.mvvmdemo.net;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.com.minimvvm.net.cookie.CookieJarImpl;
import cn.com.minimvvm.net.cookie.store.PersistentCookieStore;
import cn.com.minimvvm.net.interceptor.BaseInterceptor;
import cn.com.minimvvm.net.interceptor.CacheInterceptor;
import cn.com.minimvvm.net.interceptor.logging.Level;
import cn.com.minimvvm.net.interceptor.logging.LoggingInterceptor;
import cn.com.minimvvm.utils.HttpsUtils;
import cn.com.minimvvm.utils.LogUtils;
import cn.com.mvvmdemo.BuildConfig;
import cn.com.mvvmdemo.common.MyApp;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JokerWan on 2018/11/21.
 * Function:
 */

public class RetrofitClient {

    /**
     * 超时时间
     */
    private static final int DEFAULT_TIMEOUT = 20;
    /**
     * 缓存大小
     */
    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    private static Retrofit retrofit;

    private File httpCacheDirectory;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(BuildConfig.API_HOST, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {

        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(MyApp.getContext().getCacheDir(), "HttpCache");
        }

        Cache cache = null;
        try {
            cache = new Cache(httpCacheDirectory, CACHE_SIZE);
        } catch (Exception e) {
            LogUtils.e("Could not create http cache", e);
        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(MyApp.getContext())))
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(MyApp.getContext()))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()
                        //是否开启日志打印
                        .loggable(BuildConfig.DEBUG)
                        //打印的等级
                        .setLevel(Level.BASIC)
                        // 打印类型
                        .log(Platform.INFO)
                        // request的Tag
                        .request("Request")
                        // Response的Tag
                        .response("Response")
                        // 添加打印头, 注意 key 和 value 都不能是中文
                        .addHeader("log-header", "I am the log request header.")
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(new ConnectionPool(8, 10, TimeUnit.SECONDS))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }
}

