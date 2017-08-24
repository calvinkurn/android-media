package com.tokopedia.seller.product.variant.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.viewholder.BaseMultipleCheckViewHolder;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.view.adapter.viewholder.ProductVariantItemPickerSearchViewHolder;

import java.util.ArrayList;

/**
 * Created by User on 8/22/2017.
 */

public class ProductVariantDataAdapter extends RecyclerView.Adapter<ProductVariantItemPickerSearchViewHolder> {
    private Context context;
    private ArrayList<ProductVariantValue> productVariantValueArrayList;
    private ArrayList<Long> variantValueIdList;

    private OnProductVariantDataAdapterListener onProductVariantDataAdapterListener;
    public interface OnProductVariantDataAdapterListener{
        void onCheckAny();
        void onUnCheckAll();
    }

    public void setOnProductVariantDataAdapterListener(OnProductVariantDataAdapterListener onProductVariantDataAdapterListener) {
        this.onProductVariantDataAdapterListener = onProductVariantDataAdapterListener;
    }

    public ProductVariantDataAdapter(Context context,
                                     ArrayList<ProductVariantValue> productVariantValueArrayList,
                                     ArrayList<Long> variantValueIdList){
        this.context = context;
        this.productVariantValueArrayList = productVariantValueArrayList == null ? new ArrayList<ProductVariantValue>(): productVariantValueArrayList;
        this.variantValueIdList = variantValueIdList == null ? new ArrayList<Long>(): variantValueIdList;
    }

    @Override
    public ProductVariantItemPickerSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_variant_picker_search, parent, false);
        return new ProductVariantItemPickerSearchViewHolder(view);
    }

    public void checkAllItems(){
        if (variantValueIdList.size() == productVariantValueArrayList.size()) {
            //already check all.
            return;
        }
        for (ProductVariantValue productVariantValue : productVariantValueArrayList) {
            if (! variantValueIdList.contains(productVariantValue.getValueId())) {
                variantValueIdList.add(productVariantValue.getValueId());
            }
        }
        notifyDataSetChanged();
    }

    public void unCheckAllItems(){
        if (variantValueIdList.size() == 0) {
            //already uncheck all.
            return;
        }
        variantValueIdList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ProductVariantItemPickerSearchViewHolder holder, final int position) {
        final ProductVariantValue productVariantValue = productVariantValueArrayList.get(position);
        long productId = productVariantValue.getValueId();
        boolean isChecked = false;
        if (variantValueIdList.contains(productId)) {
            isChecked = true;
        }
        holder.bindObject(productVariantValueArrayList.get(position),isChecked);
        final BaseMultipleCheckViewHolder.CheckedCallback<ProductVariantValue> checkedCallback = new BaseMultipleCheckViewHolder.CheckedCallback<ProductVariantValue>() {
            @Override
            public void onItemChecked(ProductVariantValue productVariantValue, boolean checked) {
                long productVariantId = productVariantValue.getValueId();
                if (checked) {
                    variantValueIdList.add(productVariantId);
                } else {
                    variantValueIdList.remove(productVariantId);
                }
                notifyItemChanged(holder.getAdapterPosition());
                if (onProductVariantDataAdapterListener != null) {
                    if (variantValueIdList.size() == 0) {
                        onProductVariantDataAdapterListener.onUnCheckAll();
                    } else if (variantValueIdList.size() == 1){
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
                checkedCallback.onItemChecked(productVariantValue, checkTo);
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
        for (int i=0, sizei = productVariantValueArrayList.size(); i<sizei; i++) {
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

    public boolean isCheckAny(){
        return productVariantValueArrayList.size() > 0;
    }

    @Override
    public int getItemCount() {
        return productVariantValueArrayList.size();
    }
}
