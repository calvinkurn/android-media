package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.view.ICartListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListPresenter implements ICartListPresenter {
    private final ICartListView view;

    @Inject
    public CartListPresenter(ICartListView view) {
        this.view = view;
    }

    @Override
    public void processGetCartData() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CartItemData cartItemData = new CartItemData();
            CartItemData.OriginData originData = new CartItemData.OriginData();
            CartItemData.UpdatedData updatedData = new CartItemData.UpdatedData();
            originData.setCashBack(false);
            originData.setCashBackInfo("");
            originData.setFreeReturn(true);
            originData.setMinimalQtyOrder(2);
            originData.setMaximalQtyOrder(100);
            originData.setFavorite(true);
            originData.setPreOrder(false);
            originData.setPriceCurrency(1);
            originData.setPricePlan(2000);
            originData.setPriceFormatted("Rp. 2000");
            originData.setProductId("12345"+i);
            originData.setProductName("Product Dummy "+i);
            originData.setWeightPlan(200);
            originData.setWeightUnit(1);
            originData.setWeightFormatted("200 Gram");
            originData.setShopName("Toko Kelontong");
            originData.setShopId("2346");
            originData.setProductVarianRemark("Kuning Kelabu");

            updatedData.setQuantity(originData.getMinimalQtyOrder());
            updatedData.setRemark(originData.getProductVarianRemark());

            cartItemData.setOriginData(originData);
            cartItemData.setUpdatedData(updatedData);
            cartItemDataList.add(cartItemData);
        }

        view.renderCartListData(cartItemDataList);
    }
}
