package com.tokopedia.inbox.rescenter.discussion.view.adapter;

import android.content.Context;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder.EmptyDataBinder;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder.LoadMoreDataBinder;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder.MyDiscussionDataBinder;
import com.tokopedia.inbox.rescenter.discussion.view.adapter.databinder.TheirDiscussionDataBinder;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nisie on 3/29/17.
 */

public class ResCenterDiscussionAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_THEIR_MESSAGE = 100;
    private static final int VIEW_MY_MESSAGE = 101;
    private static final int VIEW_LOAD_MORE = 102;
    private static final int VIEW_EMPTY = 103;


    private MyDiscussionDataBinder myDiscussionDataBinder;
    private TheirDiscussionDataBinder theirDiscussionDataBinder;
    private LoadMoreDataBinder loadMoreDataBinder;
    private EmptyDataBinder emptyDataBinder;

    private ArrayList<DiscussionItemViewModel> data;
    private Context context;
    private int canLoadMore = 0;

    public ResCenterDiscussionAdapter(Context context, LoadMoreDataBinder.LoadMoreListener listener) {
        super();
        this.context = context;
        this.data = new ArrayList<>();
        myDiscussionDataBinder = new MyDiscussionDataBinder(this, context);
        theirDiscussionDataBinder = new TheirDiscussionDataBinder(this, context);
        loadMoreDataBinder = new LoadMoreDataBinder(this, listener);
        emptyDataBinder = new EmptyDataBinder(this);

    }

    public static ResCenterDiscussionAdapter createAdapter(Context context, LoadMoreDataBinder.LoadMoreListener listener) {
        return new ResCenterDiscussionAdapter(context, listener);
    }

    @Override
    public int getItemCount() {
        return data.size()
                + canLoadMore + isLoadingFull() + empty;
    }

    private int isLoadingFull() {
        return data.size() == 0 && isLoading() ? 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && data.size() == 0 && !isLoading()) {
            return VIEW_EMPTY;
        } else if (position == 0 && data.size() == 0 && isLoading()) {
            return VIEW_LOADING;
        } else if (canLoadMore == 1 && position == 0) {
            return VIEW_LOAD_MORE;
        } else if (data.get(position - canLoadMore).getMessageCreateBy() != null
                && !data.get(position - canLoadMore).getMessageCreateBy().equals("0")
                && !data.get(position - canLoadMore).getMessageCreateBy().equals(SessionHandler.getLoginID(context))) {
            return VIEW_THEIR_MESSAGE;
        } else {
            return VIEW_MY_MESSAGE;
        }
    }

    @Override
    public DataBinder getDataBinder(int viewType) {
        switch (viewType) {
            case VIEW_MY_MESSAGE:
                return myDiscussionDataBinder;
            case VIEW_THEIR_MESSAGE:
                return theirDiscussionDataBinder;
            case VIEW_LOAD_MORE:
                return loadMoreDataBinder;
            case VIEW_EMPTY:
                return emptyDataBinder;
            default:
                return super.getDataBinder(viewType);
        }
    }

    @Override
    public int getBinderPosition(int position) {
        switch (getItemViewType(position)) {
            case VIEW_LOAD_MORE:
                return 0;
            default:
                return position - canLoadMore;
        }

    }

    public void setList(List<DiscussionItemViewModel> list) {
        this.data.addAll(0, list);
        this.myDiscussionDataBinder.addAll(list);
        this.myDiscussionDataBinder.notifyDataSetChanged();
        this.theirDiscussionDataBinder.addAll(list);
        this.theirDiscussionDataBinder.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    public void addReply(DiscussionItemViewModel item) {
        this.data.add(item);
        this.myDiscussionDataBinder.addReply(item);
        this.theirDiscussionDataBinder.addReply(item);
        notifyDataSetChanged();
    }

    public ArrayList<DiscussionItemViewModel> getData() {
        return data;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        if (canLoadMore) {
            this.canLoadMore = 1;
        } else {
            this.canLoadMore = 0;
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        super.showLoading(isLoading);
        loadMoreDataBinder.setIsLoading(isLoading);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.data.remove(position);
        theirDiscussionDataBinder.remove(position);
        myDiscussionDataBinder.remove(position);
        notifyDataSetChanged();

    }

    public void clearData() {
        this.data.clear();
        theirDiscussionDataBinder.clearData();
        myDiscussionDataBinder.clearData();
        notifyDataSetChanged();
    }

    public void add(int position, DiscussionItemViewModel discussionItemViewModel) {
        this.data.add(position, discussionItemViewModel);
        theirDiscussionDataBinder.add(position, discussionItemViewModel);
        myDiscussionDataBinder.add(position, discussionItemViewModel);
        notifyDataSetChanged();
    }

    public int canLoadMore() {
        return this.canLoadMore;
    }
}
