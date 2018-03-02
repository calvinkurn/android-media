package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.transaction.checkout.view.viewholder.CartItemViewHolder;

import java.util.List;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public class InnerProductListAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private static final String TAG = InnerProductListAdapter.class.getSimpleName();

    private List<CartItemModel> mProductList;

    public InnerProductListAdapter(List<CartItemModel> cartItemModels) {
        mProductList = cartItemModels;
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_detail, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartItemViewHolder holder, int position) {
        CartItemModel cartItemModel = mProductList.get(position);
        holder.bindViewHolder(cartItemModel);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

}
