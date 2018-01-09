package com.tokopedia.inbox.rescenter.historyaction.view.customadapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.animation.GlowingView;
import com.tokopedia.inbox.rescenter.historyaction.view.model.HistoryActionViewItem;
import com.tokopedia.inbox.rescenter.historyaction.view.model.HistoryActionAdapterModel;
import com.tokopedia.inbox.rescenter.historyaction.view.presenter.HistoryActionFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryActionAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_SHIPPING_ITEM = 100;

    private final HistoryActionFragmentView fragmentView;
    private List<HistoryActionAdapterModel> arraylist;
    private Context context;
    private boolean isFinished;
    public static final int STATUS_FINISHED = 500;
    public static final int STATUS_CANCEL = 0;

    public HistoryActionAdapter(HistoryActionFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
    }

    public void setArraylist(List<HistoryActionViewItem> arraylist, int resolutionStatus) {
        List<HistoryActionAdapterModel> modelList = new ArrayList<>();
        String lastMonth = "", lastDay = "";
        for (HistoryActionViewItem viewItem : arraylist) {
            HistoryActionAdapterModel model = new HistoryActionAdapterModel();
            model.setItem(viewItem);
            if (lastDay.equals(viewItem.getDateNumber()) && lastMonth.equals(viewItem.getMonth())) {
                model.setShowLastDayAndMonth(false);
                model.setShowDateSeparator(false);
            } else {
                model.setShowLastDayAndMonth(true);
                model.setShowDateSeparator(true);
                if (viewItem.isLatest())
                    model.setShowDateSeparator(false);
            }
            lastDay = viewItem.getDateNumber();
            lastMonth = viewItem.getMonth();
            model.setShowGlowingView(viewItem.isLatest());
            modelList.add(model);
        }
        this.arraylist = modelList;
        isFinished  = resolutionStatus == STATUS_FINISHED || resolutionStatus == STATUS_CANCEL;
    }

    public List<HistoryActionAdapterModel> getArraylist() {
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
        final HistoryActionAdapterModel model = arraylist.get(position);
        renderData(holder, model);
        renderView(holder, model);
        holder.lineIndicator.setVisibility(arraylist.size() == 1 || arraylist.size() - 1 == position ?
                View.GONE :
                View.VISIBLE);
    }

    private void renderData(ActionViewHolder holder, HistoryActionAdapterModel model) {
        HistoryActionViewItem item = model.getItem();
        holder.tvUsername.setText(item.getActionByText());
        holder.tvMonth.setText(item.getMonth());
        holder.tvDateNumber.setText(item.getDateNumber());
        holder.tvTime.setText(item.getTimeNumber());
        holder.history.setText(item.getHistoryText());
    }

    private void renderView(ActionViewHolder holder, HistoryActionAdapterModel model) {
        HistoryActionViewItem item = model.getItem();
        setIndicator(holder, model);
        holder.lineSeparator.setVisibility(model.isShowDateSeparator() ? View.VISIBLE : View.GONE);
        holder.tvDateNumber.setVisibility(model.isShowLastDayAndMonth() ? View.VISIBLE : View.GONE);
        holder.tvMonth.setVisibility(model.isShowLastDayAndMonth() ? View.VISIBLE : View.GONE);
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

    private void setIndicator(ActionViewHolder holder, HistoryActionAdapterModel model) {
        holder.indicator.setImageResource(R.drawable.ic_dot_grey_24dp);
        holder.indicator.setVisibility(model.isShowGlowingView() ? View.GONE : View.VISIBLE);
        holder.glowingView.setVisibility(model.isShowGlowingView() ? View.VISIBLE : View.GONE);
        if (model.isShowGlowingView() && !isFinished) {
            holder.glowingView.renderData(new Object());
        } else {
            holder.glowingView.stopAnimation();
            holder.glowingView.setVisibility(View.GONE);
            holder.indicator.setVisibility(View.VISIBLE);
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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ActionViewHolder) {
            ((ActionViewHolder) holder).glowingView.renderData(new Object());
        }
    }
}
