package com.tokopedia.inbox.inboxmessageold.adapter;

import android.content.Context;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.inbox.inboxmessageold.adapter.databinder.LoadMoreDataBinder;
import com.tokopedia.inbox.inboxmessageold.adapter.databinder.MyMessageDataBinder;
import com.tokopedia.inbox.inboxmessageold.adapter.databinder.TheirMessageDataBinder;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;
import com.tokopedia.inbox.inboxmessageold.presenter.InboxMessageDetailFragmentPresenter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nisie on 5/19/16.
 */
public class InboxMessageDetailAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_THEIR_MESSAGE = 100;
    private static final int VIEW_MY_MESSAGE = 101;
    private static final int VIEW_LOAD_MORE = 102;


    private MyMessageDataBinder myMessageDataBinder;
    private TheirMessageDataBinder theirMessageDataBinder;
    private LoadMoreDataBinder loadMoreDataBinder;

    ArrayList<InboxMessageDetailItem> data;
    Context context;
    int canLoadMore = 0;

    public InboxMessageDetailAdapter(Context context,
                                     InboxMessageDetailFragmentPresenter presenter) {
        super();
        this.context = context;
        this.data = new ArrayList<>();
        loadMoreDataBinder = new LoadMoreDataBinder(this);
        myMessageDataBinder = new MyMessageDataBinder(this, context);
        theirMessageDataBinder = new TheirMessageDataBinder(this, context);
        theirMessageDataBinder.setPresenter(presenter);
        loadMoreDataBinder.setPresenter(presenter);
    }

    public static InboxMessageDetailAdapter createAdapter(Context context,
                                                          InboxMessageDetailFragmentPresenter presenter) {
        return new InboxMessageDetailAdapter(context, presenter);
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
            case VIEW_LOAD_MORE:
                return loadMoreDataBinder;
            case VIEW_MY_MESSAGE:
                return myMessageDataBinder;
            case VIEW_THEIR_MESSAGE:
                return theirMessageDataBinder;
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

    public void setList(List<InboxMessageDetailItem> list) {
        Collections.reverse(list);
        this.data.addAll(0, list);
        this.myMessageDataBinder.addAll(list);
        this.theirMessageDataBinder.addAll(list);
        notifyDataSetChanged();
    }

    public void addReply(InboxMessageDetailItem item) {
        this.data.add(item);
        this.myMessageDataBinder.addReply(item);
        this.theirMessageDataBinder.addReply(item);
        notifyDataSetChanged();
    }

    public ArrayList<InboxMessageDetailItem> getData() {
        return data;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        if (canLoadMore) {
            this.canLoadMore = 1;
            theirMessageDataBinder.setCanLoadMore(1);
            myMessageDataBinder.setCanLoadMore(1);
        } else {
            this.canLoadMore = 0;
            theirMessageDataBinder.setCanLoadMore(0);
            myMessageDataBinder.setCanLoadMore(0);
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
        theirMessageDataBinder.remove(position);
        myMessageDataBinder.remove(position);
        notifyDataSetChanged();

    }

    public void clearData() {
        this.data.clear();
        theirMessageDataBinder.clearData();
        myMessageDataBinder.clearData();
        notifyDataSetChanged();
    }

    public void add(int position, InboxMessageDetailItem inboxMessageDetailItem) {
        this.data.add(position, inboxMessageDetailItem);
        theirMessageDataBinder.add(position, inboxMessageDetailItem);
        myMessageDataBinder.add(position, inboxMessageDetailItem);
        notifyDataSetChanged();
    }

    public void setNav(String nav) {
        theirMessageDataBinder.setNav(nav);
    }

    public int canLoadMore() {
        return this.canLoadMore;
    }
}
