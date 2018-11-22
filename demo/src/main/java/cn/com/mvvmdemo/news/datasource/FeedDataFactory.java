package cn.com.mvvmdemo.news.datasource;

import android.arch.paging.DataSource;

import cn.com.mvvmdemo.news.viewmodel.NewsViewModel;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class FeedDataFactory extends DataSource.Factory {

    private FeedDataSource feedDataSource;
    private NewsViewModel viewModel;

    public FeedDataFactory(NewsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public DataSource create() {
        feedDataSource = new FeedDataSource(viewModel);
        return feedDataSource;
    }
}
