package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;

import com.tokopedia.transaction.checkout.view.data.CartProductData;
import java.util.List;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 5/02/18
 */
public class CartRemoveProductAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_REMOVE_ALL_CHECKBOX =
            R.layout.view_item_remove_all_checkbox;
    private static final int ITEM_CART_REMOVE_PRODUCT =
            R.layout.item_cart_remove_product;
    
    private static final int TOP_POSITION = 0;

    private Context mContext;
    private List<CartProductData> mCartProductDataList;

    public CartRemoveProductAdapter() {

    }

    public void updateData(List<CartProductData> cartProductDataList) {
        mCartProductDataList = cartProductDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(viewType, viewGroup, false);

        if (viewType == ITEM_VIEW_REMOVE_ALL_CHECKBOX) {
            return new SelectRemoveAllCheckboxViewHolder(view);
        } else {
            return new CartProductDataViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == ITEM_VIEW_REMOVE_ALL_CHECKBOX) {
            ((SelectRemoveAllCheckboxViewHolder)viewHolder).bindViewHolder();
        } else {
            ((CartProductDataViewHolder)viewHolder).bindViewHolder();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TOP_POSITION) {
            return ITEM_VIEW_REMOVE_ALL_CHECKBOX;
        } else {
            return ITEM_CART_REMOVE_PRODUCT;
        }
    }

    class SelectRemoveAllCheckboxViewHolder extends RecyclerView.ViewHolder {

        SelectRemoveAllCheckboxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {

        }

    }

    class CartProductDataViewHolder extends RecyclerView.ViewHolder {

        CartProductDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {

        }

    }

}
