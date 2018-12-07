package cn.com.mvvmdemo.news.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import cn.com.minimvvm.utils.LogUtils;
import cn.com.minimvvm.utils.RxUtils;
import cn.com.minimvvm.utils.ToastUtil;
import cn.com.mvvmdemo.R;
import cn.com.mvvmdemo.news.bean.Article;
import cn.com.mvvmdemo.news.bean.Feed;
import cn.com.mvvmdemo.news.model.ItemNews;
import cn.com.mvvmdemo.news.repository.NewsRepository;
import cn.com.mvvmdemo.utils.AppUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:   PageKeyedDataSource<Long, ItemNews>
 *         Long 加载数据的条件 ItemNews 加载数据的实体类
 */

public class FeedDataSource extends PageKeyedDataSource<Long, ItemNews> {

    private static final String TAG = FeedDataSource.class.getSimpleName();

    private final NewsRepository repository;
    private Context mContext;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FeedDataSource(Context context,NewsRepository repository) {
        mContext = context;
        this.repository = repository;
    }

    /**
     * 第一次加载
     *
     * @param params  params.placeholdersEnabled（当数据为null时是否显示占位）   params.requestedLoadSize(每页的item的数量)
     * @param callback 加载完成的回调，通知UI刷新数据
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, ItemNews> callback) {
        LogUtils.d(TAG, "loadInitial: -------params.requestedLoadSize=>" + params.requestedLoadSize);
        fetchFeed(1, params.requestedLoadSize, list -> {
            //第一页数据，前面没有页数。所以 previousPageKey 传 null , nextPageKey表示下一页的pageNo
            callback.onResult(list, null, 2L);
        });
    }

    /**
     * 加载前一页数据
     *
     * @param params  params.key对应callback.onResult()需要传入的第二个参数previousPageKey
     * @param callback
     */
    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ItemNews> callback) {

    }

    /**
     * 加载后一页数据
     *
     * @param params params.key(params.key对应callback.onResult()需要传入的第三个参数nextPageKey) 页数
     *               params.requestedLoadSize 每页加载的item条数
     * @param callback
     */
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ItemNews> callback) {

        LogUtils.d(TAG, "loadAfter: ---params.key=>" + params.key + "-------params.requestedLoadSize=>" + params.requestedLoadSize);
        fetchFeed(params.key, params.requestedLoadSize, list -> {
            // 加载完毕所有的item nextKey=null ,否则则 nextKey = params.key + 1
            Long nextKey = (params.key * params.requestedLoadSize >= list.get(0).getTotalResultsCount()) ? null : params.key + 1;
            callback.onResult(list, nextKey);
        });
    }

    public void fetchFeed(long page, int pageSize, Consumer<List<ItemNews>> onNext) {
        Disposable disposable = repository.fetchFeed(page, pageSize)
                .compose(RxUtils.schedulersTransformer())
                .map(o -> convertData((Feed) o))
                .subscribe(onNext, throwable -> ToastUtil.show(mContext, R.string.net_error));
        compositeDisposable.add(disposable);
    }

    @NonNull
    private List<ItemNews> convertData(Feed o) {
        Feed feed = o;
        List<ItemNews> itemNews = new ArrayList<>();
        List<Article> articles = feed.getArticles();
        for (Article article : articles) {
            ItemNews news = new ItemNews();

            String author = article.getAuthor() == null || article.getAuthor().isEmpty() ? mContext.getString(R.string.author_name) : article.getAuthor();
            String titleString = String.format(mContext.getString(R.string.item_title), author, article.getTitle());
            SpannableString spannableString = new SpannableString(titleString);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext.getApplicationContext(), R.color.secondary_text)),
                    titleString.lastIndexOf(author) + author.length() + 1, titleString.lastIndexOf(article.getTitle()) - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            news.setTitle(titleString);
            news.setTime(String.format(mContext.getString(R.string.item_date), AppUtils.getDate(article.getPublishedAt()), AppUtils.getTime(article.getPublishedAt())));
            news.setDesc(article.getDescription());
            news.setImgUrl(article.getUrlToImage());
            news.setTotalResultsCount(feed.getTotalResults());
            news.setId(feed.getId());

            itemNews.add(news);
        }
        return itemNews;
    }

    public void onVMClear(){
        compositeDisposable.clear();
    }

}
