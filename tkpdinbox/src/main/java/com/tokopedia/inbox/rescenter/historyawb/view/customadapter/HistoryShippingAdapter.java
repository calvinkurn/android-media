package com.tokopedia.inbox.rescenter.historyawb.view.customadapter;

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
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.historyawb.view.model.HistoryAwbViewItem;
import com.tokopedia.inbox.rescenter.historyawb.view.presenter.HistoryShippingFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 3/23/17.
 */

public class HistoryShippingAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_SHIPPING_ITEM = 100;

    private final HistoryShippingFragmentView fragmentView;
    private List<HistoryAwbViewItem> arraylist;
    private Context context;

    public HistoryShippingAdapter(HistoryShippingFragmentView fragmentView) {
        this.fragmentView = fragmentView;
        this.arraylist = new ArrayList<>();
    }

    public void setArraylist(List<HistoryAwbViewItem> arraylist) {
        this.arraylist = arraylist;
    }

    public List<HistoryAwbViewItem> getArraylist() {
        return arraylist;
    }

    @SuppressWarnings("WeakerAccess")
    public class ShippingViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView history;
        ImageView indicator;
        RecyclerView attachment;
        View actionTrack;
        View actionEdit;
        View lineIndicator;

        public ShippingViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            history = (TextView) itemView.findViewById(R.id.tv_history_text);
            indicator = (ImageView) itemView.findViewById(R.id.indicator);
            attachment = (RecyclerView) itemView.findViewById(R.id.attachment);
            actionTrack = itemView.findViewById(R.id.action_track);
            actionEdit = itemView.findViewById(R.id.action_edit);
            lineIndicator = itemView.findViewById(R.id.line_indicator);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_SHIPPING_ITEM:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.recyclerview_history_shipping, parent, false);
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
        final HistoryAwbViewItem item = arraylist.get(position);
        renderData(holder, item);
        renderView(holder, item);
        if (item.getAttachment() != null && !item.getAttachment().isEmpty()) {
            holder.attachment.setVisibility(View.VISIBLE);
            renderAttachment(holder, item);
        } else {
            holder.attachment.setVisibility(View.GONE);
        }
        holder.actionTrack.setVisibility(item.isShowTrack() ? View.VISIBLE : View.GONE);
        holder.actionTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentView.onActionTrackClick(item.getShipmentID(), item.getShippingRefNumber());
            }
        });
        holder.actionEdit.setVisibility(item.isShowEdit() ? View.VISIBLE : View.GONE);
        holder.actionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentView.onActionEditClick(
                        item.getConversationID(),
                        item.getShipmentID(),
                        item.getShippingRefNumber()
                );
            }
        });
    }

    private void renderAttachment(ShippingViewHolder holder, HistoryAwbViewItem item) {
        AttachmentAdapter attachmentAdapter = new AttachmentAdapter(item.getAttachment());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.attachment.setLayoutManager(layoutManager);
        holder.attachment.setAdapter(attachmentAdapter);
    }

    private void renderData(ShippingViewHolder holder, HistoryAwbViewItem item) {
        holder.date.setText(
                context.getString(R.string.template_history_additional_information, item.getActionByText(),
                        DateFormatUtils.formatDateForResoChatV2(item.getCreateTimestamp()))
        );
        holder.history.setText(item.getRemark().concat(" - ").concat(item.getShippingRefNumber()));
    }

    private void renderView(ShippingViewHolder holder, HistoryAwbViewItem item) {
        setIndicator(holder, item);
        setPadding(holder);
        if (item.isLatest()) {
            holder.date.setTypeface(Typeface.DEFAULT_BOLD);
            holder.history.setTypeface(Typeface.DEFAULT_BOLD);
            holder.history.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.date.setTypeface(null, Typeface.NORMAL);
            holder.history.setTypeface(null, Typeface.NORMAL);
            holder.history.setTextColor(ContextCompat.getColor(context, R.color.label_text_color));
        }
    }

    private void setIndicator(ShippingViewHolder holder, HistoryAwbViewItem item) {
        holder.lineIndicator.setVisibility(
                holder.getAdapterPosition() == getArraylist().size() - 1 ?
                        View.GONE : View.VISIBLE
        );

        holder.indicator.setImageResource(
                item.isLatest() ? R.drawable.bg_circle_green : R.drawable.bg_circle_grey
        );
    }

    private void setPadding(ShippingViewHolder holder) {
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
        return position == getArraylist().size();
    }

    @Override
    public int getItemCount() {
        return getArraylist().size() + super.getItemCount();
    }
}
