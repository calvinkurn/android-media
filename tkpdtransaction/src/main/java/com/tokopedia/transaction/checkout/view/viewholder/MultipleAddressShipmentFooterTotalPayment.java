package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressTotalPriceHolderData;

import java.util.List;

/**
 * Created by kris on 3/7/18. Tokopedia
 */

public class MultipleAddressShipmentFooterTotalPayment extends RecyclerView.ViewHolder{

    private TextView totalPayment;

    public MultipleAddressShipmentFooterTotalPayment(View itemView) {

        super(itemView);

        totalPayment = itemView.findViewById(R.id.total_payment);
    }

    public void bindFooterTotalPayment(
            List<MultipleAddressShipmentAdapterData> addressDataList,
            MultipleAddressPriceSummaryData priceSummaryData,
            MultipleAddressTotalPriceHolderData totalPriceHolderData) {
        totalPriceHolderData.setTotalPriceHolderData(totalPriceChecker(
                getTotalPayment(addressDataList),
                priceSummaryData.getTotalShippingPrice()));
        totalPayment.setText(totalPriceHolderData.getTotalPriceHolderData());
    }

    private String totalPriceChecker(String totalPriceText, long shipmentPrice) {
        if (shipmentPrice > 0) return totalPriceText;
        else return "-";
    }

    private String getTotalPayment(List<MultipleAddressShipmentAdapterData> addressDataList) {
        return formatPrice(calculateTotalPayment(addressDataList));
    }

    private String formatPrice(long unformattedPrice) {
        String formattedPrice = CurrencyFormatHelper
                .ConvertToRupiah(String.valueOf(unformattedPrice));
        formattedPrice = formattedPrice.replace(",", ".");
        return formattedPrice;
    }

    private long calculateTotalPayment(List<MultipleAddressShipmentAdapterData> addressDataList) {
        long totalPayment = 0;
        for (int i = 0; i < addressDataList.size(); i++) {
            totalPayment = totalPayment + addressDataList.get(i).getSubTotal();
        }
        return totalPayment;
    }

}
