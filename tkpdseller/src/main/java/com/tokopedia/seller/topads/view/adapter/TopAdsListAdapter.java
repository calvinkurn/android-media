package com.tokopedia.seller.topads.view.adapter;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.topads.view.adapter.viewholder.TopAdsViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public abstract class TopAdsListAdapter<T> extends BaseLinearRecyclerViewAdapter {

    public static final int AD_SINGLE_TYPE = 1;
    public static final int AD_GROUP_TYPE = 2;

    private final Context context;
    protected MultiSelector multiSelector = new MultiSelector();
    public List<T> data = new ArrayList<>();
    public HashMap<Integer, Boolean> checkeds = new HashMap<>();

    ModalMultiSelectorCallback selectionMode = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle(String.valueOf(multiSelector.getSelectedPositions().size()));
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            super.onDestroyActionMode(actionMode);
            finishSelection();
        }
    };


    public TopAdsListAdapter(Context context, List<T> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size()
                + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AD_GROUP_TYPE:
            case AD_SINGLE_TYPE:
                return new TopAdsViewHolder(context, parent, multiSelector);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AD_GROUP_TYPE:
            case AD_SINGLE_TYPE:
                TopAdsViewHolder itemHolder = (TopAdsViewHolder) holder;
                bindDataAds(position, itemHolder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (data.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return getAdType();
        }
    }

    public void setChecked(int position, boolean isChecked){
        checkeds.put(position, isChecked);
        notifyDataSetChanged();
    }

    public boolean isChecked(int position){
        return checkeds.containsKey(position) ? checkeds.get(position) : false;
    }

    private void finishSelection() {
        multiSelector.clearSelections();
        clearChecked();
    }

    public void setData(List<T> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData(){
        this.data.clear();
        finishSelection();
        notifyDataSetChanged();
    }

    public void clearChecked(){
        checkeds.clear();
        notifyDataSetChanged();
    }

    public abstract int getAdType();

    public abstract void bindDataAds(int position, RecyclerView.ViewHolder viewHolder);

    public abstract List<T> getSelectedAds();
}
