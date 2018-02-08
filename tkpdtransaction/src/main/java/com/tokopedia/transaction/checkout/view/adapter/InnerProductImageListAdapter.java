package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;

import java.util.List;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public class InnerProductImageListAdapter
        extends RecyclerView.Adapter<InnerProductImageListAdapter.CartItemViewHolder> {

    private static final String TAG = InnerProductImageListAdapter.class.getSimpleName();

    private List<CartItemModel> mProductList;
    private Context mContext;

    public InnerProductImageListAdapter(List<CartItemModel> cartItemModels) {
        mProductList = cartItemModels;
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_thumb_image, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartItemViewHolder holder, int position) {
        CartItemModel cartItemModel = mProductList.get(position);

        ImageHandler.LoadImage(holder.mIvProductImage, cartItemModel.getProductImageUrl());
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvProductImage;

        CartItemViewHolder(View view) {
            super(view);

            mIvProductImage = view.findViewById(R.id.iv_product_thumb_image);
        }

    }

}
