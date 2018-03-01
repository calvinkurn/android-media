package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public class InnerProductListAdapter
        extends RecyclerView.Adapter<InnerProductListAdapter.CartItemViewHolder> {

    private static final String TAG = InnerProductListAdapter.class.getSimpleName();

    private static final NumberFormat CURRENCY_RUPIAH =
            NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

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

        ImageHandler.LoadImage(holder.mIvProductImage, cartItemModel.getImageUrl());
        holder.mTvProductName.setText(cartItemModel.getName());
        holder.mTvProductPrice.setText(CURRENCY_RUPIAH.format(cartItemModel.getPrice()));
        holder.mTvProductWeight.setText(getTotalWeightFormat(cartItemModel.getWeight(),
                cartItemModel.getWeightUnit()));
        holder.mTvTotalProductItem.setText(String.valueOf(cartItemModel.getQuantity()));
        holder.mTvOptionalNote.setText(cartItemModel.getNoteToSeller());

        holder.mRlProductPoliciesContainer.setVisibility(getPoliciesVisibility(
                cartItemModel.isCashback(),
                cartItemModel.isFreeReturn(),
                cartItemModel.isPreOrder()));
        holder.mIvFreeReturnIcon.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        holder.mTvFreeReturnText.setVisibility(cartItemModel.isFreeReturn() ? View.VISIBLE : View.GONE);
        holder.mTvPoSign.setVisibility(cartItemModel.isPreOrder() ? View.VISIBLE : View.GONE);
        holder.mTvCashback.setVisibility(cartItemModel.isCashback() ? View.VISIBLE : View.GONE);
        holder.mTvCashback.setText(cartItemModel.getCashback());

        boolean isEmptyNotes = TextUtils.isEmpty(cartItemModel.getNoteToSeller());

        holder.mLlNoteToSellerLayout.setVisibility(isEmptyNotes ? View.GONE : View.VISIBLE);
        holder.mTvOptionalNote.setText(cartItemModel.getNoteToSeller());
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    private String getTotalWeightFormat(double totalWeight, int weightUnit) {
        String unit = weightUnit == 0 ? "Kg" : "g";
        return String.format("%s %s", (int) totalWeight, unit);
    }

    private int getPoliciesVisibility(boolean isCashback,
                                      boolean isFreeReturn,
                                      boolean isPreOrder) {

        return !isCashback && !isFreeReturn && !isPreOrder ? View.GONE : View.VISIBLE;
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.iv_product_image_container) ImageView mIvProductImage;
        @BindView(R2.id.tv_shipping_product_name) TextView mTvProductName;
        @BindView(R2.id.tv_shipped_product_price) TextView mTvProductPrice;
        @BindView(R2.id.tv_product_weight) TextView mTvProductWeight;
        @BindView(R2.id.tv_total_product_item) TextView mTvTotalProductItem;
        @BindView(R2.id.tv_optional_note_to_seller) TextView mTvOptionalNote;

        @BindView(R2.id.rl_product_policies_layout) RelativeLayout mRlProductPoliciesContainer;
        @BindView(R2.id.iv_free_return_icon) ImageView mIvFreeReturnIcon;
        @BindView(R2.id.tv_free_return_text) TextView mTvFreeReturnText;
        @BindView(R2.id.tv_po_sign) TextView mTvPoSign;
        @BindView(R2.id.tv_cashback_text) TextView mTvCashback;
        @BindView(R2.id.ll_note_to_seller) LinearLayout mLlNoteToSellerLayout;

        CartItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
