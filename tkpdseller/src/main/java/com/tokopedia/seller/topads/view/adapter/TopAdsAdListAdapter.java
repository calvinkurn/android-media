package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public class TopAdsAdListAdapter<T extends Ad> extends BaseLinearRecyclerViewAdapter {

    public interface Callback {
        void onChecked(int position, boolean checked);

        void onClicked(Ad ad);
    }

    public static final int AD_TYPE = 1;

    private List<T> data;

    private MultiSelector multiSelector;
    private HashMap<Integer, Boolean> checkedList;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public TopAdsAdListAdapter() {
        super();
        this.data = new ArrayList<>();
        multiSelector = new MultiSelector();
        checkedList = new HashMap<>();
    }

    public int getDataSize() {
        return data.size();
    }

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AD_TYPE:
                return new TopAdsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_top_ads_ad, parent, false), multiSelector);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AD_TYPE:
                TopAdsViewHolder itemHolder = (TopAdsViewHolder) holder;
                bindDataAds(position, itemHolder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    public void bindDataAds(final int position, RecyclerView.ViewHolder viewHolder) {
        final Ad ad = data.get(position);
        final TopAdsViewHolder topAdsViewHolder = (TopAdsViewHolder) viewHolder;
        topAdsViewHolder.bindObject(ad);
        topAdsViewHolder.checkedPromo.setChecked(isChecked(position));
        topAdsViewHolder.checkedPromo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(position, isChecked);
            }
        });
        topAdsViewHolder.mainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (multiSelector.getSelectedPositions().size() == 0) {
//                    topAdsListPromoViewListener.startSupportActionMode(selectionMode);
                    multiSelector.setSelected(topAdsViewHolder, true);
                } else {
                    multiSelector.tapSelection(position, topAdsViewHolder.getItemId());
//                    topAdsListPromoViewListener.setTitleMode(multiSelector.getSelectedPositions().size() + "");
                    if (multiSelector.getSelectedPositions().size() == 0) {
//                        topAdsListPromoViewListener.finishActionMode();
                        multiSelector.refreshAllHolders();
                    }
                }
                setChecked(position, multiSelector.isSelected(position, topAdsViewHolder.getItemId()));
                return true;
            }
        });
        topAdsViewHolder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!multiSelector.tapSelection(topAdsViewHolder)) {
                    if (callback != null) {
                        callback.onClicked(ad);
                    }
                    /*if (isLoading()) {
                        getPaging().setPage(getPaging().getPage() - 1);
                        presenter.onFinishConnection();
                    }*/
//                    topAdsListPromoViewListener.moveToDetail(position);
                } else {
                    setChecked(position, multiSelector.isSelected(position, topAdsViewHolder.getItemId()));
                    if (multiSelector.getSelectedPositions().size() == 0) {
//                        topAdsListPromoViewListener.finishActionMode();
                        multiSelector.refreshAllHolders();
                    } else {

//                        topAdsListPromoViewListener.setTitleMode(multiSelector.getSelectedPositions().size() + "");
                    }
                }
            }
        });
    }

    public void clearCheckedList() {
        checkedList = new HashMap<>();
        notifyDataSetChanged();
    }

    public List<T> getSelectedList() {
        List<T> selectedAds = new ArrayList<>();
        for (int position : checkedList.keySet()) {
            selectedAds.add(data.get(position));
        }
        return selectedAds;
    }

    private boolean isLastItemPosition(int position) {
        return position == data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (data.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return AD_TYPE;
        }
    }

    public void setChecked(int position, boolean checked) {
        checkedList.put(position, checked);
        if (!checked) {
            checkedList.remove(position);
        }
        if (callback != null) {
            callback.onChecked(position, checked);
        }
    }

    public boolean isChecked(int position) {
        return checkedList.containsKey(position) ? checkedList.get(position) : false;
    }

    private void finishSelection() {
        multiSelector.clearSelections();
        clearChecked();
    }

    public void addData(List<T> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.data.clear();
        finishSelection();
        notifyDataSetChanged();
    }

    public void clearChecked() {
        checkedList.clear();
        notifyDataSetChanged();
    }
}