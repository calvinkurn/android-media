package com.tokopedia.transaction.checkout.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
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
    private boolean onBind;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        onBind = true;
        CartItemHolder holderView = (CartItemHolder) holder;
        final CartItemHolderData data = cartItemHolderDataList.get(position);

        holderView.tvShopName.setText(data.getCartItemData().getOriginData().getShopName());
        holderView.tvProductName.setText(data.getCartItemData().getOriginData().getProductName());
        holderView.tvProductPrice.setText(data.getCartItemData().getOriginData().getPriceFormatted());
        holderView.tvProductWeight.setText(data.getCartItemData().getOriginData().getWeightFormatted());
        holderView.etQty.setText(String.valueOf(data.getCartItemData().getUpdatedData().getQuantity()));
        ImageHandler.loadImageRounded2(
                holderView.itemView.getContext(), holderView.ivProductImage,
                data.getCartItemData().getOriginData().getProductImage()
        );


        if (!TextUtils.isEmpty(data.getCartItemData().getUpdatedData().getRemark())) {
            holderView.etRemark.setVisibility(View.VISIBLE);
        } else {
            holderView.etRemark.setVisibility(View.GONE);
        }

        holderView.etRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    actionListener.onCartItemRemarkEditChange(
                            data.getCartItemData(), position, textView.getText().toString()
                    );
                    return true;
                }
                return false;
            }
        });
//        holderView.etRemark.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                actionListener.onCartItemRemarkEditChange(data.getCartItemData(), position, editable.toString());
//            }
//        });

        holderView.tvActionRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemActionRemarkClicked(data, position);
            }
        });
        if (data.isEditableRemark()) {
            holderView.tvActionRemark.setVisibility(View.VISIBLE);
            holderView.etRemark.setEnabled(false);
        } else {
            holderView.tvActionRemark.setVisibility(View.GONE);
            holderView.etRemark.setEnabled(true);
        }
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


        onBind = false;
    }

    @Override
    public int getItemCount() {
        return cartItemHolderDataList.size();
    }

    public void addDataList(List<CartItemData> cartItemDataList) {
        for (CartItemData cartItemData : cartItemDataList) {
            CartItemHolderData cartItemHolderData = new CartItemHolderData();
            cartItemHolderData.setCartItemData(cartItemData);
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderData.setErrorItem(false);
            cartItemHolderData.setMessageError("");
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderDataList.add(cartItemHolderData);
        }
        notifyDataSetChanged();
    }

    public void updateEditableRemark(int position) {
        cartItemHolderDataList.get(0).setEditableRemark(true);
        if (onBind) notifyItemChanged(position);
    }

    public void updateRemark(int position, String remark) {
        cartItemHolderDataList.get(0).getCartItemData().getUpdatedData().setRemark(remark);
        if (!onBind) notifyItemChanged(position);
    }

    public List<CartItemHolderData> getDataList() {
        return cartItemHolderDataList;
    }


    public interface ActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemActionRemarkClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark);
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
            this.etQty = itemView.findViewById(R.id.et_qty);
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
