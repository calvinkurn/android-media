package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class CartPayableDetailModelFactory {

    public static CartPayableDetailModel getCartPayableDetailModel() {
        return createCartPayableDetailModel("1",
                "Rp200.000",
                "3kg",
                "Rp16.000",
                "Rp0",
                "Rp100.000",
                "Rp1.300.000",
                "Anda mendapat gratis ongkir Rp20.000");
    }

    private static CartPayableDetailModel createCartPayableDetailModel(String totalItem,
                                                                 String totalItemPrice,
                                                                 String shippingWeight,
                                                                 String shippingFee,
                                                                 String insuranceFee,
                                                                 String promoPrice,
                                                                 String payablePrice,
                                                                 String promoFreeShipping) {

        CartPayableDetailModel cartPayableDetailModel = new CartPayableDetailModel();

        cartPayableDetailModel.setTotalItem(totalItem);
        cartPayableDetailModel.setTotalItemPrice(totalItemPrice);
        cartPayableDetailModel.setShippingWeight(shippingWeight);
        cartPayableDetailModel.setShippingFee(shippingFee);
        cartPayableDetailModel.setInsuranceFee(insuranceFee);
        cartPayableDetailModel.setPromoPrice(promoPrice);
        cartPayableDetailModel.setPayablePrice(payablePrice);
        cartPayableDetailModel.setPromoFreeShipping(promoFreeShipping);

        return cartPayableDetailModel;
    }

}
