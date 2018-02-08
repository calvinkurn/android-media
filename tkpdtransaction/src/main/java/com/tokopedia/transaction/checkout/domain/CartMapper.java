package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartList;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartMapper implements ICartMapper {

    @Inject
    public CartMapper() {
    }

    @Override
    public List<CartItemData> convertToCartItemDataList(CartDataListResponse cartDataListResponse) {

        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartList data : cartDataListResponse.getCartList()) {
            CartItemData cartItemData = new CartItemData();

            CartItemData.OriginData cartItemDataOrigin = new CartItemData.OriginData();
            cartItemDataOrigin.setProductVarianRemark(
                    data.getProduct().getProductNotes()
            );
            cartItemDataOrigin.setShopId(String.valueOf(data.getShop().getShopId()));
            cartItemDataOrigin.setShopName(data.getShop().getShopName());
            cartItemDataOrigin.setWeightFormatted(data.getProduct().getProductWeightFmt());
            cartItemDataOrigin.setWeightUnit(data.getProduct().getProductWeightUnitCode());
            cartItemDataOrigin.setWeightPlan(data.getProduct().getProductWeight());
            cartItemDataOrigin.setProductName(data.getProduct().getProductName());
            cartItemDataOrigin.setProductId(String.valueOf(data.getProduct().getProductId()));
            cartItemDataOrigin.setPriceFormatted(data.getProduct().getProductPriceFmt());
            cartItemDataOrigin.setPricePlan(data.getProduct().getProductPrice());
            cartItemDataOrigin.setPriceCurrency(data.getProduct().getProductPriceCurrency());
            cartItemDataOrigin.setPreOrder(data.getProduct().getIsPreorder() == 1);
            cartItemDataOrigin.setFavorite(false);
            cartItemDataOrigin.setMaximalQtyOrder(1000);
            cartItemDataOrigin.setMinimalQtyOrder(data.getProduct().getProductMinOrder());
            cartItemDataOrigin.setFreeReturn(data.getProduct().getIsFreereturns() == 1);
            cartItemDataOrigin.setCashBackInfo(data.getProduct().getProductCashback());
            cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());

            CartItemData.UpdatedData cartItemDataUpdated = new CartItemData.UpdatedData();
            cartItemDataUpdated.setRemark(cartItemDataOrigin.getProductVarianRemark());
            cartItemDataUpdated.setQuantity(data.getProduct().getProductQuantity());

            cartItemData.setOriginData(cartItemDataOrigin);
            cartItemData.setUpdatedData(cartItemDataUpdated);

            cartItemDataList.add(cartItemData);
        }

        return cartItemDataList;
    }
}
