package cn.com.mvvmdemo.news.repository;

import java.util.HashMap;

import cn.com.mvvmdemo.news.api.NewsApi;
import cn.com.mvvmdemo.news.bean.Feed;
import io.reactivex.Observable;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class NewsRepository {

    private final NewsApi api;
    private static final String QUERY_VALUE = "movies";
    private static final String API_KEY_VALUE = "079dac74a5f94ebdb990ecf61c8854b7";
    private static final String API_KEY = "apiKey";
    private static final String QUERY = "q";
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";

    public NewsRepository(NewsApi api) {
        this.api = api;
    }

    public Observable<Feed> fetchFeed(long page,int pageSize){
        HashMap<String,Object> params = new HashMap<>(4);
        params.put(QUERY,QUERY_VALUE);
        params.put(API_KEY,API_KEY_VALUE);
        params.put(PAGE,page);
        params.put(PAGE_SIZE,pageSize);
        return api.fetchFeed(params);
    }
}
