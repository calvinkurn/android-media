package com.tokopedia.inbox.rescenter.historyaction.view.customadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.animation.GlowingView;
import com.tokopedia.inbox.rescenter.historyaction.view.model.HistoryActionViewItem;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryActionAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_SHIPPING_ITEM = 100;

    private final HistoryActionFragmentView fragmentView;
    private List<HistoryActionViewItem> arraylist;
    private Context context;
    private String lastMonth, lastDay;
    private boolean isFinished;
    public static final int STATUS_FINISHED = 500;
    public static final int STATUS_CANCEL = 0;

    public HistoryActionAdapter(HistoryActionFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
        this.lastMonth = "";
        this.lastDay = "";
    }

    public void setArraylist(List<HistoryActionViewItem> arraylist, int resolutionStatus) {
        this.arraylist = arraylist;
        isFinished  = resolutionStatus == STATUS_FINISHED || resolutionStatus == STATUS_CANCEL;
    }

    public List<HistoryActionViewItem> getArraylist() {
        return arraylist;
    }

    @SuppressWarnings("WeakerAccess")
    public class ActionViewHolder extends RecyclerView.ViewHolder {

        TextView history, tvUsername, tvTime, tvDateNumber, tvMonth;
        ImageView indicator;
        View lineIndicator, lineSeparator;
        GlowingView glowingView;

        public ActionViewHolder(View itemView) {
            super(itemView);
            history = (TextView) itemView.findViewById(R.id.tv_history_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            lineIndicator = itemView.findViewById(R.id.line_indicator);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDateNumber = (TextView) itemView.findViewById(R.id.tv_date_number);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
            lineSeparator = itemView.findViewById(R.id.view_separator);
            glowingView = (GlowingView) itemView.findViewById(R.id.view_glowing);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_SHIPPING_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recyclerview_rescenter_history_item, parent, false);
                return new ActionViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_SHIPPING_ITEM:
                bindShippingViewHolder((ActionViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    private void bindShippingViewHolder(ActionViewHolder holder, int position) {
        context = holder.itemView.getContext();
        final HistoryActionViewItem item = arraylist.get(position);
        holder.lineSeparator.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        renderData(holder, item);
        renderView(holder, item);
    }

    private void renderData(ActionViewHolder holder, HistoryActionViewItem item) {
        holder.tvUsername.setText(item.getActionByText());
        holder.tvMonth.setText(item.getMonth());
        holder.tvDateNumber.setText(item.getDateNumber());
        holder.tvTime.setText(item.getTimeNumber());
        holder.history.setText(item.getHistoryText());
        holder.tvDateNumber.setVisibility(View.VISIBLE);
        holder.tvMonth.setVisibility(View.VISIBLE);
        if (lastDay.equals(holder.tvDateNumber.getText().toString()) && lastMonth.equals(holder.tvMonth.getText().toString())) {
            holder.tvDateNumber.setVisibility(View.GONE);
            holder.tvMonth.setVisibility(View.GONE);
            holder.lineSeparator.setVisibility(View.GONE);
        }
        lastDay = holder.tvDateNumber.getText().toString();
        lastMonth = holder.tvMonth.getText().toString();
    }

    private void renderView(ActionViewHolder holder, HistoryActionViewItem item) {
        setIndicator(holder, item);
        if (isFinished) {
            holder.tvUsername.setTextColor(ContextCompat.getColor(context, R.color.label_text_color));
            holder.history.setTextColor(ContextCompat.getColor(context, R.color.label_text_color));
        } else {
            if (item.isLatest()) {
                holder.tvUsername.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green));
                holder.history.setTextColor(ContextCompat.getColor(context, R.color.black_70));
            } else {
                holder.tvUsername.setTextColor(ContextCompat.getColor(context, R.color.label_text_color));
                holder.history.setTextColor(ContextCompat.getColor(context, R.color.label_text_color));
            }
        }

    }

    private void setIndicator(ActionViewHolder holder, HistoryActionViewItem item) {
        holder.lineIndicator.setVisibility(
                holder.getAdapterPosition() == getArraylist().size() - 1 ?
                        View.GONE : View.VISIBLE
        );

        if (isFinished) {
            holder.indicator.setImageResource(R.drawable.ic_dot_grey_24dp);
        } else {
            holder.indicator.setImageResource(
                    item.isLatest() ? R.drawable.bg_circle_green : R.drawable.ic_dot_grey_24dp
            );

            holder.indicator.setVisibility(item.isLatest() ? View.GONE : View.VISIBLE);
            holder.glowingView.setVisibility(item.isLatest() ? View.VISIBLE : View.GONE);
            if (holder.glowingView.getVisibility() == View.VISIBLE) {
                holder.glowingView.renderData(new Object());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (arraylist.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_SHIPPING_ITEM;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == arraylist.size();
    }

    @Override
    public int getItemCount() {
        return arraylist.size() + super.getItemCount();
    }
}
