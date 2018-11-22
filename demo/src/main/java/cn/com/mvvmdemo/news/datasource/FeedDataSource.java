package cn.com.mvvmdemo.news.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import cn.com.minimvvm.utils.LogUtils;
import cn.com.mvvmdemo.news.model.ItemNews;
import cn.com.mvvmdemo.news.viewmodel.NewsViewModel;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class FeedDataSource extends PageKeyedDataSource<Long, ItemNews> {

    private static final String TAG = FeedDataSource.class.getSimpleName();

    private final NewsViewModel viewModel;

    public FeedDataSource(NewsViewModel viewModel) {
        this.viewModel = viewModel;
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
        viewModel.fetchFeed(1, params.requestedLoadSize, list -> {
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
        viewModel.fetchFeed(params.key, params.requestedLoadSize, list -> {
            // 加载完毕所有的item nextKey=null ,否则则 nextKey = params.key + 1
            Long nextKey = (params.key * params.requestedLoadSize >= list.get(0).getTotalResultsCount()) ? null : params.key + 1;
            callback.onResult(list, nextKey);
        });
    }
}
