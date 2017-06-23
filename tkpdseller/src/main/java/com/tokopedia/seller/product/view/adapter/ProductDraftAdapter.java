package com.tokopedia.seller.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.ProductDraftViewModel;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.product.view.presenter.ProductDraftView;

import java.io.File;
import java.net.URI;

/**
 * @author hendry on 5/19/17.
 */

public class ProductDraftAdapter extends BaseListAdapter<ProductDraftViewModel> {

    public static final int ITEM_TYPE = 4121;

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
                    int position = getLayoutPosition();
                    if (onDraftDeleteListener!= null) {
                        onDraftDeleteListener.onDelete(data.get(position), position);
                    }
                }
            });
        }

        public void bind (ProductDraftViewModel model) {
            if (TextUtils.isEmpty(model.getProductName())) {
                tvProductName.setText(MethodChecker.fromHtml("<i>" +
                        tvProductName.getContext().getString(R.string.product_no_have_product_name_yet)
                        +"</i>"));
            } else {
                tvProductName.setText(MethodChecker.fromHtml("<b>" +
                        model.getProductName()
                        +"</b>"));
            }
            tvCompletionPercentage.setText(tvCompletionPercentage.getContext().getString(R.string.product_draft_item_percent_complete,
                    model.getCompletionPercent()));
            rcProgressCompletion.setProgress(model.getCompletionPercent());

            if (TextUtils.isEmpty(model.getPrimaryImageUrl())) {
                ivProduct.setImageResource(R.drawable.ic_image_unavailable);
            } else if (isValidURL(model.getPrimaryImageUrl())) {
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

    public void confirmDelete (int position){
        if (position < 0 || position >= data.size()) {
            return;
        }
        data.remove(position);
        notifyItemRemoved(position);
    }

    private boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (Exception e) {
            return false;
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