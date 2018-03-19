package com.tokopedia.transaction.checkout.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentCartData;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;
import com.tokopedia.transaction.pickuppoint.view.customview.PickupPointLayout;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by kris on 3/7/18. Tokopedia
 */

public class MultipleShippingAddressViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    private TextView senderName;

    private ImageView productImage;

    private TextView productName;

    private TextView productPrice;

    private TextView productWeight;

    private TextView productQty;

    private ViewGroup notesToSellerLayout;

    private TextView notesField;

    private ViewGroup addressLayout;

    private TextView addressTitle;

    private TextView addressReceiverName;

    private TextView address;

    private TextView chooseCourierButton;

    private TextView tvSelectedShipment;

    private ImageView ivChevronShipmentOption;

    private ViewGroup subTotalLayout;

    private TextView subTotalAmount;

    private ImageView subTotalTotalDetailButton;

    private PickupPointLayout pickupPointLayout;

    private TextView phoneNumber;

    private TextView changeAddress;

    private RelativeLayout rlProductPoliciesLayout;

    private ImageView ivFreeReturnIcon;

    private TextView tvFreeReturnText;

    private TextView tvPoSign;

    private TextView tvCashbackText;

    private ViewGroup detailPriceLayout;

    private TextView totalGoodsLabel;

    private TextView itemPrice;

    private TextView totalDeliveryLabel;

    private TextView deliveryPrice;

    private TextView insurancePrice;

    private LinearLayout errorContainer;
    private TextView tvError;
    private TextView tvErrorDetail;

    public MultipleShippingAddressViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();

        senderName = itemView.findViewById(R.id.sender_name);

        productImage = itemView.findViewById(R.id.iv_product_image);

        productName = itemView.findViewById(R.id.tv_product_name);

        productPrice = itemView.findViewById(R.id.tv_product_price);

        productWeight = itemView.findViewById(R.id.tv_product_weight);

        productQty = itemView.findViewById(R.id.tv_product_total_item);

        notesToSellerLayout = itemView.findViewById(R.id.ll_optional_note_to_seller_layout);

        notesField = itemView.findViewById(R.id.tv_optional_note_to_seller);

        addressLayout = itemView.findViewById(R.id.address_layout);

        addressTitle = itemView.findViewById(R.id.tv_address_name);

        addressReceiverName = itemView.findViewById(R.id.tv_recipient_name);

        address = itemView.findViewById(R.id.tv_recipient_address);

        changeAddress = itemView.findViewById(R.id.tv_change_address);

        phoneNumber = itemView.findViewById(R.id.tv_recipient_phone);

        changeAddress.setVisibility(View.GONE);

        chooseCourierButton = itemView.findViewById(R.id.choose_courier_button);

        subTotalLayout = itemView.findViewById(R.id.sub_total_layout);

        subTotalAmount = itemView.findViewById(R.id.sub_total_amount);

        subTotalTotalDetailButton = itemView.findViewById(R.id.sub_total_detail_button);

        pickupPointLayout = itemView.findViewById(R.id.pickup_point_layout);

        rlProductPoliciesLayout = itemView.findViewById(R.id.rl_product_policies_layout);

        rlProductPoliciesLayout.setVisibility(View.GONE);

        ivFreeReturnIcon = itemView.findViewById(R.id.iv_free_return_icon);

        tvFreeReturnText = itemView.findViewById(R.id.tv_free_return_label);

        tvPoSign = itemView.findViewById(R.id.tv_pre_order);

        tvCashbackText = itemView.findViewById(R.id.tv_cashback);

        tvSelectedShipment = itemView.findViewById(R.id.tv_selected_shipment);

        ivChevronShipmentOption = itemView.findViewById(R.id.iv_chevron_shipment_option);

        this.errorContainer = itemView.findViewById(R.id.ll_warning_container);
        this.tvError = itemView.findViewById(R.id.tv_warning);
        this.tvErrorDetail = itemView.findViewById(R.id.tv_warning_detail);

        detailPriceLayout = itemView.findViewById(R.id.detail_price_layout);

        totalGoodsLabel = itemView.findViewById(R.id.total_goods_label);

        itemPrice = itemView.findViewById(R.id.item_price);

        totalDeliveryLabel = itemView.findViewById(R.id.total_delivery_label);

        deliveryPrice = itemView.findViewById(R.id.delivery_price);

        insurancePrice = itemView.findViewById(R.id.total_insurance_price);

    }

    public void bindItems(MultipleAddressShipmentAdapterData data,
                          MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener) {
        data.setSubTotal(calculateSubTotal(data));
        MultipleAddressItemData itemData = data.getItemData();
        senderName.setText(data.getSenderName());
        ImageHandler.LoadImage(productImage, data.getProductImageUrl());
        productName.setText(data.getProductName());
        productPrice.setText(data.getProductPrice());
        productWeight.setText(itemData.getProductWeight());
        productQty.setText(itemData.getProductQty());
        if (itemData.getProductNotes().isEmpty()) {
            notesToSellerLayout.setVisibility(View.GONE);
        } else {
            notesToSellerLayout.setVisibility(View.VISIBLE);
            notesField.setText(itemData.getProductNotes());
        }
        addressTitle.setText(itemData.getAddressTitle());
        addressReceiverName.setText(itemData.getAddressReceiverName());
        address.setText(itemData.getAddressStreet()
                + ", " + itemData.getAddressCityName()
                + ", " + itemData.getAddressProvinceName());
        phoneNumber.setText(itemData.getRecipientPhoneNumber());

        totalGoodsLabel
                .setText(totalGoodsLabel.getText()
                        .toString()
                        .replace("#", data.getItemData().getProductQty()));

        totalDeliveryLabel
                .setText(totalDeliveryLabel
                        .getText()
                        .toString()
                        .replace("#", data.getItemData().getProductWeight())
                );

        if(isShipmentDataInitiated(data)) {
            itemPrice.setText(formatPrice(data.getProductPriceNumber()));
            deliveryPrice.setText(formatPrice(
                    getGeneratedShipmentCartData(data).getDeliveryPriceTotal()));
            insurancePrice.setText(formatPrice(
                    getGeneratedShipmentCartData(data).getInsurancePrice()
            ));
        } else {
            itemPrice.setText("-");
            deliveryPrice.setText("-");
            insurancePrice.setText("-");
        }

        subTotalAmount.setText(formatPrice(data.getSubTotal()));
        chooseCourierButton.setOnClickListener(onChooseCourierClicked(
                data, data.getInvoicePosition(), listener)
        );
        tvSelectedShipment.setOnClickListener(onChooseCourierClicked(
                data, data.getInvoicePosition(), listener)
        );
        subTotalLayout.setOnClickListener(onChevronClickedListener());
        chooseCourierButton
                .setOnClickListener(getChooseCourierClickListener(data, data.getInvoicePosition(), listener));
        if (data.getSelectedShipmentDetailData() != null &&
                data.getSelectedShipmentDetailData().getSelectedCourier() != null) {
            chooseCourierButton.setVisibility(View.GONE);
            tvSelectedShipment.setText(
                    data.getSelectedShipmentDetailData().getSelectedCourier().getName());
            tvSelectedShipment.setVisibility(View.VISIBLE);
            ivChevronShipmentOption.setVisibility(View.VISIBLE);
        } else {
            tvSelectedShipment.setVisibility(View.GONE);
            ivChevronShipmentOption.setVisibility(View.GONE);
            chooseCourierButton.setVisibility(View.VISIBLE);
        }


        if (data.isError()) {
            errorContainer.setBackgroundResource(R.color.bg_cart_item_error);
            tvError.setTextColor(context.getResources().getColor(R.color.text_cart_item_error_red));
            tvError.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_red,
                    0, 0, 0);
            errorContainer.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.VISIBLE);
            tvErrorDetail.setVisibility(View.GONE);
            tvError.setText(data.getErrorMessage());
        } else if (data.isWarning()) {
            errorContainer.setBackgroundResource(R.color.bg_cart_item_warning);
            tvError.setTextColor(context.getResources().getColor(R.color.black_54));
            tvError.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning_grey,
                    0, 0, 0);
            errorContainer.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.VISIBLE);
            tvErrorDetail.setVisibility(View.GONE);
            tvError.setText(data.getWarningMessage());
        } else {
            errorContainer.setVisibility(View.GONE);
            tvError.setVisibility(View.GONE);
            tvErrorDetail.setVisibility(View.GONE);
        }
        //TODO Release Later
