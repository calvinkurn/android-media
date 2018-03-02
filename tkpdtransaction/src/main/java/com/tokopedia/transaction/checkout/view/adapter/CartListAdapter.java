package com.tokopedia.transaction.checkout.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.design.voucher.VoucherCartHachikoView;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemTickerErrorHolderData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VIEW_ITEM_CART = R.layout.holder_item_cart_new;
    private static final int TYPE_VIEW_PROMO_SUGGESTION = R.layout.holder_item_cart_potential_promo;
    private static final int TYPE_VIEW_PROMO = R.layout.holder_item_cart_promo;
    private static final int TYPE_VIEW_TICKER_CART_ERROR = R.layout.holder_item_cart_ticker_error;

    private final ActionListener actionListener;
    private List<Object> cartItemHolderDataList;

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
        } else if (viewType == TYPE_VIEW_PROMO) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(TYPE_VIEW_PROMO, parent, false);
            return new CartPromoHolder(view);
        } else if (viewType == TYPE_VIEW_TICKER_CART_ERROR) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(TYPE_VIEW_TICKER_CART_ERROR, parent, false);
            return new CartTickerErrorHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
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

            if (TextUtils.isEmpty(data.getCartItemData().getUpdatedData().getRemark())
                    && !data.isEditableRemark()) {
                holderView.etRemark.setVisibility(View.GONE);
                holderView.tvLabelRemarkOption.setVisibility(View.VISIBLE);
                holderView.lineBottom.setVisibility(View.VISIBLE);
            } else {
                holderView.etRemark.setVisibility(View.VISIBLE);
                holderView.tvLabelRemarkOption.setVisibility(View.GONE);
                holderView.lineBottom.setVisibility(View.GONE);
                holderView.etRemark.setText(data.getCartItemData().getUpdatedData().getRemark());
            }

            holderView.ivProductImage.setOnClickListener(getOnClickProductItemListener(position, data));
            holderView.tvProductName.setOnClickListener(getOnClickProductItemListener(position, data));

            holderView.tvShopName.setOnClickListener(getOnClickShopItemListener(position, data));

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

            holderView.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onCartItemDeleteButtonClicked(data, position);
                }
            });

            renderErrorFormItemValidation(data, holderView, position);
            renderErrorItemHeader(data, holderView, position);

            holderView.etRemark.addTextChangedListener(new RemarkTextWatcher(position));
            holderView.etQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int qty = 0;
                    try {
                        qty = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    data.getCartItemData().getUpdatedData().setQuantity(qty);
                    actionListener.onCartItemQuantityFormEdited();
                    renderErrorFormItemValidation(data, holderView, position);
                }
            });

            if (data.getCartItemData().getOriginData().isFavorite()) {
                holderView.ivWishlistBadge.setImageResource(R.drawable.ic_wishlist_red);
            } else {
                holderView.ivWishlistBadge.setImageResource(R.drawable.ic_wishlist);
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
        } else if (getItemViewType(position) == TYPE_VIEW_PROMO) {
            final CartPromoHolder holderView = (CartPromoHolder) holder;
            final CartItemPromoHolderData data = (CartItemPromoHolderData) cartItemHolderDataList.get(position);
            holderView.voucherCartHachikoView.setActionListener(new VoucherCartHachikoView.ActionListener() {
                @Override
                public void onClickUseVoucher() {
                    actionListener.onCartPromoUseVoucherPromoClicked(data, position);
                }

                @Override
                public void disableVoucherDisount() {
                    actionListener.onCartPromoCancelVoucherPromoClicked(data, position);
                }

                @Override
                public void trackingSuccessVoucher(String voucherName) {
                    actionListener.onCartPromoTrackingSuccess(data, position);
                }

                @Override
                public void trackingCancelledVoucher() {
                    actionListener.onCartPromoTrackingCancelled(data, position);
                }
            });
            if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON) {
                holderView.voucherCartHachikoView.setCoupon(
                        data.getCouponTitle(), data.getCouponMessage(), data.getCouponCode()
                );
            } else if (data.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER) {
                holderView.voucherCartHachikoView.setVoucher(
                        data.getVoucherCode(), data.getVoucherMessage()
                );
            } else {
                holderView.voucherCartHachikoView.setPromoAndCouponLabel();
                holderView.voucherCartHachikoView.resetView();
            }
        } else if (getItemViewType(position) == TYPE_VIEW_TICKER_CART_ERROR) {
            final CartTickerErrorHolder holderView = (CartTickerErrorHolder) holder;
            final CartItemTickerErrorHolderData data = (CartItemTickerErrorHolderData) cartItemHolderDataList.get(position);

            holderView.tvErrorMessage.setText(data.getCartTickerErrorData().getErrorInfo());
            holderView.tvBtnAction.setText(data.getCartTickerErrorData().getActionInfo());
            holderView.tvBtnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.onCartItemTickerErrorActionClicked(data, position);
                }
            });
        }
    }

    @NonNull
    private View.OnClickListener getOnClickShopItemListener(@SuppressLint("RecyclerView") final int position, final CartItemHolderData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemShopNameClicked(data, position);
            }
        };
    }

    @NonNull
    private View.OnClickListener getOnClickProductItemListener(@SuppressLint("RecyclerView") final int position, final CartItemHolderData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemProductClicked(data, position);
            }
        };
    }

    private void renderErrorFormItemValidation(CartItemHolderData data, CartItemHolder holderView, int position) {
        if (data.getErrorFormItemValidationType() == CartItemHolderData.ERROR_EMPTY) {
            holderView.errorContainer.setVisibility(View.GONE);
            holderView.tvError.setVisibility(View.GONE);
            holderView.tvErrorDetail.setVisibility(View.GONE);

            holderView.tvErrorFormValidation.setText("");
            holderView.tvErrorFormValidation.setVisibility(View.GONE);
        } else {
            holderView.errorContainer.setVisibility(View.VISIBLE);
            holderView.tvError.setVisibility(View.VISIBLE);
            holderView.tvErrorDetail.setVisibility(View.GONE);
            holderView.tvError.setText(data.getErrorFormItemValidationMessage());

            holderView.tvErrorFormValidation.setText(data.getErrorFormItemValidationMessage());
            holderView.tvErrorFormValidation.setVisibility(View.VISIBLE);
        }
    }

    private void renderErrorItemHeader(CartItemHolderData data, CartItemHolder holderView, int position) {
        if (data.getCartItemData().isError()) {
            holderView.errorContainer.setBackgroundResource(R.color.bg_cart_item_error);
            holderView.tvError.setTextColor(MainApplication.getAppContext().getResources().getColor(R.color.text_cart_item_error_red));
            holderView.errorContainer.setVisibility(View.VISIBLE);
            holderView.tvError.setVisibility(View.VISIBLE);
            holderView.tvErrorDetail.setVisibility(View.GONE);
            holderView.tvError.setText(data.getCartItemData().getErrorMessage());
        } else if (data.getCartItemData().isWarning()) {
            holderView.errorContainer.setBackgroundResource(R.color.bg_cart_item_warning);
            holderView.tvError.setTextColor(MainApplication.getAppContext().getResources().getColor(R.color.black_54));
            holderView.errorContainer.setVisibility(View.VISIBLE);
            holderView.tvError.setVisibility(View.VISIBLE);
            holderView.tvErrorDetail.setVisibility(View.GONE);
            holderView.tvError.setText(data.getCartItemData().getWarningMessage());
        } else {
            holderView.errorContainer.setVisibility(View.GONE);
            holderView.tvError.setVisibility(View.GONE);
            holderView.tvErrorDetail.setVisibility(View.GONE);

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
            cartItemHolderData.setErrorFormItemValidationMessage("");
            cartItemHolderData.setEditableRemark(false);
            cartItemHolderDataList.add(cartItemHolderData);
        }
        notifyDataSetChanged();
    }

    public List<CartItemHolderData> getDataList() {
        List<CartItemHolderData> cartItemHolderDataFinalList = new ArrayList<>();
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object object = cartItemHolderDataList.get(i);
            if (object instanceof CartItemHolderData) {
                cartItemHolderDataFinalList.add((CartItemHolderData) object);
            }
        }
        return cartItemHolderDataFinalList;
    }

    public List<CartItemData> getCartItemDataList() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (Object object : cartItemHolderDataList) {
            if (object instanceof CartItemHolderData) {
                CartItemHolderData cartItemHolderData = (CartItemHolderData) object;
                cartItemDataList.add(cartItemHolderData.getCartItemData());
            }
        }

        return cartItemDataList;
    }

    public void increaseQuantity(int position) {
        if (getItemViewType(position) == TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().increaseQuantity();
        }
        notifyItemChanged(position);
    }

    public void decreaseQuantity(int position) {
        if (getItemViewType(position) == TYPE_VIEW_ITEM_CART) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().decreaseQuantity();
        }
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (cartItemHolderDataList.get(position) instanceof CartItemHolderData) {
            return TYPE_VIEW_ITEM_CART;
        } else if (cartItemHolderDataList.get(position) instanceof CartPromoSuggestion) {
            return TYPE_VIEW_PROMO_SUGGESTION;
        } else if (cartItemHolderDataList.get(position) instanceof CartItemPromoHolderData) {
            return TYPE_VIEW_PROMO;
        } else if (cartItemHolderDataList.get(position) instanceof CartItemTickerErrorHolderData) {
            return TYPE_VIEW_TICKER_CART_ERROR;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void addPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        cartItemHolderDataList.add(cartPromoSuggestion);
        notifyDataSetChanged();
    }

    public void deleteItem(List<CartItemData> cartItemDataList) {
        cartItemHolderDataList.removeAll(cartItemDataList);
        notifyDataSetChanged();
    }

    public void resetData() {
        cartItemHolderDataList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(CartItemData cartItemData) {
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object data = cartItemHolderDataList.get(i);
            if (data instanceof CartItemHolderData) {
                if (((CartItemHolderData) data).getCartItemData().getOriginData().getCartId()
                        == cartItemData.getOriginData().getCartId()) {
                    cartItemHolderDataList.remove(i);
                    notifyItemRemoved(i);
                }
            }
        }

        if (getDataList().isEmpty()) actionListener.onCartItemListIsEmpty();
    }

    public void updateItemPromoVoucher(CartItemPromoHolderData cartItemPromoHolderData) {
        for (int i = 0; i < cartItemHolderDataList.size(); i++) {
            Object object = cartItemHolderDataList.get(i);
            if (object instanceof CartItemPromoHolderData) {
                cartItemHolderDataList.set(i, cartItemPromoHolderData);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void addPromoVoucherData(CartItemPromoHolderData cartItemPromoHolderData) {
        cartItemHolderDataList.add(cartItemPromoHolderData);
        notifyDataSetChanged();
    }

    public void addCartTickerError(CartItemTickerErrorHolderData cartItemTickerErrorHolderData) {
        cartItemHolderDataList.add(cartItemTickerErrorHolderData);
        notifyDataSetChanged();
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

        void onCartItemListIsEmpty();

        void onCartPromoUseVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position);

        void onCartPromoCancelVoucherPromoClicked(CartItemPromoHolderData cartItemPromoHolderData, int position);

        void onCartPromoTrackingSuccess(CartItemPromoHolderData cartItemPromoHolderData, int position);

        void onCartPromoTrackingCancelled(CartItemPromoHolderData cartItemPromoHolderData, int position);

        void onCartItemQuantityFormEdited();

        void onCartItemTickerErrorActionClicked(CartItemTickerErrorHolderData data, int position);
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductWeight;
        private TextView tvShopName;
        private AppCompatEditText etQty;
        private ImageView btnQtyPlus;
        private ImageView btnQtyMinus;
        private TextView tvInfoRFreeReturn;
        private TextView tvInfoPreOrder;
        private TextView tvInfoCashBack;
        private AppCompatEditText etRemark;
        private TextView tvLabelRemarkOption;
        private ImageView btnDelete;
        private ImageView ivWishlistBadge;
        private TextView tvErrorFormValidation;

        private LinearLayout errorContainer;
        private TextView tvError;
        private TextView tvErrorDetail;
        private View lineBottom;

        CartItemHolder(View itemView) {
            super(itemView);
            this.tvErrorFormValidation = itemView.findViewById(R.id.tv_error_form_validation);
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
            this.ivWishlistBadge = itemView.findViewById(R.id.iv_image_wishlist);
            this.errorContainer = itemView.findViewById(R.id.ll_warning_container);
            this.tvError = itemView.findViewById(R.id.tv_warning);
            this.tvErrorDetail = itemView.findViewById(R.id.tv_warning_detail);
            this.lineBottom = itemView.findViewById(R.id.line_bottom_remark);
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

    public class CartPromoHolder extends RecyclerView.ViewHolder {
        private VoucherCartHachikoView voucherCartHachikoView;

        CartPromoHolder(View itemView) {
            super(itemView);
            this.voucherCartHachikoView = itemView.findViewById(R.id.voucher_cart_holder_view);
        }
    }


    public class CartTickerErrorHolder extends RecyclerView.ViewHolder {
        private TextView tvErrorMessage;
        private TextView tvBtnAction;

        CartTickerErrorHolder(View itemView) {
            super(itemView);
            this.tvErrorMessage = itemView.findViewById(R.id.tv_error);
            this.tvBtnAction = itemView.findViewById(R.id.btn_error_action);
        }
    }

    private class RemarkTextWatcher implements TextWatcher {
        private final int position;

        RemarkTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            ((CartItemHolderData) cartItemHolderDataList.get(position))
                    .getCartItemData().getUpdatedData().setRemark(s.toString());
        }
    }
}
