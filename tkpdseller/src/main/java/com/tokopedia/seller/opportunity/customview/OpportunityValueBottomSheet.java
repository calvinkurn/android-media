package com.tokopedia.seller.opportunity.customview;

import android.content.Context;

import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.seller.R;

/**
 * Created by normansyahputa on 1/10/18.
 */

public class OpportunityValueBottomSheet {
    public static BottomSheetView showOpportunityValue(Context context){
        BottomSheetView bottomSheetView = new BottomSheetView(context);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(context.getString(R.string.reputation_value))
                .setBody(context.getString(R.string.reputation_detail_bottomsheet))
                .setImg(R.drawable.ic_peluang_fix_ico)
                .build());

        return bottomSheetView;
    }

    public static BottomSheetView showShippingFee(Context context){
        BottomSheetView bottomSheetView = new BottomSheetView(context);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(context.getString(R.string.label_delivery_price_title))
                .setBody(context.getString(R.string.label_delivery_price_detail))
                .setImg(R.drawable.ic_shipping_fee)
                .build());

        return bottomSheetView;
    }

    public static BottomSheetView showProductPrice(Context context){
        BottomSheetView bottomSheetView = new BottomSheetView(context);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(context.getString(R.string.label_product_price))
                .setBody(context.getString(R.string.label_product_price_detail))
                .setImg(R.drawable.ic_product_price)
                .build());

        return bottomSheetView;
    }
}
