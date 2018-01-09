package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.animation.GlowingView;
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
    private String lastMonth, lastDay;
    public static final int STATUS_FINISHED = 500;
    public static final int STATUS_CANCEL = 0;

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
        this.lastMonth = "";
        this.lastDay = "";
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
        holder.tvMonth.setText(item.getMonth());
        holder.tvDateNumber.setText(item.getDateNumber());
        holder.tvTime.setText(item.getTimeNumber());
        holder.lineSeparator.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        holder.lineIndicator.setVisibility(position == getHistoryItems().size() - 1 ? View.GONE : View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setOnActionMoreHistoryClick();
            }
        });
        if (lastDay.equals(holder.tvDateNumber.getText().toString()) && lastMonth.equals(holder.tvMonth.getText().toString())) {
            holder.tvDateNumber.setVisibility(View.GONE);
            holder.tvMonth.setVisibility(View.GONE);
            holder.lineSeparator.setVisibility(View.GONE);
        }
        lastDay = holder.tvDateNumber.getText().toString();
        lastMonth = holder.tvMonth.getText().toString();
        if (listener.getResolutionStatus() == STATUS_FINISHED || listener.getResolutionStatus() == STATUS_CANCEL) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setImageResource(R.drawable.ic_dot_grey_24dp);
            holder.tvUsername.setTextColor(MethodChecker.getColor(context, R.color.label_text_color));
            holder.history.setTextColor(MethodChecker.getColor(context, R.color.label_text_color));
        } else {
            holder.indicator.setImageResource(
                    item.isLatest() ? R.drawable.bg_circle_green : R.drawable.ic_dot_grey_24dp
            );
            holder.indicator.setVisibility(item.isLatest() ? View.GONE : View.VISIBLE);
            holder.glowingView.setVisibility(item.isLatest() ? View.VISIBLE : View.GONE);
            if (holder.glowingView.getVisibility() == View.VISIBLE) {
                holder.glowingView.renderData(new Object());
            }
            holder.tvUsername.setTextColor(MethodChecker.getColor(
                    context,
                    item.isLatest() ? R.color.tkpd_main_green : R.color.label_text_color));
            holder.history.setTextColor(MethodChecker.getColor(
                    context,
                    item.isLatest() ? R.color.black_70 : R.color.label_text_color));
        }
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
        View lineIndicator, lineSeparator;
        GlowingView glowingView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            history = (TextView) itemView.findViewById(R.id.tv_history_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            lineIndicator = itemView.findViewById(R.id.line_indicator);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDateNumber = (TextView) itemView.findViewById(R.id.tv_date_number);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
            glowingView = (GlowingView) itemView.findViewById(R.id.view_glowing);
            lineSeparator = itemView.findViewById(R.id.view_separator);
        }
    }
}
