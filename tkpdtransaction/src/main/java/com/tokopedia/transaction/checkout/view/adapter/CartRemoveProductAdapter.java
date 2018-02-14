package com.tokopedia.transaction.checkout.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.transaction.R;

import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import java.util.List;

import butterknife.BindView;
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

    private CartRemoveProductActionListener mActionListener;

    private Context mContext;
    private List<CartItemData> mCartItemModelList;

    private boolean isRemoveAll;

    public CartRemoveProductAdapter(CartRemoveProductActionListener actionListener) {
        mActionListener = actionListener;
        isRemoveAll = false;

    }

    public void updateData(List<CartItemData> cartItemModels) {
        mCartItemModelList = cartItemModels;
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
            int pos = position - 1;
            ((CartProductDataViewHolder)viewHolder).bindViewHolder(mCartItemModelList.get(pos), pos);
        }
    }

    @Override
    public int getItemCount() {
        return mCartItemModelList.size() + 1;
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

        @BindView(R2.id.checkBox)
        CheckBox mCbRemoveAll;

        SelectRemoveAllCheckboxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder() {
            mCbRemoveAll.setOnClickListener(checkBoxClickedListener());
        }

        private View.OnClickListener checkBoxClickedListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isRemoveAll = !isRemoveAll;
                    notifyDataSetChanged();
                }
            };
        }

    }

    /**
     * To be implemented by container fragment which will receive the data from adapter
     */
    public interface CartRemoveProductActionListener {

        /**
         * Executed when state of checkbox is changed
         * @param state boolean state of checked on unchecked
         * @param position index of list where the item is checked
         */
        void onCheckBoxStateChangedListener(boolean state, int position);

    }

    class CartProductDataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.cb_remove_product)
        CheckBox mCbRemoveProduct;
        @BindView(R2.id.tv_sender_name)
        TextView mTvSenderName;
        @BindView(R2.id.iv_product_image_container)
        ImageView mIvProductImage;
        @BindView(R2.id.tv_shipping_product_name)
        TextView mTvProductName;
        @BindView(R2.id.tv_shipped_product_price)
        TextView mTvProductPrice;
        @BindView(R2.id.tv_product_weight)
        TextView mTvProductWeight;
        @BindView(R2.id.tv_total_product_item)
        TextView mTvTotalProductItem;

        int position;
        boolean isChecked = false;

        CartProductDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindViewHolder(CartItemData cartItemModel, int position) {
            this.position = position;

            CartItemData.OriginData originData = cartItemModel.getOriginData();
            CartItemData.UpdatedData updatedData = cartItemModel.getUpdatedData();

            mCbRemoveProduct.setChecked(isRemoveAll || isChecked);
            mCbRemoveProduct.setOnClickListener(checkBoxClickedListener());
            mCbRemoveProduct.setOnCheckedChangeListener(onChangeStateListener(position));

            mTvSenderName.setText(originData.getShopName());
            mTvProductName.setText(originData.getProductName());
            mTvProductPrice.setText(originData.getPriceFormatted());
            mTvProductWeight.setText(originData.getWeightFormatted());
            mTvTotalProductItem.setText(String.valueOf(updatedData.getQuantity()));
            ImageHandler.LoadImage(mIvProductImage, originData.getProductImage());
        }

        private CompoundButton.OnCheckedChangeListener onChangeStateListener(final int position) {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                    mActionListener.onCheckBoxStateChangedListener(state, position);
                }
            };
        }

        private View.OnClickListener checkBoxClickedListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isChecked = !isChecked;
                    mCbRemoveProduct.setChecked(isRemoveAll || isChecked);
                }
            };
        }

    }

}
