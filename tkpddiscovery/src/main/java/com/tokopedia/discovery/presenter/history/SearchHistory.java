package com.tokopedia.discovery.presenter.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.discovery.view.history.SearchHistoryView;

/**
 * Created by Erry on 6/30/2016.
 */
public abstract class SearchHistory extends BaseImpl<SearchHistoryView> {

    public SearchHistory(SearchHistoryView view) {
        super(view);
    }

    public abstract RecyclerView.Adapter getAdapter();
    public abstract void unregisterBroadcast(Context context);
}
