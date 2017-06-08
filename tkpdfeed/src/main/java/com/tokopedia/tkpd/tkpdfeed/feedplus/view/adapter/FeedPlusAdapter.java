package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.LoadingModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final FeedPlusTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private LoadingModel loadingModel;
    private RetryModel retryModel;
    private boolean canLoadMore;
    private AddFeedModel addFeedModel;

    public FeedPlusAdapter(FeedPlusTypeFactory typeFactory) {
        this.list = new ArrayList<>();
        this.typeFactory = typeFactory;
        this.emptyModel = new EmptyModel();
        this.loadingModel = new LoadingModel();
        this.retryModel = new RetryModel();
        this.addFeedModel = new AddFeedModel();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Visitable> list) {
        this.list = list;
    }

    public void addList(List<Visitable> list) {
        int positionStart = getItemCount();
        this.list.addAll(list);
        notifyItemRangeInserted(positionStart, list.size());
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void showEmpty() {
        this.list.add(emptyModel);
    }

    public void removeEmpty() {
        this.list.remove(emptyModel);
    }

    public void showRetry(){
        int positionStart = getItemCount();
        this.list.add(retryModel);
        notifyItemRangeInserted(positionStart, 1);
    }

    public void removeRetry(){
        int index = this.list.indexOf(retryModel);
        this.list.remove(retryModel);
        notifyItemRemoved(index);
    }

    public void showLoading() {
        this.list.add(loadingModel);
    }

    public void removeLoading() {
        this.list.remove(loadingModel);
    }

    public boolean isLoading() {
        return this.list.contains(loadingModel);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public List<Visitable> getlist() {
        return list;
    }

    public void showAddFeed() {
        int positionStart = getItemCount();
        this.list.add(addFeedModel);
        notifyItemRangeInserted(positionStart, 1);
    }

    public void removeAddFeed(){
        this.list.remove(addFeedModel);
    }
}
