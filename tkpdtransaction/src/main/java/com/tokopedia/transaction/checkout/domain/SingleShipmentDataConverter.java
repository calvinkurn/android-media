package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.factory.CartSingleAddressDataFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 08/02/18,
 *         Aghny A. Putra on 08/02/18.
 */
public class SingleShipmentDataConverter extends ConverterData<List<CartItemData>,
        CartSingleAddressData> {

    private static final int GRAM = 0;
    private static final int KILOGRAM = 1;

    @Inject
    public SingleShipmentDataConverter() {
    }

    @Override
    public CartSingleAddressData convert(List<CartItemData> cartItemDataList) {
        CartSingleAddressData cartSingleAddressData =
                CartSingleAddressDataFactory.getDummyCartSingleAddressData();

        List<CartItemModel> cartItemModels = convertCartItemList(cartItemDataList);
        List<CartSellerItemModel> cartSellerItemModels = groupItemBySeller(cartItemModels);

        cartSingleAddressData.setCartSellerItemModelList(cartSellerItemModels);

        return cartSingleAddressData;
    }

    private List<CartSellerItemModel> groupItemBySeller(List<CartItemModel> cartItemModels) {
        Map<String, CartSellerItemModel> itemGroupMap = new HashMap<>(cartItemModels.size());

        for (CartItemModel cartItemModel : cartItemModels) {
            String shopId = cartItemModel.getShopId();

            if (!itemGroupMap.containsKey(shopId)) {
                CartSellerItemModel cartSellerItemModel = new CartSellerItemModel();
                List<CartItemModel> cartItemModelList = new ArrayList<>();
                cartItemModelList.add(cartItemModel);

                cartSellerItemModel.setTotalItemPlan(cartItemModel.getTotalProductItem());
                cartSellerItemModel.setTotalWeightPlan(cartItemModel.getProductWeightPlan());
                cartSellerItemModel.setTotalPricePlan(cartItemModel.getProductPricePlan());
                cartSellerItemModel.setCartItemModels(cartItemModelList);

                itemGroupMap.put(shopId, cartSellerItemModel);

            } else {
                CartSellerItemModel cartSellerItemModel = itemGroupMap.get(shopId);

                cartSellerItemModel.getCartItemModels().add(cartItemModel);

                int totalItem = cartSellerItemModel.getTotalItemPlan()
                        + cartItemModel.getTotalProductItem();
                cartSellerItemModel.setTotalItemPlan(totalItem);

                double weightSum = cartSellerItemModel.getTotalWeightPlan()
                        + cartItemModel.getProductWeightPlan();
                cartSellerItemModel.setTotalWeightPlan(weightSum);

                double priceSum = cartSellerItemModel.getTotalPricePlan()
                        + cartItemModel.getProductPricePlan();
                cartSellerItemModel.setTotalPricePlan(priceSum);
            }
        }

        return convertModelMapToList(itemGroupMap);
    }

    private List<CartSellerItemModel> convertModelMapToList(Map<String, CartSellerItemModel> map) {
        List<CartSellerItemModel> cartSellerItemModels = new ArrayList<>();

        for (Map.Entry<String, CartSellerItemModel> entry : map.entrySet()) {
            cartSellerItemModels.add(entry.getValue());
        }

        return cartSellerItemModels;
    }

    private List<CartItemModel> convertCartItemList(List<CartItemData> cartItemDataList) {
        List<CartItemModel> cartItemModels = new ArrayList<>();
        for (CartItemData cartItemData : cartItemDataList) {
            cartItemModels.add(convertCartItem(cartItemData));
        }

        return cartItemModels;
    }

    private CartItemModel convertCartItem(CartItemData cartItemData) {
        CartItemModel cartItemModel = new CartItemModel();

        cartItemModel.setFreeReturn(cartItemData.getOriginData().isFreeReturn());
        cartItemModel.setPoAvailable(cartItemData.getOriginData().isPreOrder());
        cartItemModel.setCashback(cartItemData.getOriginData().isCashBack());
        cartItemModel.setCashback(cartItemData.getOriginData().getCashBackInfo());

        cartItemModel.setProductImageUrl(cartItemData.getOriginData().getProductImage());

        cartItemModel.setShopName(cartItemData.getOriginData().getShopName());
        cartItemModel.setShopId(cartItemData.getOriginData().getShopId());
        cartItemModel.setProductId(cartItemData.getOriginData().getProductId());
        cartItemModel.setProductName(cartItemData.getOriginData().getProductName());

        cartItemModel.setProductPriceFormatted(cartItemData.getOriginData().getPriceFormatted());
        cartItemModel.setProductPriceCurrency(cartItemData.getOriginData().getPriceCurrency());
        cartItemModel.setProductPricePlan(cartItemData.getOriginData().getPricePlan());

        cartItemModel.setProductWeightPlan(cartItemData.getOriginData().getWeightPlan());
        cartItemModel.setProductWeightUnit(cartItemData.getOriginData().getWeightUnit());
        cartItemModel.setProductWeightFormatted(getWeightFormat(
                cartItemData.getOriginData().getWeightPlan(),
                cartItemData.getOriginData().getWeightUnit()));

        cartItemModel.setNoteToSeller(cartItemData.getUpdatedData().getRemark());
        cartItemModel.setTotalProductItem(cartItemData.getUpdatedData().getQuantity());

        return cartItemModel;
    }

    private String getWeightFormat(double weightPlan, int weightUnit) {
        String weight = String.valueOf(weightPlan);

        switch (weightUnit) {
            case KILOGRAM:
                return weight + " Kg";
            case GRAM:
                return weight + " g";
            default:
                return weight;
        }
    }

}
