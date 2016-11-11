package com.tokopedia.transaction.cart.adapter;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.transaction.cart.model.CartProductItemEditable;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartProductItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_PRODUCT_ITEM = R.layout.cart_product_item_holder;
    private final Fragment hostFragment;


    private List<Object> dataList = new ArrayList<>();
    private boolean editMode;

    public CartProductItemAdapter(Fragment hostFragment) {
        this.hostFragment = hostFragment;
    }

    public void fillDataList(List<CartProduct> dataList) {
        for (CartProduct data : dataList) {
            this.dataList.add(new CartProductItemEditable(data));
        }
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PRODUCT_ITEM) {
            return new ProductItemHolder(LayoutInflater.from(
                    hostFragment.getActivity()).inflate(viewType, parent, false
            ));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int i = getItemViewType(position);
        if (i == TYPE_PRODUCT_ITEM) {
            final ProductItemHolder productItemHolder = (ProductItemHolder) holder;
            final CartProductItemEditable item = (CartProductItemEditable) dataList.get(position);
            bindProductItemHolder(productItemHolder, item);
        }
    }

    private void bindProductItemHolder(ProductItemHolder holder, CartProductItemEditable item) {
        holder.tvNameProduct.setText(item.getCartProduct().getProductName());
        holder.tvPriceProduct.setText(item.getCartProduct().getProductTotalPriceIdr());
        holder.tvWeightProduct.setText(item.getCartProduct().getProductTotalWeight());
        holder.etQuantityProduct.setEnabled(editMode);
        holder.etNotesProduct.setEnabled(editMode);
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof CartProductItemEditable) {
            return TYPE_PRODUCT_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void isEditableMode(boolean editMode) {
        this.editMode = editMode;
        this.notifyDataSetChanged();
    }

    static class ProductItemHolder extends RecyclerView.ViewHolder {
        @Bind(R2.id.tv_error)
        TextView tvError;
        @Bind(R2.id.tv_preorder_label)
        TextView tvPreorderLabel;
        @Bind(R2.id.btn_delete)
        ImageView btnDelete;
        @Bind(R2.id.iv_pic_product)
        ImageView ivPicProduct;
        @Bind(R2.id.tv_name_product)
        TextView tvNameProduct;
        @Bind(R2.id.tv_price_product)
        TextView tvPriceProduct;
        @Bind(R2.id.tv_weight_product)
        TextView tvWeightProduct;
        @Bind(R2.id.tv_preorder_period)
        TextView tvPreorderPeriod;
        @Bind(R2.id.et_quantity_product)
        EditText etQuantityProduct;
        @Bind(R2.id.et_notes_product)
        EditText etNotesProduct;

        ProductItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
