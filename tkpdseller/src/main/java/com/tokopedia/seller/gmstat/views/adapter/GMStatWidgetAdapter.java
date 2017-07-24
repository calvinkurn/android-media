package com.tokopedia.seller.gmstat.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.gmstat.views.models.GrossIncome;
import com.tokopedia.seller.gmstat.views.models.LoadingGMModel;
import com.tokopedia.seller.gmstat.views.models.LoadingGMTwoModel;
import com.tokopedia.seller.gmstat.views.viewholder.EmptyVH;
import com.tokopedia.seller.gmstat.views.viewholder.GrossEarnVH;
import com.tokopedia.seller.gmstat.views.viewholder.LoadingGMGrossIncome;

import java.util.List;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatWidgetAdapter extends RecyclerView.Adapter {

    private List<ItemType> baseGMModels;

    public GMStatWidgetAdapter(List<ItemType> baseGMModels) {
        this.baseGMModels = baseGMModels;
    }

    public void clear() {
        this.baseGMModels.clear();
    }

    public void addAll(List<ItemType> baseGMModels) {
        if (baseGMModels == null) {
            return;
        }
        this.baseGMModels.addAll(baseGMModels);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GrossIncome.TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gross_earn, parent, false);
                return new GrossEarnVH(view);
            case LoadingGMTwoModel.TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_gross_earn_loading, parent, false);
                return new LoadingGMGrossIncome(view);
        }
        return new EmptyVH(new ImageView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (baseGMModels.get(position).getType()) {
            case GrossIncome.TYPE:
                ((GrossEarnVH) holder).bind((GrossIncome) baseGMModels.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return baseGMModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (baseGMModels.get(position).getType()) {
            case GrossIncome.TYPE:
            case LoadingGMModel.TYPE:
            case LoadingGMTwoModel.TYPE:
                return baseGMModels.get(position).getType();
            default:
                return super.getItemViewType(position);
        }
    }
}
