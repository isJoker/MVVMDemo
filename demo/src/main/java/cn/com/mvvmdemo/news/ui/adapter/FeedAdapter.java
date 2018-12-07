package cn.com.mvvmdemo.news.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.com.mvvmdemo.R;
import cn.com.mvvmdemo.databinding.ItemFeedBinding;
import cn.com.mvvmdemo.news.model.ItemNews;

/**
 * Created by JokerWan on 2018/12/3.
 * Function:
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ArticleItemViewHolder> {
    private List<ItemNews> itemNews;

    public FeedAdapter() {
        itemNews = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArticleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_feed, parent, false);
        return new ArticleItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleItemViewHolder holder, int position) {
        holder.binding.setItemNews(itemNews.get(position));
    }

    public class ArticleItemViewHolder extends RecyclerView.ViewHolder {

        private ItemFeedBinding binding;

        public ArticleItemViewHolder(ItemFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return itemNews.size();
    }

    public void setData(List<ItemNews> data){
        itemNews.clear();
        if(data != null) {
            itemNews.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<ItemNews> data){
        int preSize = itemNews.size();
        if(data != null && data.size() > 0) {
            itemNews.addAll(data);
            notifyItemRangeInserted(preSize,data.size());
        }
    }
}
