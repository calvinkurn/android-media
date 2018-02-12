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
public class InnerProductListAdapter
        extends RecyclerView.Adapter<InnerProductListAdapter.CartItemViewHolder> {

    private static final String TAG = InnerProductListAdapter.class.getSimpleName();

    private List<CartItemModel> mProductList;
    private Context mContext;

    public InnerProductListAdapter(List<CartItemModel> cartItemModels) {
        mProductList = cartItemModels;
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_detail, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CartItemViewHolder holder, int position) {
        CartItemModel cartItemModel = mProductList.get(position);

        ImageHandler.LoadImage(holder.mIvProductImage, cartItemModel.getProductImageUrl());
        holder.mTvProductName.setText(cartItemModel.getProductName());
        holder.mTvProductPrice.setText(cartItemModel.getProductPriceFormatted());
        holder.mTvProductWeight.setText(cartItemModel.getProductWeightFormatted());
        holder.mTvTotalProductItem.setText(String.valueOf(cartItemModel.getTotalProductItem()));
        holder.mTvOptionalNote.setText(cartItemModel.getNoteToSeller());

        holder.mRlProductPoliciesContainer.setVisibility(getPoliciesVisibility());
        holder.mIvFreeReturnIcon.setVisibility(getFreeReturnVisibility(cartItemModel.isFreeReturn()));
        holder.mTvFreeReturnText.setVisibility(getFreeReturnVisibility(cartItemModel.isFreeReturn()));
        holder.mTvPoSign.setVisibility(getPoVisibility(cartItemModel.isPoAvailable()));
        holder.mTvCashback.setVisibility(getCashbackVisibility(cartItemModel.getCashback()));
        holder.mTvCashback.setText(getCashback(cartItemModel.getCashback()));
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    private int getPoliciesVisibility() {
        return View.VISIBLE;
    }

    private int getFreeReturnVisibility(boolean isFreeReturn) {
        return isFreeReturn ? View.VISIBLE : View.GONE;
    }

    private int getPoVisibility(boolean isPoAvailable) {
        return isPoAvailable ? View.VISIBLE : View.GONE;
    }

    private int getCashbackVisibility(String cashback) {
        return cashback.equals("0%") ? View.GONE : View.VISIBLE;
    }

    private String getCashback(String cashback) {
        return "Cashback " + cashback;
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvProductImage;
        TextView mTvProductName;
        TextView mTvProductPrice;
        TextView mTvProductWeight;
        TextView mTvTotalProductItem;
        TextView mTvOptionalNote;

        RelativeLayout mRlProductPoliciesContainer;
        ImageView mIvFreeReturnIcon;
        TextView mTvFreeReturnText;
        TextView mTvPoSign;
        TextView mTvCashback;

        CartItemViewHolder(View view) {
            super(view);

            mIvProductImage = view.findViewById(R.id.iv_product_image_container);
            mTvProductName = view.findViewById(R.id.tv_shipping_product_name);
            mTvProductPrice = view.findViewById(R.id.tv_shipped_product_price);
            mTvProductWeight = view.findViewById(R.id.tv_product_weight);
            mTvTotalProductItem = view.findViewById(R.id.tv_total_product_item);
            mTvOptionalNote = view.findViewById(R.id.tv_optional_note_to_seller);

            mRlProductPoliciesContainer = view.findViewById(R.id.rl_product_policies_layout);
            mIvFreeReturnIcon = view.findViewById(R.id.iv_free_return_icon);
            mTvFreeReturnText = view.findViewById(R.id.tv_free_return_text);
            mTvPoSign = view.findViewById(R.id.tv_po_sign);
            mTvCashback = view.findViewById(R.id.tv_cashback_text);
        }

    }

}
