package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ActionListener actionListener;
    private List<CartItemHolderData> cartItemHolderDataList;

    @Inject
    public CartListAdapter(ActionListener actionListener) {
        this.cartItemHolderDataList = new ArrayList<>();
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item_cart_new, parent, false);
        return new CartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartItemHolder holderView = (CartItemHolder) holder;
        CartItemHolderData data = cartItemHolderDataList.get(position);

        holderView.tvShopName.setText(data.getCartItemData().getOriginData().getShopName());
        holderView.tvProductName.setText(data.getCartItemData().getOriginData().getProductName());
        holderView.tvProductPrice.setText(data.getCartItemData().getOriginData().getPriceFormatted());
        holderView.tvProductWeight.setText(data.getCartItemData().getOriginData().getWeightFormatted());
        holderView.etQty.setText(data.getCartItemData().getUpdatedData().getQuantity());
        holderView.etRemark.setText(data.getCartItemData().getUpdatedData().getRemark());

        holderView.tvInfoRFreeReturn.setVisibility(
                data.getCartItemData().getOriginData().isFreeReturn() ? View.VISIBLE : View.GONE
        );
        holderView.tvInfoPreOrder.setVisibility(
                data.getCartItemData().getOriginData().isPreOrder() ? View.VISIBLE : View.GONE
        );
        holderView.tvInfoCashBack.setVisibility(
                data.getCartItemData().getOriginData().isCashBack() ? View.VISIBLE : View.GONE
        );
        holderView.tvInfoRFreeReturn.setText(data.getCartItemData().getOriginData().getCashBackInfo());


    }

    @Override
    public int getItemCount() {
        return cartItemHolderDataList.size();
    }

    public interface ActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemActionRemarkClicked(CartItemHolderData cartItemHolderData, int position);

    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductWeight;
        private TextView tvShopName;
        private EditText etQty;
        private ImageView btnQtyPlus;
        private ImageView btnQtyMinus;
        private TextView tvInfoRFreeReturn;
        private TextView tvInfoPreOrder;
        private TextView tvInfoCashBack;
        private EditText etRemark;
        private TextView tvActionRemark;
        private ImageView btnDelete;

        public CartItemHolder(View itemView) {
            super(itemView);
            this.ivProductImage = itemView.findViewById(R.id.iv_image_product);
            this.tvProductName = itemView.findViewById(R.id.tv_product_name);
            this.tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            this.tvProductWeight = itemView.findViewById(R.id.tv_product_weight);
            this.tvShopName = itemView.findViewById(R.id.tv_shop_name);
            this.etQty = itemView.findViewById(R.id.et_quantity_product);
            this.btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus);
            this.btnQtyMinus = itemView.findViewById(R.id.btn_qty_min);
            this.tvInfoRFreeReturn = itemView.findViewById(R.id.tv_info_free_return);
            this.tvInfoPreOrder = itemView.findViewById(R.id.tv_info_preorder);
            this.tvInfoCashBack = itemView.findViewById(R.id.tv_info_cashback);
            this.etRemark = itemView.findViewById(R.id.et_remark);
            this.tvActionRemark = itemView.findViewById(R.id.tv_action_form_remark);
            this.btnDelete = itemView.findViewById(R.id.btn_delete_cart);
        }
    }


}
