package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 06/02/18
 */

public class CartSellerItemModelFactory {

    public static List<CartSellerItemModel> getDummyCartSellerItemModelList() {
        List<CartSellerItemModel> cartItemModels = new ArrayList<>();

        cartItemModels.add(createDummySellerCartItemModel("Adidas",
                CartItemModelFactory.getDummyCartItemModelList(),
                "Alfatrex",
                "Rp1.300.000"));

        cartItemModels.add(createDummySellerCartItemModel("Palugada",
                CartItemModelFactory.getDummyCartItemModelList(),
                "Go-Send",
                "Rp600.000"));

        return cartItemModels;
    }

    public static CartSellerItemModel createDummySellerCartItemModel(String senderName,
                                                               List<CartItemModel> cartItems,
                                                               String shipmentOption,
                                                               String totalPrice) {

        CartSellerItemModel cartSellerItemModel = new CartSellerItemModel();
        cartSellerItemModel.setSenderName(senderName);
        cartSellerItemModel.setCartItemModels(cartItems);
        cartSellerItemModel.setShipmentOption(shipmentOption);
        cartSellerItemModel.setTotalPrice(totalPrice);

        return cartSellerItemModel;
    }
}
