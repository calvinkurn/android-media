package com.tokopedia.seller.product.variant.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantDetailViewHolder;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;

import java.util.ArrayList;

/**
 * Created by hendry on 8/22/2017.
 */
@Deprecated
public class ProductVariantDetailAdapter extends RecyclerView.Adapter<ProductVariantDetailViewHolder> {

    public interface OnProductVariantDataAdapterListener {
        void onCheckAny();
        void onUnCheckAll();
    }

    private Context context;
    private ArrayList<ProductVariantDetailViewModel> productVariantValueArrayList;
    private ArrayList<Long> variantValueIdList;

    private OnProductVariantDataAdapterListener onProductVariantDataAdapterListener;

    public void setOnProductVariantDataAdapterListener(OnProductVariantDataAdapterListener onProductVariantDataAdapterListener) {
        this.onProductVariantDataAdapterListener = onProductVariantDataAdapterListener;
    }

    public ProductVariantDetailAdapter(Context context,
                                       ArrayList<ProductVariantDetailViewModel> productVariantValueArrayList,
                                       ArrayList<Long> variantValueIdList) {
        this.context = context;
        this.productVariantValueArrayList = productVariantValueArrayList;
        this.variantValueIdList = variantValueIdList;
    }

    @Override
    public ProductVariantDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_variant_detail, parent, false);
        return new ProductVariantDetailViewHolder(view);
    }

    public void checkAllItems() {
        if (variantValueIdList.size() == productVariantValueArrayList.size()) {
            //already check all.
            return;
        }
        for (ProductVariantDetailViewModel productVariantDetailViewModel : productVariantValueArrayList) {
            if (!variantValueIdList.contains(productVariantDetailViewModel.getValueId())) {
                variantValueIdList.add(productVariantDetailViewModel.getValueId());
            }
        }
        notifyDataSetChanged();
    }

    public void unCheckAllItems() {
        if (variantValueIdList.size() == 0) {
            //already uncheck all.
            return;
        }
        variantValueIdList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ProductVariantDetailViewHolder holder, final int position) {
        final ProductVariantDetailViewModel productVariantDetailViewModel = productVariantValueArrayList.get(position);
        long productId = productVariantDetailViewModel.getValueId();
        boolean isChecked = false;
        if (variantValueIdList.contains(productId)) {
            isChecked = true;
        }
        holder.bindObject(productVariantValueArrayList.get(position), isChecked);
        final BaseMultipleCheckViewHolder.CheckedCallback<ProductVariantDetailViewModel> checkedCallback = new BaseMultipleCheckViewHolder.CheckedCallback<ProductVariantDetailViewModel>() {
            @Override
            public void onItemChecked(ProductVariantDetailViewModel variantDetailViewModel, boolean checked) {
                long productVariantId = variantDetailViewModel.getValueId();
                if (checked) {
                    variantValueIdList.add(productVariantId);
                } else {
                    variantValueIdList.remove(productVariantId);
                }
                notifyItemChanged(holder.getAdapterPosition());
                if (onProductVariantDataAdapterListener != null) {
                    if (variantValueIdList.size() == 0) {
                        onProductVariantDataAdapterListener.onUnCheckAll();
                    } else if (variantValueIdList.size() == 1) {
                        if (checked) {
                            onProductVariantDataAdapterListener.onCheckAny();
                        }
                    }
                }
            }
        };
        holder.setCheckedCallback(checkedCallback);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkTo = !holder.isChecked();
                holder.setChecked(checkTo);
                checkedCallback.onItemChecked(productVariantDetailViewModel, checkTo);
            }
        });
    }

    public ArrayList<Long> getVariantValueIdList() {
        return variantValueIdList;
    }

    /**
     * @return variantValueIdList, the rearrangement is followed by the source.
     */
    public ArrayList<Long> getVariantValueIdListSorted() {
        ArrayList<Long> sortedArrayList = new ArrayList<>();
        for (int i = 0, sizei = productVariantValueArrayList.size(); i < sizei; i++) {
            int position = variantValueIdList.indexOf(productVariantValueArrayList.get(i).getValueId());
            if (position >= 0) {
                sortedArrayList.add(variantValueIdList.get(position));
            }
        }
        if (sortedArrayList.size() == 0) {
            return null;
        }
        return sortedArrayList;
    }

    @Override
    public int getItemCount() {
        return productVariantValueArrayList.size();
    }
}