//        renderPickupPoint(itemViewHolder, data);
    }

    //TODO Release Later
    /*private void renderPickupPoint(final MultipleShippingAddressViewHolder itemViewHolder,
                                   final MultipleAddressShipmentAdapterData data) {
        pickupPointLayout.setListener(new PickupPointLayout.ViewListener() {
            @Override
            public void onChoosePickupPoint() {
                listener.onChoosePickupPoint(data, getAdapterPosition());
            }

            @Override
            public void onClearPickupPoint(Store oldStore) {
                listener.onClearPickupPoint(data, getAdapterPosition());
            }

            @Override
            public void onEditPickupPoint(Store oldStore) {
                listener.onEditPickupPoint(data, getAdapterPosition());
            }
        });
        if (data.getStore() == null) {
            pickupPointLayout.unSetData(pickupPointLayout.getContext());
            pickupPointLayout.enableChooserButton(pickupPointLayout.getContext());
            chooseCourierButton.setText("Pilih Kurir");
        } else {
            pickupPointLayout.setData(pickupPointLayout.getContext(), data.getStore());
            chooseCourierButton.setText("Alfatrex");
        }
        pickupPointLayout.setVisibility(View.VISIBLE);
    }*/

    private View.OnClickListener onChooseCourierClicked(
            final MultipleAddressShipmentAdapterData data,
            final int position,
            final MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener
    ) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChooseShipment(data, position);
            }
        };
    }

    private View.OnClickListener onChevronClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detailPriceLayout.isShown()) {
                    detailPriceLayout.setVisibility(View.GONE);
                    subTotalTotalDetailButton.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.chevron_down));
                } else {
                    detailPriceLayout.setVisibility(View.VISIBLE);
                    subTotalTotalDetailButton.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.chevron_up));
                }
            }
        };
    }

    private View.OnClickListener getChooseCourierClickListener(
            final MultipleAddressShipmentAdapterData data,
            final int position,
            final MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChooseShipment(data, position);
            }
        };
    }


    private long calculateSubTotal(MultipleAddressShipmentAdapterData data) {
        if (isShipmentDataInitiated(data))
            return data.getProductPriceNumber()
                    + getGeneratedShipmentCartData(data).getDeliveryPriceTotal();
        else return 0;
    }

    private boolean isShipmentDataInitiated(MultipleAddressShipmentAdapterData data) {
        return data.getSelectedShipmentDetailData() != null
                &&
                data.getSelectedShipmentDetailData().getShipmentCartData() != null;
    }

    private ShipmentCartData getGeneratedShipmentCartData(MultipleAddressShipmentAdapterData data) {
        return data.getSelectedShipmentDetailData().getShipmentCartData();
    }

    private String formatPrice(long unformattedPrice) {
        Locale locale = new Locale("in", "ID");
        NumberFormat rupiahCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return rupiahCurrencyFormat.format(unformattedPrice);
    }
}
