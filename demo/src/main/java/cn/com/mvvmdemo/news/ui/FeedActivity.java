package cn.com.mvvmdemo.news.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import cn.com.minimvvm.base.BaseActivity;
import cn.com.mvvmdemo.BR;
import cn.com.mvvmdemo.R;
import cn.com.mvvmdemo.databinding.ActivityFeedBinding;
import cn.com.mvvmdemo.news.ui.adapter.FeedListAdapter;
import cn.com.mvvmdemo.news.viewmodel.NewsViewModel;

public class FeedActivity extends BaseActivity<ActivityFeedBinding, NewsViewModel> {

    private FeedListAdapter adapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_feed;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public void initData() {

        binding.listFeed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new FeedListAdapter();
        binding.listFeed.setAdapter(adapter);

        viewModel.getArticleLiveData().observe(this, pagedList -> adapter.submitList(pagedList));
    }
}
