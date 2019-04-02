package com.tokopedia.inbox.rescenter.historyaddress.view.customadapter;

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
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.animation.GlowingView;
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
    private boolean isFinished;
    public static final int STATUS_FINISHED = 500;
    public static final int STATUS_CANCEL = 0;

    public HistoryAddressAdapter(HistoryAddressFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
    }

    public void setArraylist(List<HistoryAddressViewItem> arraylist, int resolutionStatus) {
        this.arraylist = arraylist;
        this.isFinished = resolutionStatus == STATUS_CANCEL || resolutionStatus == STATUS_FINISHED;
    }

    public List<HistoryAddressViewItem> getArraylist() {
        return arraylist;
    }

    @SuppressWarnings("WeakerAccess")
    public class AddressViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView history;
        ImageView indicator;
        View lineIndicator;
        GlowingView glowingView;

        public AddressViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            history = (TextView) itemView.findViewById(R.id.tv_address_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            lineIndicator = itemView.findViewById(R.id.line_indicator);
            glowingView = (GlowingView) itemView.findViewById(R.id.view_glowing);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_SHIPPING_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recyclerview_history_address, parent, false);
                return new AddressViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_SHIPPING_ITEM:
                bindShippingViewHolder((AddressViewHolder) viewHolder, position);
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
                break;
        }
    }

    private void bindShippingViewHolder(AddressViewHolder holder, int position) {
        context = holder.itemView.getContext();
        final HistoryAddressViewItem item = arraylist.get(position);
        renderData(holder, item);
        renderView(holder, item, position);
    }

    private void renderData(AddressViewHolder holder, HistoryAddressViewItem item) {
        holder.date.setText(
                context.getString(R.string.template_history_additional_information, item.getActionByText(),
                        item.getCreateTimestamp())
        );
        holder.history.setText(MethodChecker.fromHtml(item.getAddress()));
    }

    private void renderView(AddressViewHolder holder, HistoryAddressViewItem item, int position) {
        setPadding(holder);
        setIndicator(holder, item, position);
    }


    private void setIndicator(AddressViewHolder holder, HistoryAddressViewItem item, int position) {
        holder.lineIndicator.setVisibility(position == (arraylist.size() - 1) ?
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

    private void setPadding(AddressViewHolder holder) {
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

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof AddressViewHolder) {
            ((AddressViewHolder)holder).glowingView.renderData(new Object());
        }
    }
}
