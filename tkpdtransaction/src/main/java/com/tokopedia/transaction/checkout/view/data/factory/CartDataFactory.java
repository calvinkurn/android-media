package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 18/02/18.
 */

public class CartDataFactory {

    public static CartItemData.OriginData getOriginData1() {
        return createCartItemOriginData(true,
                "Cashback 5%",
                false,
                true,
                true,
                1,
                390000,
                "Rp390.000",
                "fadjfei",
                "Sottile Sepatu Kulit Derby leather shoes - coklat terang / brown tan",
                "https://ecs7.tokopedia.net/img/cache/300/product-1/2017/9/5/22604480/22604480_72244f48-dce4-494a-8d0a-abcaad1f6fdf_2048_0.jpg",
                "93rkqf9e4",
                "Sottile Shoes",
                2,
                1,
                "2kg");
    }

    public static CartItemData.OriginData getOriginData2() {
        return createCartItemOriginData(false,
                "",
                false,
                false,
                true,
                1,
                140000,
                "Rp140.000",
                "adfarg",
                "Sottile Slim wallet card / dompet kartu kulit minimalis",
                "https://ecs7.tokopedia.net/img/cache/300/product-1/2018/1/9/4876069/4876069_7b430395-ae53-495b-a6e5-f560ae116da8_2000_2000.jpg",
                "93rkqf9e4",
                "Sottile Shoes",
                300,
                0,
                "300gr");
    }

    public static CartItemData.UpdatedData getUpdatedData1() {
        return createCartItemUpdatedData("gak nyesel nunggu beberapa mingguuuuu", 1,
                1000, 140);
    }

    public static CartItemData.UpdatedData getUpdatedData2() {
        return createCartItemUpdatedData("", 10, 1000, 140);
    }

    public static CartItemData.MessageErrorData getMessageErrorData() {
        return createMessageErrorData();
    }

    public static List<CartItemData> createCartListItemData() {
        List<CartItemData> cartItemListData = new ArrayList<>();

        cartItemListData.add(createCartItemData(getOriginData1(), getUpdatedData1(), getMessageErrorData()));
        cartItemListData.add(createCartItemData(getOriginData2(), getUpdatedData2(), getMessageErrorData()));

        return cartItemListData;
    }

    public static CartItemData createCartItemData(CartItemData.OriginData originData,
                                                  CartItemData.UpdatedData updatedData,
                                                  CartItemData.MessageErrorData messageErrorData) {

        CartItemData cartItemData = new CartItemData();

        cartItemData.setOriginData(originData);
        cartItemData.setUpdatedData(updatedData);
        cartItemData.setErrorData(messageErrorData);

        return cartItemData;
    }

    public static CartItemData.MessageErrorData createMessageErrorData() {

        CartItemData.MessageErrorData messageErrorData = new CartItemData.MessageErrorData();

        return messageErrorData;
    }

    public static CartItemData.UpdatedData createCartItemUpdatedData(String remark,
                                                                     int quantity,
                                                                     int maxQuantity,
                                                                     int maxCharRemark) {

        CartItemData.UpdatedData updatedData = new CartItemData.UpdatedData();

        updatedData.setRemark(remark);
        updatedData.setQuantity(quantity);
        updatedData.setMaxQuantity(maxQuantity);
        updatedData.setMaxCharRemark(maxCharRemark);

        return updatedData;
    }

    public static CartItemData.OriginData createCartItemOriginData(boolean isCashback,
                                                                   String cashbackInfo,
                                                                   boolean isFavorite,
                                                                   boolean isFreeReturn,
                                                                   boolean isPreOrder,
                                                                   int priceCurrency,
                                                                   double pricePlan,
                                                                   String priceFormatted,
                                                                   String productId,
                                                                   String productName,
                                                                   String productImage,
                                                                   String shopId,
                                                                   String shopName,
                                                                   double weightPlan,
                                                                   int weightUnit,
                                                                   String weightFormatted) {

        CartItemData.OriginData originData = new CartItemData.OriginData();

        originData.setCashBack(isCashback);
        originData.setCashBackInfo(cashbackInfo);
        originData.setFavorite(isFavorite);
        originData.setFreeReturn(isFreeReturn);
        originData.setPreOrder(isPreOrder);
        originData.setPriceCurrency(priceCurrency);
        originData.setPricePlan(pricePlan);
        originData.setPriceFormatted(priceFormatted);
        originData.setProductId(productId);
        originData.setProductName(productName);
        originData.setProductImage(productImage);
        originData.setShopId(shopId);
        originData.setShopName(shopName);
        originData.setWeightPlan(weightPlan);
        originData.setWeightUnit(weightUnit);
        originData.setWeightFormatted(weightFormatted);

        return originData;
    }

}
