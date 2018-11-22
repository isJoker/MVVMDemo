package cn.com.minimvvm.net;

import java.util.concurrent.TimeUnit;

import cn.com.minimvvm.net.download.DownLoadSubscriber;
import cn.com.minimvvm.net.download.ProgressCallBack;
import cn.com.minimvvm.net.interceptor.ProgressInterceptor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by JokerWan on 2018/11/21.
 * Function: 文件下载管理
 */

public class DownLoadManager {

    private static DownLoadManager instance;
    private static Retrofit retrofit;

    private DownLoadManager() {
        buildNetWork();
    }

    public static DownLoadManager getInstance() {
        if (instance == null) {
            instance = new DownLoadManager();
        }
        return instance;
    }

    /**
     * 下载
     *
     * @param downUrl
     * @param callBack
     */
    public void load(String downUrl, final ProgressCallBack callBack) {
        retrofit.create(ApiService.class)
                .download(downUrl)
                //请求网络 在调度者的io线程
                .subscribeOn(Schedulers.io())
                //指定线程保存文件
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callBack.saveFile(responseBody);
                    }
                })
                //在主线程中更新ui
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DownLoadSubscriber<ResponseBody>(callBack));
    }

    private void buildNetWork() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(NetworkUtil.url)
                .build();
    }

    private interface ApiService {
        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url);
    }
}
