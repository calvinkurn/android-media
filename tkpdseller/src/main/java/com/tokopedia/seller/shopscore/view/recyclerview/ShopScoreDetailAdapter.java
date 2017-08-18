package com.tokopedia.seller.shopscore.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.shopscore.view.model.ShopScoreDetailItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int SHOP_SCORE_DETAIL = 500;
    private List<ShopScoreDetailItemViewModel> data = new ArrayList<>();

    @Override
    public int getItemCount() {
        return data.size() + super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SHOP_SCORE_DETAIL:
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shop_score_detail, parent, false);
                return new ShopScoreDetailViewHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case SHOP_SCORE_DETAIL:
                bindView((ShopScoreDetailViewHolder) viewHolder, data.get(position));
                break;
            default:
                super.onBindViewHolder(viewHolder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.isEmpty() || isLoading() || isRetry()) {
            return super.getItemViewType(position);
        } else {
            return SHOP_SCORE_DETAIL;
        }
    }

    private void bindView(ShopScoreDetailViewHolder viewHolder, ShopScoreDetailItemViewModel data) {
        viewHolder.setTitle(data.getTitle());
        viewHolder.setShopScoreDescription(data.getDescription());
        viewHolder.setProgressBarColor(data.getProgressBarColor());
        viewHolder.setShopScoreValue(data.getValue());
    }

    public void updateData(List<ShopScoreDetailItemViewModel> viewModel) {
        data = viewModel;
        notifyDataSetChanged();
    }
}
