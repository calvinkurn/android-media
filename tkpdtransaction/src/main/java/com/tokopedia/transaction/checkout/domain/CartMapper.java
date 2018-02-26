package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartList;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;
import com.tokopedia.transaction.checkout.view.data.UpdateCartData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartMapper implements ICartMapper {
    private final IMapperUtil mapperUtil;

    @Inject
    public CartMapper(IMapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CartListData convertToCartItemDataList(CartDataListResponse cartDataListResponse) {
        CartListData cartListData = new CartListData();
        cartListData.setError(!mapperUtil.isEmpty(cartDataListResponse.getErrors()));
        cartListData.setErrorMessage(mapperUtil.convertToString(cartDataListResponse.getErrors()));

        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartList data : cartDataListResponse.getCartList()) {
            CartItemData cartItemData = new CartItemData();

            CartItemData.OriginData cartItemDataOrigin = new CartItemData.OriginData();
            cartItemDataOrigin.setProductVarianRemark(
                    data.getProduct().getProductNotes()
            );
            cartItemDataOrigin.setCartId(data.getCartId());
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
            cartItemDataOrigin.setMinimalQtyOrder(data.getProduct().getProductMinOrder());
            cartItemDataOrigin.setFreeReturn(data.getProduct().getIsFreereturns() == 1);
            cartItemDataOrigin.setCashBackInfo(data.getProduct().getProductCashback());
            cartItemDataOrigin.setProductImage(data.getProduct().getProductImage().getImageSrc200Square());

            CartItemData.UpdatedData cartItemDataUpdated = new CartItemData.UpdatedData();
            cartItemDataUpdated.setRemark(cartItemDataOrigin.getProductVarianRemark());
            cartItemDataUpdated.setQuantity(data.getProduct().getProductQuantity());
            cartItemDataUpdated.setMaxCharRemark(cartDataListResponse.getMaxCharNote());
            cartItemDataUpdated.setMaxQuantity(cartDataListResponse.getMaxQuantity());

            CartItemData.MessageErrorData cartItemMessageErrorData = new CartItemData.MessageErrorData();
            cartItemMessageErrorData.setErrorCheckoutPriceLimit(cartDataListResponse.getMessages().getErrorCheckoutPriceLimit());
            cartItemMessageErrorData.setErrorFieldBetween(cartDataListResponse.getMessages().getErrorFieldBetween());
            cartItemMessageErrorData.setErrorFieldMaxChar(cartDataListResponse.getMessages().getErrorFieldMaxChar());
            cartItemMessageErrorData.setErrorFieldRequired(cartDataListResponse.getMessages().getErrorFieldRequired());
            cartItemMessageErrorData.setErrorProductAvailableStock(cartDataListResponse.getMessages().getErrorProductAvailableStock());
            cartItemMessageErrorData.setErrorProductAvailableStockDetail(cartDataListResponse.getMessages().getErrorProductAvailableStockDetail());
            cartItemMessageErrorData.setErrorProductMaxQuantity(cartDataListResponse.getMessages().getErrorProductMaxQuantity());
            cartItemMessageErrorData.setErrorProductMinQuantity(cartDataListResponse.getMessages().getErrorProductMinQuantity());


            cartItemData.setOriginData(cartItemDataOrigin);
            cartItemData.setUpdatedData(cartItemDataUpdated);
            cartItemData.setErrorData(cartItemMessageErrorData);

            cartItemData.setError(!mapperUtil.isEmpty(data.getErrors()));
            cartItemData.setErrorMessage(mapperUtil.convertToString(data.getErrors()));

            cartItemDataList.add(cartItemData);
        }

        CartPromoSuggestion cartPromoSuggestion = new CartPromoSuggestion();
        cartPromoSuggestion.setCta(cartDataListResponse.getPromoSuggestion().getCta());
        cartPromoSuggestion.setCtaColor(cartDataListResponse.getPromoSuggestion().getCtaColor());
        cartPromoSuggestion.setPromoCode(cartDataListResponse.getPromoSuggestion().getPromoCode());
        cartPromoSuggestion.setText(cartDataListResponse.getPromoSuggestion().getText());
        cartPromoSuggestion.setVisible(cartDataListResponse.getPromoSuggestion().getIsVisible() == 1);


//        cartPromoSuggestion.setCta("Gunakan Sekarang!");
//        cartPromoSuggestion.setCtaColor("#42b549");
//        cartPromoSuggestion.setPromoCode("TOKOCASH");
//        cartPromoSuggestion.setText("[iOS] Cashback hingga 25% menggunakan Promo <b>TOKOCASH</b> !");
//        cartPromoSuggestion.setVisible(true);


        cartListData.setCartItemDataList(cartItemDataList);
        cartListData.setCartPromoSuggestion(cartPromoSuggestion);

        return cartListData;
    }

    @Override
    public DeleteCartData convertToDeleteCartData(DeleteCartDataResponse deleteCartDataResponse) {
        return new DeleteCartData.Builder()
                .message(deleteCartDataResponse.getMessage())
                .success(deleteCartDataResponse.getSuccess() == 1)
                .build();
    }

    @Override
    public UpdateCartData convertToUpdateCartData(UpdateCartDataResponse updateCartDataResponse) {
        return new UpdateCartData.Builder()
                .goTo(updateCartDataResponse.get_goto())
                .message(updateCartDataResponse.getError())
                .success(updateCartDataResponse.isStatus())
                .build();
    }
}
