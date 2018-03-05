package com.tokopedia.transaction.checkout.view.viewholder;

import android.support.v7.widget.RecyclerView;
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

    private TextView mTvPromoFreeShipping;
    private TextView mTvPromoTextViewRemove;

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
        mTvPromoFreeShipping = itemView.findViewById(R.id.tv_promo_free_shipping);
        mTvPromoTextViewRemove = itemView.findViewById(R.id.tv_promo_text_view_remove);

        mActionListener = actionListener;
    }

    public void bindViewHolder(ShipmentCostModel shipmentCostModel) {
        mRlShipmentCostLayout.setVisibility(View.VISIBLE);

        mTvTotalItemLabel.setText(getTotalItemLabel(shipmentCostModel.getTotalItem()));
        mTvTotalItemPrice.setText(getPriceFormat(shipmentCostModel.getTotalItemPrice()));
        mTvShippingFeeLabel.setText(getTotalWeightLabel(shipmentCostModel.getTotalWeight(), GRAM));
        mTvShippingFee.setText(getPriceFormat(shipmentCostModel.getShippingFee()));
        mTvInsuranceFee.setText(getPriceFormat(shipmentCostModel.getInsuranceFee()));
        mTvPromoDiscount.setText(getPriceFormat(shipmentCostModel.getPromoPrice()));

        shipmentCostModel.setTotalPrice(calculateTotalPrice(shipmentCostModel));
        mTvGrandTotal.setText(getPriceFormat(shipmentCostModel.getTotalPrice()));

        mTvPromoFreeShipping.setVisibility(View.VISIBLE);
        mTvPromoTextViewRemove.setOnClickListener(togglePromoTextListener);

        mActionListener.onTotalPaymentChange(shipmentCostModel);
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

    private double calculateTotalPrice(ShipmentCostModel shipmentCostModel) {
        return shipmentCostModel.getTotalItemPrice()
                + shipmentCostModel.getInsuranceFee()
                + shipmentCostModel.getShippingFee();
    }

    private void togglePromoText() {
        mTvPromoFreeShipping.setVisibility(View.GONE);
        mTvPromoTextViewRemove.setVisibility(View.GONE);
    }

    private View.OnClickListener togglePromoTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            togglePromoText();
        }
    };

}
