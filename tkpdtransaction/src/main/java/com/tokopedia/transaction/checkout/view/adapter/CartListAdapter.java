package com.tokopedia.transaction.checkout.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VIEW_ITEM_CART = R.layout.holder_item_cart_new;
    private static final int TYPE_VIEW_PROMO_SUGGESTION = R.layout.holder_item_cart_potential_promo;

    private final ActionListener actionListener;
    private List<Object> cartItemHolderDataList;
    private boolean onBind;

    @Inject
    public CartListAdapter(ActionListener actionListener) {
        this.cartItemHolderDataList = new ArrayList<>();
        this.actionListener = actionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW_ITEM_CART) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(TYPE_VIEW_ITEM_CART, parent, false);
            return new CartItemHolder(view);
        } else if (viewType == TYPE_VIEW_PROMO_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(TYPE_VIEW_PROMO_SUGGESTION, parent, false);
            return new CartPromoSuggestionHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        onBind = true;
        if (getItemViewType(position) == TYPE_VIEW_ITEM_CART) {
            final CartItemHolder holderView = (CartItemHolder) holder;
            final CartItemHolderData data = (CartItemHolderData) cartItemHolderDataList.get(position);

            holderView.tvShopName.setText(data.getCartItemData().getOriginData().getShopName());
            holderView.tvProductName.setText(data.getCartItemData().getOriginData().getProductName());
            holderView.tvProductPrice.setText(data.getCartItemData().getOriginData().getPriceFormatted());
            holderView.tvProductWeight.setText(data.getCartItemData().getOriginData().getWeightFormatted());
            holderView.etQty.setText(String.valueOf(data.getCartItemData().getUpdatedData().getQuantity()));
            ImageHandler.loadImageRounded2(
                    holderView.itemView.getContext(), holderView.ivProductImage,
                    data.getCartItemData().getOriginData().getProductImage()
            );
            holderView.etRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        actionListener.onCartItemRemarkEditChange(
                                data.getCartItemData(), position, textView.getText().toString()
                        );
                        return true;
                    }
                    return false;
                }
            });

            holderView.tvLabelRemarkOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holderView.etRemark.setVisibility(View.VISIBLE);
                    holderView.tvLabelRemarkOption.setVisibility(View.GONE);
                }
            });

            if (TextUtils.isEmpty(data.getCartItemData().getUpdatedData().getRemark())) {
                holderView.etRemark.setVisibility(View.GONE);
                holderView.tvLabelRemarkOption.setVisibility(View.VISIBLE);
            } else {
                holderView.etRemark.setVisibility(View.VISIBLE);
                holderView.tvLabelRemarkOption.setVisibility(View.GONE);
                holderView.etRemark.setText(data.getCartItemData().getUpdatedData().getRemark());
            }

            holderView.tvInfoRFreeReturn.setVisibility(
                    data.getCartItemData().getOriginData().isFreeReturn() ? View.VISIBLE : View.GONE
            );
            holderView.tvInfoPreOrder.setVisibility(
                    data.getCartItemData().getOriginData().isPreOrder() ? View.VISIBLE : View.GONE
            );
            holderView.tvInfoCashBack.setVisibility(
                    data.getCartItemData().getOriginData().isCashBack() ? View.VISIBLE : View.GONE
            );
            holderView.tvInfoCashBack.setText(data.getCartItemData().getOriginData().getCashBackInfo());


            holderView.btnQtyPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onCartItemQuantityPlusButtonClicked(data, position);
                }
            });

            holderView.btnQtyMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onCartItemQuantityMinusButtonClicked(data, position);
                }
            });
            renderErrorWarning(data, holderView, position);
            if (!data.getCartItemData().getUpdatedData().getRemark()
                    .equalsIgnoreCase(holderView.etRemark.getText().toString())) {
                ((CartItemHolderData) cartItemHolderDataList.get(position)).getCartItemData().getUpdatedData().setRemark(
                        holderView.etRemark.getText().toString()
                );
            }


        } else if (getItemViewType(position) == TYPE_VIEW_PROMO_SUGGESTION) {
            final CartPromoSuggestionHolder holderView = (CartPromoSuggestionHolder) holder;
            final CartPromoSuggestion data = (CartPromoSuggestion) cartItemHolderDataList.get(position);
            holderView.tvDesc.setText(data.getText());
            holderView.tvAction.setText(data.getCta());
            holderView.tvAction.setTextColor(Color.parseColor(data.getCtaColor()));
            holderView.tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onCartPromoSuggestionActionClicked(data, position);
                }
            });
            holderView.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onCartPromoSuggestionButtonCloseClicked(data, position);
                }
            });
        }

        onBind = false;
    }

    private void renderErrorWarning(CartItemHolderData data, CartItemHolder holderView, int position) {
        if (data.getErrorType() == CartItemHolderData.ERROR_EMPTY) {
            holderView.errorContainer.setVisibility(View.GONE);
            holderView.tvError.setVisibility(View.GONE);
            holderView.tvErrorDetail.setVisibility(View.GONE);
        } else {
            holderView.errorContainer.setVisibility(View.VISIBLE);
            holderView.tvError.setVisibility(View.VISIBLE);
            holderView.tvErrorDetail.setVisibility(View.GONE);
            holderView.tvError.setText(data.getMessageError());
        }
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

    public List<CartItemHolderData> getDataList() {
        List<CartItemHolderData> cartItemHolderDataFinalList = new ArrayList<>();
        for (Object object : cartItemHolderDataList) {
            if (object instanceof CartItemHolderData) cartItemHolderDataFinalList.add(
                    (CartItemHolderData) object
            );
        }
        return cartItemHolderDataFinalList;
    }

    public void increaseQuantity(int position) {
        if (getItemViewType(position) == TYPE_VIEW_ITEM_CART)
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().increaseQuantity();
        notifyItemChanged(position);
    }

    public void decreaseQuantity(int position) {
        if (getItemViewType(position) == TYPE_VIEW_ITEM_CART)
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().decreaseQuantity();
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (cartItemHolderDataList.get(position) instanceof CartItemHolderData) {
            return TYPE_VIEW_ITEM_CART;
        } else if (cartItemHolderDataList.get(position) instanceof CartPromoSuggestion) {
            return TYPE_VIEW_PROMO_SUGGESTION;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void addPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        cartItemHolderDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        cartItemHolderDataList.remove(position);
        notifyItemRemoved(position);
    }

    public interface ActionListener {

        void onCartItemDeleteButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityPlusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemQuantityMinusButtonClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemProductClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemShopNameClicked(CartItemHolderData cartItemHolderData, int position);

        void onCartItemRemarkEditChange(CartItemData cartItemData, int position, String remark);

        void onCartPromoSuggestionActionClicked(CartPromoSuggestion data, int position);

        void onCartPromoSuggestionButtonCloseClicked(CartPromoSuggestion data, int position);
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
        private TextView tvLabelRemarkOption;
        private ImageView btnDelete;

        private LinearLayout errorContainer;
        private TextView tvError;
        private TextView tvErrorDetail;

        CartItemHolder(View itemView) {
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
            this.tvLabelRemarkOption = itemView.findViewById(R.id.tv_label_remark_option);
            this.etRemark = itemView.findViewById(R.id.et_remark);
            this.btnDelete = itemView.findViewById(R.id.btn_delete_cart);


            this.errorContainer = itemView.findViewById(R.id.ll_warning_container);
            this.tvError = itemView.findViewById(R.id.tv_warning);
            this.tvErrorDetail = itemView.findViewById(R.id.tv_warning_detail);
        }
    }


    public class CartPromoSuggestionHolder extends RecyclerView.ViewHolder {

        private ImageView btnClose;
        private TextView tvDesc;
        private TextView tvAction;

        CartPromoSuggestionHolder(View itemView) {
            super(itemView);
            this.btnClose = itemView.findViewById(R.id.btn_close);
            this.tvAction = itemView.findViewById(R.id.tv_action);
            this.tvDesc = itemView.findViewById(R.id.tv_desc);
        }
    }


}
