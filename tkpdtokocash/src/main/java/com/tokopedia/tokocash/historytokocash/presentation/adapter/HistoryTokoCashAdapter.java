package com.tokopedia.tokocash.historytokocash.presentation.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.historytokocash.presentation.model.ItemHistory;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/24/17.
 * Contains 3 View Holder : Item, Loading, & empty state for the last item
 */

public class HistoryTokoCashAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_EMPTY = 2;

    private List<ItemHistory> itemHistoryList;
    private Context context;
    private ItemHistoryListener listener;
    private boolean nextUriAvailable;

    public HistoryTokoCashAdapter(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList = itemHistoryList;
    }

    public void setListener(ItemHistoryListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_list_history_tokocash, parent, false);
            return new ItemViewHolderHistory(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_loading_view, parent, false);
            return new ItemLoadingViewHolder(view);
        } else if (viewType == VIEW_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_empty_loading_history, parent, false);
            return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolderHistory) {
            renderItemView((ItemViewHolderHistory) holder, itemHistoryList.get(position));
        } else if (holder instanceof ItemLoadingViewHolder) {
            ItemLoadingViewHolder loadingViewHolder = (ItemLoadingViewHolder) holder;
            loadingViewHolder.progressBarHistory.setVisibility(View.VISIBLE);
        } else if (holder instanceof EmptyViewHolder) {
        }
    }

    private void renderItemView(ItemViewHolderHistory itemViewHolder, final ItemHistory itemHistory) {
        ImageHandler.loadImageThumbs(context, itemViewHolder.iconItem, itemHistory.getUrlImage());
        itemViewHolder.descItem.setText(!TextUtils.isEmpty(itemHistory.getNotes()) ? itemHistory.getNotes() :
                itemHistory.getDescription());
        itemViewHolder.titleItem.setText(itemHistory.getTitle());
        itemViewHolder.priceItem.setText(itemHistory.getAmountChanges());
        itemViewHolder.priceItem.setTextColor(ContextCompat.getColor(context,
                itemHistory.getAmountChangesSymbol().equals("+") ? R.color.green_500 : R.color.red_500));
        itemViewHolder.dateItem.setText(itemHistory.getTransactionInfoDate());
        itemViewHolder.layoutItemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickItemHistory(itemHistory);
            }
        });
    }

    public void addItemHistoryList(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList.clear();
        this.itemHistoryList.addAll(itemHistoryList);
        notifyDataSetChanged();
    }

    public void addItemHistoryListLoadMore(List<ItemHistory> itemHistoryList) {
        this.itemHistoryList.addAll(itemHistoryList);
        notifyDataSetChanged();
    }

    public void isNextUri(boolean nextUri) {
        this.nextUriAvailable = nextUri;
    }

    @Override
    public int getItemCount() {
        return itemHistoryList == null || itemHistoryList.size() == 0 ? 0 : itemHistoryList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            if (!nextUriAvailable)
                return VIEW_EMPTY;
            else
                return VIEW_TYPE_LOADING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    static class ItemViewHolderHistory extends RecyclerView.ViewHolder {

        private RelativeLayout layoutItemHistory;
        private ImageView iconItem;
        private TextView titleItem;
        private TextView priceItem;
        private TextView descItem;
        private TextView dateItem;

        public ItemViewHolderHistory(View itemView) {
            super(itemView);
            layoutItemHistory = (RelativeLayout) itemView.findViewById(R.id.layout_item_history);
            iconItem = (ImageView) itemView.findViewById(R.id.icon_item_history);
            titleItem = (TextView) itemView.findViewById(R.id.title_item_history);
            priceItem = (TextView) itemView.findViewById(R.id.price_item_history);
            descItem = (TextView) itemView.findViewById(R.id.desc_item_history);
            dateItem = (TextView) itemView.findViewById(R.id.date_item_history);
        }
    }

    static class ItemLoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBarHistory;

        public ItemLoadingViewHolder(View itemView) {
            super(itemView);
            progressBarHistory = (ProgressBar) itemView.findViewById(R.id.progress_bar_history);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface ItemHistoryListener {

        void onClickItemHistory(ItemHistory itemHistory);
    }
}