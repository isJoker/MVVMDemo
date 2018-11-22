package cn.com.mvvmdemo.news.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.com.minimvvm.base.BaseViewModel;
import cn.com.minimvvm.utils.RxUtils;
import cn.com.mvvmdemo.R;
import cn.com.mvvmdemo.net.RetrofitClient;
import cn.com.mvvmdemo.news.api.NewsApi;
import cn.com.mvvmdemo.news.bean.Article;
import cn.com.mvvmdemo.news.bean.Feed;
import cn.com.mvvmdemo.news.datasource.FeedDataFactory;
import cn.com.mvvmdemo.news.model.ItemNews;
import cn.com.mvvmdemo.news.repository.NewsRepository;
import cn.com.mvvmdemo.utils.AppUtils;
import io.reactivex.functions.Consumer;

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
    private LiveData<PagedList<ItemNews>> articleLiveData;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(RetrofitClient.getInstance().create(NewsApi.class));
        init();
    }

    private void init() {
        executor = Executors.newFixedThreadPool(THREAD_NUM);

        FeedDataFactory feedDataFactory = new FeedDataFactory(this);

        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                //当数据为null时是否显示占位
                .setEnablePlaceholders(true)
                //初始化数据时加载的数量，默认为pageSize*3
                .setInitialLoadSizeHint(FIRST_LOAD_SIZE)
                .setPageSize(PAGE_SIZE)
                //预加载的数量，默认为pageSize
                .setPrefetchDistance(PRE_LOAD_PAGE_SIZE)
                .build();

        articleLiveData = new LivePagedListBuilder(feedDataFactory, pagedListConfig)
                .setFetchExecutor(executor)
                .build();
    }

    public void fetchFeed(long page, int pageSize, Consumer<List<ItemNews>> onNext) {
        repository.fetchFeed(page, pageSize)
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))
                .compose(RxUtils.schedulersTransformer())
                .map(o -> convertData((Feed) o))
                .subscribe(onNext, throwable -> showToast(R.string.net_error));
    }

    @NonNull
    private Object convertData(Feed o) {
        Feed feed = o;
        List<ItemNews> itemNews = new ArrayList<>();
        List<Article> articles = feed.getArticles();
        for (Article article : articles) {
            ItemNews news = new ItemNews();

            String author = article.getAuthor() == null || article.getAuthor().isEmpty() ? getApplication().getString(R.string.author_name) : article.getAuthor();
            String titleString = String.format(getApplication().getString(R.string.item_title), author, article.getTitle());
            SpannableString spannableString = new SpannableString(titleString);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getApplication().getApplicationContext(), R.color.secondary_text)),
                    titleString.lastIndexOf(author) + author.length() + 1, titleString.lastIndexOf(article.getTitle()) - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            news.setTitle(titleString);
            news.setTime(String.format(getApplication().getString(R.string.item_date), AppUtils.getDate(article.getPublishedAt()), AppUtils.getTime(article.getPublishedAt())));
            news.setDesc(article.getDescription());
            news.setImgUrl(article.getUrlToImage());
            news.setTotalResultsCount(feed.getTotalResults());
            news.setId(feed.getId());

            itemNews.add(news);
        }
        return itemNews;
    }

    public LiveData<PagedList<ItemNews>> getArticleLiveData() {
        return articleLiveData;
    }


}
