package cn.com.mvvmdemo.news.ui.adapter;

import android.arch.paging.PagedListAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.com.mvvmdemo.R;
import cn.com.mvvmdemo.databinding.ItemFeedBinding;
import cn.com.mvvmdemo.news.model.ItemNews;

/**
 * Created by JokerWan on 2018/11/22.
 * Function:
 */

public class FeedListAdapter extends PagedListAdapter<ItemNews, FeedListAdapter.ArticleItemViewHolder> {

    public FeedListAdapter() {
        super(ItemNews.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FeedListAdapter.ArticleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_feed, parent, false);
        return new ArticleItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedListAdapter.ArticleItemViewHolder holder, int position) {
        holder.binding.setItemNews(getItem(position));
    }

    public class ArticleItemViewHolder extends RecyclerView.ViewHolder {

        private ItemFeedBinding binding;

        public ArticleItemViewHolder(ItemFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
