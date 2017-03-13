package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/13/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyItems;
    private boolean limit;

    public HistoryAdapter() {
        historyItems = new ArrayList<>();
    }

    public static HistoryAdapter createDefaultInstance(List<HistoryItem> historyItems) {
        HistoryAdapter adapter = new HistoryAdapter();
        adapter.setHistoryItems(historyItems);
        adapter.setLimit(false);
        adapter.notifyDataSetChanged();
        return adapter;
    }

    public static HistoryAdapter createLimitInstance(List<HistoryItem> historyItems) {
        HistoryAdapter adapter = new HistoryAdapter();
        adapter.setHistoryItems(historyItems);
        adapter.setLimit(true);
        adapter.notifyDataSetChanged();
        return adapter;
    }

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }

    public void setHistoryItems(List<HistoryItem> historyItems) {
        this.historyItems = historyItems;
    }

    private boolean isLimit() {
        return limit;
    }

    private void setLimit(boolean limit) {
        this.limit = limit;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        public HistoryViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_complaint_product_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (isLimit()) {
            return getHistoryItems().size() < 2 ? getHistoryItems().size() : 2;
        } else {
            return getHistoryItems().size();
        }
    }
}
