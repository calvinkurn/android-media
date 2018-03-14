package com.tokopedia.transaction.checkout.view.viewholder;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressTotalPriceHolderData;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by kris on 3/7/18. Tokopedia
 */

public class MultipleAddressShipmentFooterTotalPayment extends RecyclerView.ViewHolder {

    private static final String FONT_FAMILY_SANS_SERIF_MEDIUM = "sans-serif-medium";
    private TextView totalPayment;
    private TextView promoMessage;

    private MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener;

    public MultipleAddressShipmentFooterTotalPayment(View itemView,
                                                     MultipleAddressShipmentAdapter.MultipleAddressShipmentAdapterListener listener) {

        super(itemView);
        this.listener = listener;
        totalPayment = itemView.findViewById(R.id.total_payment);
        promoMessage = itemView.findViewById(R.id.tv_promo_message);
    }

    public void bindFooterTotalPayment(
            List<MultipleAddressShipmentAdapterData> addressDataList,
            MultipleAddressPriceSummaryData priceSummaryData,
            MultipleAddressTotalPriceHolderData totalPriceHolderData,
            CartItemPromoHolderData itemPromoHolderData) {

        totalPriceHolderData.setTotalPriceHolderData(totalPriceChecker(
                getTotalPayment(addressDataList),
                priceSummaryData.getTotalShippingPrice()
                        - getDiscountData(itemPromoHolderData)));

        totalPayment.setText(totalPriceHolderData.getTotalPriceHolderData());
        if (priceSummaryData.getAppliedPromo() != null) {
            String promoMessageString = priceSummaryData
                    .getAppliedPromo()
                    .getDataVoucher().getVoucherPromoDesc();
            formatPromoMessage(promoMessage, promoMessageString);
            promoMessage.setVisibility(View.VISIBLE);
            listener.onShowPromo(promoMessageString);
        } else {
            promoMessage.setVisibility(View.GONE);
        }
    }

    private long getDiscountData(CartItemPromoHolderData promoHolderData) {
        if (promoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_COUPON)
            return promoHolderData.getCouponDiscountAmount();
        else if (promoHolderData.getTypePromo() == CartItemPromoHolderData.TYPE_PROMO_VOUCHER)
            return promoHolderData.getVoucherDiscountAmount();
        else return 0;
    }

    private String totalPriceChecker(String totalPriceText, long shipmentPrice) {
        if (shipmentPrice > 0) return totalPriceText;
        else return "-";
    }

    private String getTotalPayment(List<MultipleAddressShipmentAdapterData> addressDataList) {
        return formatPrice(calculateTotalPayment(addressDataList));
    }

    private String formatPrice(long unformattedPrice) {
        Locale locale = new Locale("in", "ID");
        NumberFormat rupiahCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return rupiahCurrencyFormat.format(unformattedPrice);
    }

    private long calculateTotalPayment(List<MultipleAddressShipmentAdapterData> addressDataList) {
        long totalPayment = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            totalPayment = totalPayment + addressDataList.get(i).getSubTotal();
        }
        return totalPayment;
    }

    private Spannable formatPromoMessage(TextView textView, String promoMessage) {
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
                listener.onRemovePromo();
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(formattedPromoMessage);
        return formattedPromoMessage;
    }

}
