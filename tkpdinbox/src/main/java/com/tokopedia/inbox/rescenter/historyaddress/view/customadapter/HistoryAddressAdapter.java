package com.tokopedia.inbox.rescenter.historyaddress.view.customadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.historyaddress.view.model.HistoryAddressViewItem;
import com.tokopedia.inbox.rescenter.historyaddress.view.presenter.HistoryAddressFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryAddressAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_SHIPPING_ITEM = 100;

    private final HistoryAddressFragmentView fragmentView;
    private List<HistoryAddressViewItem> arraylist;
    private Context context;

    public HistoryAddressAdapter(HistoryAddressFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
    }

    public void setArraylist(List<HistoryAddressViewItem> arraylist) {
        this.arraylist = arraylist;
    }

    @SuppressWarnings("WeakerAccess")
    public class ShippingViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView history;
        ImageView indicator;

        public ShippingViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            history = (TextView) itemView.findViewById(R.id.tv_address_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_SHIPPING_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recyclerview_history_address, parent, false);
                return new ShippingViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_SHIPPING_ITEM:
                bindShippingViewHolder((ShippingViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    private void bindShippingViewHolder(ShippingViewHolder holder, int position) {
        context = holder.itemView.getContext();
        final HistoryAddressViewItem item = arraylist.get(position);
        renderData(holder, item);
        renderView(holder, item);
    }

    private void renderData(ShippingViewHolder holder, HistoryAddressViewItem item) {
        String additionalText = context.getString(R.string.template_history_additional_information);
        holder.date.setText(
                additionalText
                        .replace("X123", item.getActionByText())
                        .replace("Y123", item.getDate())
        );
        holder.history.setText(item.getAddress());
    }

    private void renderView(ShippingViewHolder holder, HistoryAddressViewItem item) {
        holder.indicator.setImageResource(
                item.isLatest() ? R.drawable.ic_check_circle_48dp : R.drawable.ic_dot_grey_24dp
        );
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
