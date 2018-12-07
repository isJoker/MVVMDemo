package cn.com.mvvmdemo.news.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.com.minimvvm.base.BaseViewModel;
import cn.com.mvvmdemo.net.RetrofitClient;
import cn.com.mvvmdemo.news.api.NewsApi;
import cn.com.mvvmdemo.news.datasource.FeedDataFactory;
import cn.com.mvvmdemo.news.model.ItemNews;
import cn.com.mvvmdemo.news.repository.NewsRepository;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class NewsViewModel extends BaseViewModel {

    private final NewsRepository repository;
    private Executor executor;
    /**
     * 后续加载的PageSize
     */
    private static final int PAGE_SIZE = 20;
    /**
     * 初始化数据时加载的数量
     */
    private static final int FIRST_LOAD_SIZE = 10;
    /**
     * 预加载的数量
     */
    private static final int PRE_LOAD_PAGE_SIZE = 15;
    /**
     * 加载数据线程池的数量
     */
    private static final int THREAD_NUM = 5;
    /**
     * 列表的数据
     */
    private LiveData<PagedList<ItemNews>> articleLiveData;
    private FeedDataFactory feedDataFactory;

    //支持rxjava
//    private Observable<PagedList<ItemNews>> articleObservable;


    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(RetrofitClient.getInstance().create(NewsApi.class));
        init();
    }

    private void init() {
        executor = Executors.newFixedThreadPool(THREAD_NUM);

        feedDataFactory = new FeedDataFactory(getApplication(), repository);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                //当数据为null时是否显示占位
                .setEnablePlaceholders(false)
                //初始化数据时加载的数量，默认为pageSize*3
                .setInitialLoadSizeHint(FIRST_LOAD_SIZE)
                .setPageSize(PAGE_SIZE)
                //预加载的数量，默认为pageSize
                .setPrefetchDistance(PRE_LOAD_PAGE_SIZE)
                .build();

        articleLiveData = new LivePagedListBuilder(feedDataFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build();

        //支持rxjava
        /*articleObservable = new RxPagedListBuilder(goodsDataFactory, pagedListConfig)
                .setFetchScheduler(new IoScheduler())
                .buildObservable();*/
    }


    public LiveData<PagedList<ItemNews>> getArticleLiveData() {
        return articleLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //页面销毁移除disposable
        feedDataFactory.getDataSource().onVMClear();
    }

    //支持rxjava
    /*public void getArticleList(Consumer<PagedList<ItemNews>> consumer) {
        articleObservable.compose(RxUtils.bindToLifecycle(getLifecycleProvider())).subscribe(consumer);
    }*/
}
