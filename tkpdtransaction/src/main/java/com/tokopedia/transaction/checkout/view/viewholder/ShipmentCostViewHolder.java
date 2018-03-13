package com.tokopedia.transaction.checkout.view.viewholder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author Aghny A. Putra on 02/03/18
 */

public class ShipmentCostViewHolder extends RecyclerView.ViewHolder {

    private static final Locale LOCALE_ID = new Locale("in", "ID");
    private static final NumberFormat CURRENCY_IDR = NumberFormat.getCurrencyInstance(LOCALE_ID);
    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";

    private static final int GRAM = 0;
    private static final int KILOGRAM = 1;

    private RelativeLayout mRlShipmentCostLayout;
    private TextView mTvTotalItemLabel;
    private TextView mTvTotalItemPrice;
    private TextView mTvShippingFeeLabel;
    private TextView mTvShippingFee;
    private TextView mTvInsuranceFee;
    private TextView mTvPromoDiscount;
    private TextView mTvGrandTotal;

    private TextView mTvPromoMessage;

    private SingleAddressShipmentAdapter.ActionListener mActionListener;

    public ShipmentCostViewHolder(View itemView,
                                  SingleAddressShipmentAdapter.ActionListener actionListener) {
        super(itemView);

        mRlShipmentCostLayout = itemView.findViewById(R.id.rl_shipment_cost);
        mTvTotalItemLabel = itemView.findViewById(R.id.tv_total_item_label);
        mTvTotalItemPrice = itemView.findViewById(R.id.tv_total_item_price);
        mTvShippingFeeLabel = itemView.findViewById(R.id.tv_shipping_fee_label);
        mTvShippingFee = itemView.findViewById(R.id.tv_shipping_fee);
        mTvInsuranceFee = itemView.findViewById(R.id.tv_insurance_fee);
        mTvPromoDiscount = itemView.findViewById(R.id.tv_promo);
        mTvGrandTotal = itemView.findViewById(R.id.tv_payable);
        mTvPromoMessage = itemView.findViewById(R.id.tv_promo_message);

        mActionListener = actionListener;
    }

    public void bindViewHolder(ShipmentCostModel shipmentCost) {
        mRlShipmentCostLayout.setVisibility(View.VISIBLE);

        mTvTotalItemLabel.setText(getTotalItemLabel(shipmentCost.getTotalItem()));
        mTvTotalItemPrice.setText(getPriceFormat(shipmentCost.getTotalItemPrice()));
        mTvShippingFeeLabel.setText(getTotalWeightLabel(shipmentCost.getTotalWeight(), GRAM));
        mTvShippingFee.setText(getPriceFormat(shipmentCost.getShippingFee()));
        mTvInsuranceFee.setText(getPriceFormat(shipmentCost.getInsuranceFee()));
        mTvPromoDiscount.setText(getPriceFormat(shipmentCost.getPromoPrice()));

        mTvGrandTotal.setText(getPriceFormat(shipmentCost.getTotalPrice()));

        if (!TextUtils.isEmpty(shipmentCost.getPromoMessage())) {
            formatPromoMessage(mTvPromoMessage, shipmentCost.getPromoMessage());
            mActionListener.onShowPromoMessage(shipmentCost.getPromoMessage());
            mTvPromoMessage.setVisibility(View.VISIBLE);
        } else {
            mActionListener.onHidePromoMessage();
            mTvPromoMessage.setVisibility(View.GONE);
        }
//        mTvPromoTextViewRemove.setOnClickListener(togglePromoTextListener);
    }

    private void formatPromoMessage(TextView textView, String promoMessage) {
        String formatText = " Hapus";
        promoMessage += formatText;
        int startSpan = promoMessage.indexOf(formatText);
        int endSpan = promoMessage.indexOf(formatText) + formatText.length();
        Spannable formattedPromoMessage = new SpannableString(promoMessage);
        final int color = ContextCompat.getColor(textView.getContext(), R.color.black_54);
        formattedPromoMessage.setSpan(new ForegroundColorSpan(color), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        formattedPromoMessage.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setTypeface(Typeface.create(FONT_FAMILY_SANS_SERIF_MEDIUM, Typeface.NORMAL));
        formattedPromoMessage.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(color);
                textPaint.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                mActionListener.onRemovePromoCode();
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(formattedPromoMessage);
    }

    private String getTotalItemLabel(int totalItem) {
        return String.format("Jumlah Barang (%s Item)", totalItem);
    }

    private String getTotalWeightLabel(double weight, int weightUnit) {
        String unit = weightUnit == GRAM ? "gr" : "Kg";
        return String.format("Ongkos Kirim (%s %s)", (int) weight, unit);
    }

    private String getPriceFormat(double price) {
        return price == 0 ? "-" : CURRENCY_IDR.format(price);
    }

    private void togglePromoText() {
        mTvPromoMessage.setVisibility(View.GONE);
    }

    private View.OnClickListener togglePromoTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            togglePromoText();
        }
    };

}