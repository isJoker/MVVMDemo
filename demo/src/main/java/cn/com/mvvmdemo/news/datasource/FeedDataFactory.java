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

    public FeedDataFactory(Context context,NewsRepository repository) {
        dataSource = new FeedDataSource(context,repository);
    }

    @Override
    public DataSource create() {
        return dataSource;
    }

    public FeedDataSource getDataSource() {
        return dataSource;
    }
}
