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
import com.tokopedia.inbox.R;
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

    public HistoryActionAdapter(HistoryActionFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
    }

    public void setArraylist(List<HistoryActionViewItem> arraylist) {
        this.arraylist = arraylist;
    }

    public List<HistoryActionViewItem> getArraylist() {
        return arraylist;
    }

    @SuppressWarnings("WeakerAccess")
    public class ActionViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView history;
        ImageView indicator;
        View lineIndicator;

        public ActionViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            history = (TextView) itemView.findViewById(R.id.tv_action_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            lineIndicator = itemView.findViewById(R.id.line_indicator);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_SHIPPING_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recyclerview_history_action, parent, false);
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
        renderData(holder, item);
        renderView(holder, item);
    }

    private void renderData(ActionViewHolder holder, HistoryActionViewItem item) {
        String additionalText = context.getString(R.string.template_history_additional_information);
        holder.date.setText(
                additionalText
                        .replace("X123", item.getActionByText())
                        .replace("Y123", item.getDate())
        );
        holder.history.setText(item.getHistoryText());
    }

    private void renderView(ActionViewHolder holder, HistoryActionViewItem item) {
        setPadding(holder);
        setIndicator(holder, item);
        if (item.isLatest()) {
            holder.date.setTypeface(Typeface.DEFAULT_BOLD);
            holder.history.setTypeface(Typeface.DEFAULT_BOLD);
            holder.history.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.date.setTypeface(null, Typeface.NORMAL);
            holder.history.setTypeface(null, Typeface.NORMAL);
            holder.history.setTextColor(ContextCompat.getColor(context, R.color.grey));
        }
    }

    private void setIndicator(ActionViewHolder holder, HistoryActionViewItem item) {
        holder.lineIndicator.setVisibility(
                holder.getAdapterPosition() == getArraylist().size() - 1 ?
                        View.GONE : View.VISIBLE
        );

        holder.indicator.setImageResource(
                item.isLatest() ? R.drawable.ic_check_circle_48dp : R.drawable.ic_dot_grey_24dp
        );
    }

    private void setPadding(ActionViewHolder holder) {
        if (holder.getAdapterPosition() == 0) {
            holder.itemView.setPadding(
                    context.getResources().getDimensionPixelSize(R.dimen.padding_small),
                    context.getResources().getDimensionPixelSize(R.dimen.padding_small),
                    context.getResources().getDimensionPixelSize(R.dimen.padding_small),
                    0
            );
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
