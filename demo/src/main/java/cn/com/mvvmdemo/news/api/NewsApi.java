package cn.com.mvvmdemo.news.api;

import java.util.HashMap;

import cn.com.mvvmdemo.news.bean.Feed;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public interface NewsApi {

    @GET("/v2/everything")
    Observable<Feed> fetchFeed(@QueryMap HashMap<String,Object> params);
}
