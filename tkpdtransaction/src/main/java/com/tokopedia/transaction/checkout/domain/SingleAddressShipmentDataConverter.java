package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartItemModel;
import com.tokopedia.transaction.checkout.view.data.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.cartshipmentform.CartShipmentAddressFormData;
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
public class SingleAddressShipmentDataConverter extends ConverterData<CartShipmentAddressFormData,
        CartSingleAddressData> {

    @Inject
    public SingleAddressShipmentDataConverter() {
    }

    @Override
    public CartSingleAddressData convert(CartShipmentAddressFormData cartItemDataList) {

        return null;
    }

    private CartPayableDetailModel getTotalPayableDetail(List<CartSellerItemModel> cartSellerItemModels) {

        int totalItem = 0;
        double totalPrice = 0.0;
        double totalWeight = 0.0;

        for (CartSellerItemModel cartSellerItemModel : cartSellerItemModels) {
            totalItem += cartSellerItemModel.getTotalQuantity();
            totalPrice += cartSellerItemModel.getTotalPrice();
            totalWeight += cartSellerItemModel.getTotalWeight();
        }

        CartPayableDetailModel cartPayableDetailModel = new CartPayableDetailModel();
        cartPayableDetailModel.setTotalPrice(totalPrice);
        cartPayableDetailModel.setTotalWeight(totalWeight);
        cartPayableDetailModel.setTotalItem(totalItem);

        return cartPayableDetailModel;
    }

    private List<CartSellerItemModel> groupItemBySeller(List<CartItemModel> cartItemModels) {
        Map<String, CartSellerItemModel> itemGroupMap = new HashMap<>(cartItemModels.size());

        for (CartItemModel cartItemModel : cartItemModels) {
            String shopId = cartItemModel.getShopId();

            if (!itemGroupMap.containsKey(shopId)) {
                CartSellerItemModel cartSellerItemModel = new CartSellerItemModel();
                List<CartItemModel> cartItemModelList = new ArrayList<>();
                cartItemModelList.add(cartItemModel);

                cartSellerItemModel.setShopName(cartItemModel.getShopName());
                cartSellerItemModel.setTotalQuantity(cartItemModel.getQuantity());
                cartSellerItemModel.setTotalWeight(cartItemModel.getWeight());
                cartSellerItemModel.setWeightUnit(cartItemModel.getWeightUnit());
                cartSellerItemModel.setTotalPrice(cartItemModel.getPrice());
                cartSellerItemModel.setCartItemModels(cartItemModelList);

                itemGroupMap.put(shopId, cartSellerItemModel);

            } else {
                CartSellerItemModel cartSellerItemModel = itemGroupMap.get(shopId);

                cartSellerItemModel.getCartItemModels().add(cartItemModel);

                int totalItem = cartSellerItemModel.getTotalQuantity()
                        + cartItemModel.getQuantity();
                cartSellerItemModel.setTotalQuantity(totalItem);

                double weightSum = cartSellerItemModel.getTotalWeight()
                        + cartItemModel.getWeight();
                cartSellerItemModel.setTotalWeight(weightSum);

                double priceSum = cartSellerItemModel.getTotalPrice()
                        + cartItemModel.getPrice();
                cartSellerItemModel.setTotalPrice(priceSum);
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
        cartItemModel.setPreOrder(cartItemData.getOriginData().isPreOrder());
        cartItemModel.setCashback(cartItemData.getOriginData().isCashBack());
        cartItemModel.setCashback(cartItemData.getOriginData().getCashBackInfo());

        cartItemModel.setImageUrl(cartItemData.getOriginData().getProductImage());

        cartItemModel.setShopName(cartItemData.getOriginData().getShopName());
        cartItemModel.setShopId(cartItemData.getOriginData().getShopId());
        cartItemModel.setId(cartItemData.getOriginData().getProductId());
        cartItemModel.setName(cartItemData.getOriginData().getProductName());

        cartItemModel.setCurrency(cartItemData.getOriginData().getPriceCurrency());
        cartItemModel.setPrice(cartItemData.getOriginData().getPricePlan());

        cartItemModel.setWeight(cartItemData.getOriginData().getWeightPlan());
        cartItemModel.setWeightUnit(cartItemData.getOriginData().getWeightUnit());

        cartItemModel.setNoteToSeller(cartItemData.getUpdatedData().getRemark());
        cartItemModel.setQuantity(cartItemData.getUpdatedData().getQuantity());

        return cartItemModel;
    }

}
