package com.tokopedia.seller.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;
import com.tokopedia.seller.topads.view.adapter.TopAdsBaseListAdapter;

/**
 * @author hendry on 5/19/17.
 */

public class ProductDraftAdapter extends TopAdsBaseListAdapter<ProductDraftViewModel> {

    public static final int ITEM_TYPE = 4121;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE:
                return new ProductDraftViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_product_draft, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    private class ProductDraftViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvCompletionPercentage;
        RoundCornerProgressBar rcProgressCompletion;

        private ProductDraftViewHolder(View itemView) {
            super(itemView);
            ivProduct = (ImageView) itemView.findViewById(R.id.iv_product);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvCompletionPercentage = (TextView) itemView.findViewById(R.id.tv_completion_percentage);
            rcProgressCompletion = (RoundCornerProgressBar) itemView.findViewById(R.id.progress_value_completion);
            View deleteIcon = itemView.findViewById(R.id.vg_trash_can);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO hendry delete item
                }
            });
        }

        public void bind (ProductDraftViewModel model) {
            tvProductName.setText("Test");
            tvCompletionPercentage.setText("38% complete");
            rcProgressCompletion.setProgress(38);
            // ImageHandler.loadImageRounded2(ivProduct.getContext(), ivProduct, model.getPrimaryImageUrl());
            ivProduct.setImageResource(R.drawable.ic_image_unavailable);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE:
                bindData(position, holder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public void bindData(final int position, RecyclerView.ViewHolder viewHolder) {
        super.bindData(position, viewHolder);
        final ProductDraftViewHolder draftViewHolder = (ProductDraftViewHolder) viewHolder;
        if (data.size() <= position) {
            return;
        }
        final ProductDraftViewModel model = data.get(position);
        draftViewHolder.bind(model);
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = super.getItemViewType(position);
        if (!isUnknownViewType(itemType)) {
            return itemType;
        }
        return ITEM_TYPE;
    }
}