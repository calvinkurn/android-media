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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.util.EndlessScrollRecycleListener;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.AddFeedModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> list;
    private final FeedPlusTypeFactory typeFactory;
    private EmptyModel emptyModel;
    private EmptyFeedBeforeLoginModel emptyFeedBeforeLoginModel;
    private LoadingModel loadingModel;
    private RetryModel retryModel;
    private boolean unsetListener;
    private AddFeedModel addFeedModel;
    private OnLoadListener loadListener;
    private RecyclerView recyclerView;
    private int itemTreshold = 5;

    private EndlessScrollRecycleListener endlessScrollListener = new EndlessScrollRecycleListener() {
        @Override
        public void onLoadMore(int page, int totalItemsCount) {
            if (isLoading())
                return;
            if (loadListener != null && !unsetListener && list.size() > itemTreshold) {
                showLoading();
                loadListener.onLoad(totalItemsCount);
            }
        }

        @Override
        public void onScroll(int lastVisiblePosition) {
            if (loadListener instanceof OnScrollListener)
                ((OnScrollListener) loadListener).onScroll(lastVisiblePosition);
        }
    };

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
        this.list.addAll(list);
    }

    public void clearData() {
        this.list.clear();
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

    public List<Visitable> getlist() {
        return list;
    }

    public void showAddFeed() {
        this.list.add(addFeedModel);
    }

    public void removeAddFeed(){
        this.list.remove(addFeedModel);
    }

    public void addItem(Visitable item) {
        this.list.add(item);
    }

    public int getItemTreshold() {
        return itemTreshold;
    }

    public void setItemTreshold(int itemTreshold) {
        this.itemTreshold = itemTreshold;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);
        setEndlessScrollListener();
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public void setEndlessScrollListener() {
        unsetListener = false;
        recyclerView.addOnScrollListener(endlessScrollListener);
    }

    public void unsetEndlessScrollListener() {
        unsetListener = true;
        recyclerView.removeOnScrollListener(endlessScrollListener);
    }

    public interface OnLoadListener {
        void onLoad(int totalCount);

    }

    public interface OnScrollListener extends OnLoadListener {
        void onScroll(int lastVisiblePosition);

    }

    public void showUserNotLogin() {
        emptyFeedBeforeLoginModel = new EmptyFeedBeforeLoginModel();
        this.list.add(emptyFeedBeforeLoginModel);
    }
}
