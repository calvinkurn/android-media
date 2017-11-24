package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.HistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/13/17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyItems;
    private boolean limit;
    private DetailResCenterFragmentView listener;

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

    public static HistoryAdapter createLimitInstance(List<HistoryItem> historyItems, DetailResCenterFragmentView listener) {
        HistoryAdapter adapter = new HistoryAdapter();
        adapter.setHistoryItems(historyItems);
        adapter.setLimit(true);
        adapter.notifyDataSetChanged();
        adapter.setListener(listener);
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

    public void setListener(DetailResCenterFragmentView listener) {
        this.listener = listener;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_rescenter_history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryItem item = historyItems.get(position);
        Context context = holder.itemView.getContext();
        holder.history.setText(item.getHistoryText());
        holder.tvUsername.setText(item.getProvider());
        holder.tvMonth.setText(DateFormatUtils.get3LettersMonth(item.getDateTimestamp()));
        holder.tvDateNumber.setText(DateFormatUtils.getDayNumber(item.getDateTimestamp()));
        holder.tvTime.setText(DateFormatUtils.getTimeWithWIB(item.getDateTimestamp()));
        holder.indicator.setImageResource(
                item.isLatest() ? R.drawable.ic_check_circle_48dp : R.drawable.ic_dot_grey_24dp
        );
        holder.lineIndicator.setVisibility(position == getHistoryItems().size() - 1 ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionMoreHistoryClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (isLimit()) {
            return getHistoryItems().size() < 2 ? getHistoryItems().size() : 2;
        } else {
            return getHistoryItems().size();
        }
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView history, tvUsername, tvTime, tvDateNumber, tvMonth;
        ImageView indicator;
        View lineIndicator;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            history = (TextView) itemView.findViewById(R.id.tv_history_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            lineIndicator = itemView.findViewById(R.id.line_indicator);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDateNumber = (TextView) itemView.findViewById(R.id.tv_date_number);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
        }
    }
}
