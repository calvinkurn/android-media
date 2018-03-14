package com.tokopedia.transaction.checkout.view.viewholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.adapter.CartListAdapter;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

/**
 * @author anggaprasetiyo on 13/03/18.
 */
public class CartListItemViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_VIEW_ITEM_CART = R.layout.holder_item_cart_new;
    private final CartListAdapter.ActionListener actionListener;
    private final Context context;

    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvProductWeight;
    private TextView tvShopName;
    private AppCompatEditText etQty;
    private ImageView btnQtyPlus;
    private ImageView btnQtyMinus;
    private ImageView ivIconFreeReturn;
    private TextView tvInfoRFreeReturn;
    private TextView tvInfoPreOrder;
    private TextView tvInfoCashBack;
    private AppCompatEditText etRemark;
    private TextView tvLabelRemarkOption;
    private ImageView btnDelete;
    private ImageView ivWishlistBadge;
    private TextView tvErrorFormValidation;
    private TextView tvErrorFormRemarkValidation;
    private LinearLayout errorContainer;
    private TextView tvError;
    private TextView tvErrorDetail;
    private View lineBottom;

    public CartListItemViewHolder(View itemView, CartListAdapter.ActionListener actionListener) {
        super(itemView);
        this.actionListener = actionListener;
        this.context = itemView.getContext();

        this.tvErrorFormValidation = itemView.findViewById(R.id.tv_error_form_validation);
        this.tvErrorFormRemarkValidation = itemView.findViewById(R.id.tv_error_form_remark_validation);
        this.ivProductImage = itemView.findViewById(R.id.iv_image_product);
        this.tvProductName = itemView.findViewById(R.id.tv_product_name);
        this.tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        this.tvProductWeight = itemView.findViewById(R.id.tv_product_weight);
        this.tvShopName = itemView.findViewById(R.id.tv_shop_name);
        this.etQty = itemView.findViewById(R.id.et_qty);
        this.btnQtyPlus = itemView.findViewById(R.id.btn_qty_plus);
        this.btnQtyMinus = itemView.findViewById(R.id.btn_qty_min);
        this.ivIconFreeReturn = itemView.findViewById(R.id.iv_free_return_icon);
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

    public void bindData(final CartItemHolderData data, final int position) {
        this.tvShopName.setText(
                Html.fromHtml(data.getCartItemData().getOriginData().getShopName())
        );
        this.tvProductName.setText(
                Html.fromHtml(data.getCartItemData().getOriginData().getProductName())
        );
        this.tvProductPrice.setText(data.getCartItemData().getOriginData().getPriceFormatted());
        this.tvProductWeight.setText(data.getCartItemData().getOriginData().getWeightFormatted());
        String quantity = String.valueOf(data.getCartItemData().getUpdatedData().getQuantity());

        this.etQty.setText(String.valueOf(data.getCartItemData().getUpdatedData().getQuantity()));
        if (quantity.length() > 0) {
            this.etQty.setSelection(quantity.length());
        }
        ImageHandler.loadImageRounded2(
                this.itemView.getContext(), this.ivProductImage,
                data.getCartItemData().getOriginData().getProductImage()
        );
        this.etRemark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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


        this.tvLabelRemarkOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etRemark.setVisibility(View.VISIBLE);
                tvLabelRemarkOption.setVisibility(View.GONE);
            }
        });

        if (TextUtils.isEmpty(data.getCartItemData().getUpdatedData().getRemark())
                && !data.isEditableRemark()) {
            this.etRemark.setVisibility(View.GONE);
            this.tvLabelRemarkOption.setVisibility(View.VISIBLE);
            this.lineBottom.setVisibility(View.VISIBLE);
        } else {
            this.etRemark.setVisibility(View.VISIBLE);
            this.tvLabelRemarkOption.setVisibility(View.GONE);
            this.lineBottom.setVisibility(View.GONE);
            this.etRemark.setText(data.getCartItemData().getUpdatedData().getRemark());
        }

        this.ivProductImage.setOnClickListener(getOnClickProductItemListener(position, data));
        this.tvProductName.setOnClickListener(getOnClickProductItemListener(position, data));

        this.tvShopName.setOnClickListener(getOnClickShopItemListener(position, data));

        if (data.getCartItemData().getOriginData().isFreeReturn()) {
            this.tvInfoRFreeReturn.setVisibility(View.VISIBLE);
            this.ivIconFreeReturn.setVisibility(View.VISIBLE);
            ImageHandler.loadImageRounded2(
                    this.itemView.getContext(), this.ivIconFreeReturn,
                    data.getCartItemData().getOriginData().getFreeReturnLogo()
            );
        } else {
            this.tvInfoRFreeReturn.setVisibility(View.GONE);
            this.ivIconFreeReturn.setVisibility(View.GONE);
        }

        this.tvInfoPreOrder.setVisibility(
                data.getCartItemData().getOriginData().isPreOrder() ? View.VISIBLE : View.GONE
        );

        this.tvInfoCashBack.setVisibility(
                data.getCartItemData().getOriginData().isCashBack() ? View.VISIBLE : View.GONE
        );

        this.tvInfoCashBack.setText(data.getCartItemData().getOriginData().getCashBackInfo());


        this.btnQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemQuantityPlusButtonClicked(data, position);
            }
        });

        this.btnQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemQuantityMinusButtonClicked(data, position);
            }
        });

        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemDeleteButtonClicked(data, position);
            }
        });

        renderErrorFormItemValidation(data);
        renderErrorItemHeader(data);


        this.etRemark.addTextChangedListener(new RemarkTextWatcher(data));
        this.etQty.addTextChangedListener(new QuantityTextWatcher(data));

        if (data.getCartItemData().getOriginData().isFavorite()) {
            this.ivWishlistBadge.setImageResource(R.drawable.ic_wishlist_red);
        } else {
            this.ivWishlistBadge.setImageResource(R.drawable.ic_wishlist);
        }
    }

    @NonNull
    private View.OnClickListener getOnClickProductItemListener(
            @SuppressLint("RecyclerView") final int position,
            final CartItemHolderData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemProductClicked(data, position);
            }
        };
    }

    @NonNull
    private View.OnClickListener getOnClickShopItemListener(
            @SuppressLint("RecyclerView") final int position,
            final CartItemHolderData data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCartItemShopNameClicked(data, position);
            }
        };
    }

    private void renderErrorFormItemValidation(CartItemHolderData data) {
        if (data.getErrorFormItemValidationType() == CartItemHolderData.ERROR_EMPTY) {
            this.tvErrorFormValidation.setText("");
            this.tvErrorFormValidation.setVisibility(View.GONE);
            this.tvErrorFormRemarkValidation.setVisibility(View.GONE);
            this.tvErrorFormRemarkValidation.setText("");
        } else {
            if (data.getErrorFormItemValidationType() == CartItemHolderData.ERROR_FIELD_MAX_CHAR) {
                this.tvErrorFormValidation.setText("");
                this.tvErrorFormValidation.setVisibility(View.GONE);
                this.tvErrorFormRemarkValidation.setVisibility(View.VISIBLE);
                this.tvErrorFormRemarkValidation.setText(data.getErrorFormItemValidationMessage());
            } else {
                this.tvErrorFormValidation.setText(data.getErrorFormItemValidationMessage());
                this.tvErrorFormValidation.setVisibility(View.VISIBLE);
                this.tvErrorFormRemarkValidation.setVisibility(View.GONE);
                this.tvErrorFormRemarkValidation.setText("");
            }
        }
    }


    private void renderErrorItemHeader(CartItemHolderData data) {
        if (data.getCartItemData().isError()) {
            this.errorContainer.setBackgroundResource(R.color.bg_cart_item_error);
            this.tvError.setTextColor(context.getResources().getColor(R.color.text_cart_item_error_red));
            this.tvError.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_red,
                    0, 0, 0);
            this.errorContainer.setVisibility(View.VISIBLE);
            this.tvError.setVisibility(View.VISIBLE);
            this.tvErrorDetail.setVisibility(View.GONE);
            this.tvError.setText(data.getCartItemData().getErrorMessage());
        } else if (data.getCartItemData().isWarning()) {
            this.errorContainer.setBackgroundResource(R.color.bg_cart_item_warning);
            this.tvError.setTextColor(context.getResources().getColor(R.color.black_54));
            this.tvError.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_grey,
                    0, 0, 0);
            this.errorContainer.setVisibility(View.VISIBLE);
            this.tvError.setVisibility(View.VISIBLE);
            this.tvErrorDetail.setVisibility(View.GONE);
            this.tvError.setText(data.getCartItemData().getWarningMessage());
        } else {
            this.errorContainer.setVisibility(View.GONE);
            this.tvError.setVisibility(View.GONE);
            this.tvErrorDetail.setVisibility(View.GONE);
        }
    }

    private class RemarkTextWatcher implements TextWatcher {
        private final CartItemHolderData data;


        RemarkTextWatcher(CartItemHolderData data) {
            this.data = data;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            data.getCartItemData().getUpdatedData().setRemark(s.toString());
            renderErrorFormItemValidation(data);
        }
    }

    private class QuantityTextWatcher implements TextWatcher {
        private final CartItemHolderData data;

        QuantityTextWatcher(CartItemHolderData data) {
            this.data = data;
        }

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
            renderErrorFormItemValidation(data);
        }
    }
}
