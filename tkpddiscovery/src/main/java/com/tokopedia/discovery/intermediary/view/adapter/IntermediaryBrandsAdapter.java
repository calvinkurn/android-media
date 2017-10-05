package com.tokopedia.discovery.intermediary.view.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R2;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.domain.model.BrandModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 5/26/17.
 */

public class IntermediaryBrandsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private int brandWidth;
    private List<BrandModel> brandModels;
    private IntermediaryBrandsAdapter.BrandListener brandListener;

    public IntermediaryBrandsAdapter(int brandWidth, List<BrandModel> brandModels,
                                     IntermediaryBrandsAdapter.BrandListener brandListener) {
        this.brandWidth = brandWidth;
        this.brandModels = brandModels;
        this.brandListener = brandListener;
    }

    @Override
    public IntermediaryBrandsAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.item_brand_intermediary, null
        );
        v.setMinimumWidth(brandWidth);
        return new IntermediaryBrandsAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        IntermediaryBrandsAdapter.ItemRowHolder itemRowHolder = (IntermediaryBrandsAdapter.ItemRowHolder) holder;
        itemRowHolder.container.getLayoutParams().width = brandWidth;
        ImageHandler.LoadImage(itemRowHolder.thumbnail,brandModels.get(position).getImageUrl());
        itemRowHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandListener.onBrandClick(brandModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandModels.size();
    }

    class ItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.thumbnail)
        ImageView thumbnail;

        @BindView(R2.id.linWrapper)
        LinearLayout container;

        ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface BrandListener {
        void onBrandClick(BrandModel brandModel);
    }

}


