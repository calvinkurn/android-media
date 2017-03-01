package com.tokopedia.seller.opportunity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.viewmodel.Opportunity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityListAdapter extends BaseLinearRecyclerViewAdapter{

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    ArrayList<Opportunity> list;

    private static final int VIEW_OPPORTUNITY = 100;
    private boolean actionEnabled;

    public static OpportunityListAdapter createInstance() {
        return new OpportunityListAdapter();
    }

    public OpportunityListAdapter(){
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_OPPORTUNITY:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_opportunity, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_OPPORTUNITY:
                bindOpportunity((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindOpportunity(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_OPPORTUNITY;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    public List<Opportunity> getList() {
        return list;
    }

    public void setList(List<Opportunity> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
