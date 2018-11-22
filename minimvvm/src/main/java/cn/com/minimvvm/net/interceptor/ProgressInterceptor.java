package cn.com.minimvvm.net.interceptor;

import java.io.IOException;

import cn.com.minimvvm.net.download.ProgressResponseBody;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 下载进度拦截器
 */
public class ProgressInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body()))
                .build();
    }
}
