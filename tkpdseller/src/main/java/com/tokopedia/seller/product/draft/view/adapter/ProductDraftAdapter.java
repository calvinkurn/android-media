package com.tokopedia.seller.product.draft.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.adapter.BaseViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;
import com.tokopedia.seller.R;

import java.io.File;

/**
 * @author hendry on 5/19/17.
 */

public class ProductDraftAdapter extends BaseListAdapter<ProductDraftViewModel> {

    OnDraftDeleteListener onDraftDeleteListener;
    public interface OnDraftDeleteListener{
        void onDelete(ProductDraftViewModel draftViewModel, int position);
    }

    public void setOnDraftDeleteListener(OnDraftDeleteListener onDraftDeleteListener) {
        this.onDraftDeleteListener = onDraftDeleteListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductDraftViewModel.TYPE:
                return new ProductDraftViewHolder(getLayoutView(parent, R.layout.item_product_draft));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    public void confirmDelete (int position){
        if (position < 0 || position >= data.size()) {
            return;
        }
        data.remove(position);
        notifyItemRemoved(position);
    }

    private class ProductDraftViewHolder extends BaseViewHolder<ProductDraftViewModel> {
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
                    int position = getLayoutPosition();
                    if (onDraftDeleteListener!= null) {
                        onDraftDeleteListener.onDelete(data.get(position), position);
                    }
                }
            });
        }

        @Override
        public void bindObject(ProductDraftViewModel model) {
            if (TextUtils.isEmpty(model.getProductName())) {
                tvProductName.setText(MethodChecker.fromHtml("<i>" +
                        tvProductName.getContext().getString(R.string.product_no_have_product_name_yet)
                        + "</i>"));
            } else {
                tvProductName.setText(MethodChecker.fromHtml("<b>" +
                        model.getProductName()
                        + "</b>"));
            }
            tvCompletionPercentage.setText(tvCompletionPercentage.getContext().getString(R.string.product_draft_item_percent_complete,
                    model.getCompletionPercent()));
            rcProgressCompletion.setProgress(model.getCompletionPercent());

            if (TextUtils.isEmpty(model.getPrimaryImageUrl())) {
                ivProduct.setImageResource(R.drawable.ic_image_unavailable);
            } else if (URLUtil.isNetworkUrl(model.getPrimaryImageUrl())) {
                ImageHandler.loadImageFitCenter(
                        ivProduct.getContext(),
                        ivProduct,
                        model.getPrimaryImageUrl()
                );
            } else { // local Uri
                ImageHandler.loadImageFromFileFitCenter(
                        ivProduct.getContext(),
                        ivProduct,
                        new File(model.getPrimaryImageUrl())
                );
            }
        }

    }
}