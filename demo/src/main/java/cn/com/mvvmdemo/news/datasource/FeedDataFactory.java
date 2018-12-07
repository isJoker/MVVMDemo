package cn.com.mvvmdemo.news.datasource;

import android.arch.paging.DataSource;
import android.content.Context;

import cn.com.mvvmdemo.news.repository.NewsRepository;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class FeedDataFactory extends DataSource.Factory {

    private FeedDataSource dataSource;
    private Context context;
    private NewsRepository repository;

    public FeedDataFactory(Context context,NewsRepository repository) {
        this.context = context;
        this.repository = repository;
    }

    @Override
    public DataSource create() {
        dataSource = new FeedDataSource(context,repository);
        return dataSource;
    }

    public FeedDataSource getDataSource() {
        return dataSource;
    }
}
