package com.tokopedia.transaction.checkout.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Product;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Shop;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartPayableDetailModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSingleAddressData;

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

    private static final int FIRST_ELEMENT = 0;
    private static final int PRIME_ADDRESS = 2;
    private UserAddress userAddress;
    private String keroToken;
    private String keroUnixTime;

    @Inject
    public SingleAddressShipmentDataConverter() {
    }

    @Override
    public CartSingleAddressData convert(CartShipmentAddressFormData cartItemDataList) {
        keroToken = cartItemDataList.getKeroToken();
        keroUnixTime = String.valueOf(cartItemDataList.getKeroUnixTime());

        GroupAddress groupAddress = cartItemDataList.getGroupAddress().get(FIRST_ELEMENT);

        userAddress = groupAddress.getUserAddress();
        List<GroupShop> groupShops = groupAddress.getGroupShop();

        RecipientAddressModel recipientAddressModel = convertFromUserAddress(userAddress);
        List<CartSellerItemModel> cartSellerItemModels = convertFromGroupShopList(groupShops);
        CartPayableDetailModel cartPayableDetailModel = getTotalPayableDetail(cartSellerItemModels);

        CartSingleAddressData cartSingleAddressData = new CartSingleAddressData();
        cartSingleAddressData.setRecipientAddressModel(recipientAddressModel);
        cartSingleAddressData.setCartSellerItemModelList(cartSellerItemModels);
        cartSingleAddressData.setCartPayableDetailModel(cartPayableDetailModel);

        return cartSingleAddressData;
    }

    private List<CartSellerItemModel> convertFromGroupShopList(List<GroupShop> groupShops) {
        List<CartSellerItemModel> cartSellerItemModels = new ArrayList<>();

        for (GroupShop groupShop : groupShops) {
            cartSellerItemModels.add(convertFromGroupShop(groupShop));
        }

        return cartSellerItemModels;
    }

    private CartSellerItemModel convertFromGroupShop(GroupShop groupShop) {
        CartSellerItemModel sellerItemModel = new CartSellerItemModel();

        Shop shop = groupShop.getShop();
        sellerItemModel.setShopId(String.valueOf(shop.getShopId()));
        sellerItemModel.setShopName(shop.getShopName());

        List<Product> products = groupShop.getProducts();
        List<CartItemModel> cartItemModels = convertFromProductList(products);
        sellerItemModel.setCartItemModels(cartItemModels);

        for (CartItemModel cartItemModel : cartItemModels) {
            sellerItemModel.setTotalItemPrice(sellerItemModel.getTotalPrice() + cartItemModel.getPrice());
            sellerItemModel.setTotalWeight(sellerItemModel.getTotalWeight() + cartItemModel.getWeight());
            sellerItemModel.setTotalQuantity(sellerItemModel.getTotalQuantity() + cartItemModel.getQuantity());
        }

        sellerItemModel.setTotalPrice(sellerItemModel.getTotalItemPrice());

        sellerItemModel.setShipmentCartData(new ShipmentCartDataBuilder()
                .setGroupShop(groupShop)
                .setCartSellerItemModel(sellerItemModel)
                .build());

        return sellerItemModel;
    }

    private CartItemModel convertFromProduct(Product product) {
        CartItemModel cartItemModel = new CartItemModel();

        cartItemModel.setId(String.valueOf(product.getProductId()));
        cartItemModel.setName(product.getProductName());
        cartItemModel.setImageUrl(product.getProductImageSrc200Square());
        cartItemModel.setCurrency(product.getProductPriceCurrency());
        cartItemModel.setPrice(product.getProductPrice());
        cartItemModel.setQuantity(product.getProductQuantity());
        cartItemModel.setWeight(product.getProductWeight());
        cartItemModel.setWeightFmt(product.getProductWeightFmt());
        cartItemModel.setNoteToSeller(product.getProductNotes());
        cartItemModel.setPreOrder(product.isProductIsPreorder());
        cartItemModel.setFreeReturn(product.isProductIsFreeReturns());
        cartItemModel.setCashback(product.getProductCashback());
        cartItemModel.setCashback(!TextUtils.isEmpty(product.getProductCashback()));

        return cartItemModel;
    }

    private List<CartItemModel> convertFromProductList(List<Product> products) {
        List<CartItemModel> cartItemModels = new ArrayList<>();

        for (Product product : products) {
            cartItemModels.add(convertFromProduct(product));
        }

        return cartItemModels;
    }

    private RecipientAddressModel convertFromUserAddress(UserAddress userAddress) {
        RecipientAddressModel recipientAddress = new RecipientAddressModel();

        recipientAddress.setId(String.valueOf(userAddress.getAddressId()));
        recipientAddress.setAddressStatus(userAddress.getStatus());
        recipientAddress.setAddressName(userAddress.getAddressName());
        recipientAddress.setAddressCountryName(userAddress.getCountry());
        recipientAddress.setAddressProvinceName(userAddress.getProvinceName());
        recipientAddress.setDestinationDistrictName(userAddress.getDistrictName());
        recipientAddress.setAddressCityName(userAddress.getCityName());
        recipientAddress.setAddressStreet(userAddress.getAddress());
        recipientAddress.setAddressPostalCode(userAddress.getPostalCode());

        recipientAddress.setRecipientName(userAddress.getReceiverName());
        recipientAddress.setRecipientPhoneNumber(userAddress.getPhone());

        recipientAddress.setSelected(userAddress.getStatus() == PRIME_ADDRESS);

        return recipientAddress;
    }

    private CartPayableDetailModel getTotalPayableDetail(List<CartSellerItemModel> cartSellerItemModels) {
        CartPayableDetailModel cartPayable = new CartPayableDetailModel();

        for (CartSellerItemModel itemModel : cartSellerItemModels) {
            cartPayable.setTotalItem(cartPayable.getTotalItem() + itemModel.getTotalQuantity());
            cartPayable.setTotalPrice(cartPayable.getTotalPrice() + itemModel.getTotalPrice());
            cartPayable.setTotalWeight(cartPayable.getTotalWeight() + itemModel.getTotalWeight());
        }

        return cartPayable;
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

    class ShipmentCartDataBuilder {
        private GroupShop groupShop;
        private ShipmentCartData shipmentCartData;
        private CartSellerItemModel cartSellerItemModel;

        public ShipmentCartDataBuilder() {
            shipmentCartData = new ShipmentCartData();
        }

        public ShipmentCartDataBuilder setGroupShop(GroupShop groupShop) {
            this.groupShop = groupShop;
            return this;
        }

        public ShipmentCartDataBuilder setCartSellerItemModel(CartSellerItemModel cartSellerItemModel) {
            this.cartSellerItemModel = cartSellerItemModel;
            return this;
        }

        public ShipmentCartData build() {
            if (userAddress != null && shipmentCartData != null && cartSellerItemModel != null &&
                    groupShop != null) {
                shipmentCartData.setToken(keroToken);
                shipmentCartData.setUt(keroUnixTime);
                shipmentCartData.setDestinationAddress(userAddress.getAddress());
                shipmentCartData.setDestinationDistrictId(String.valueOf(userAddress.getDistrictId()));
                shipmentCartData.setDestinationLatitude(!TextUtils.isEmpty(userAddress.getLatitude()) ?
                        Double.parseDouble(userAddress.getLatitude()) : null);
                shipmentCartData.setDestinationLongitude(!TextUtils.isEmpty(userAddress.getLongitude()) ?
                        Double.parseDouble(userAddress.getLongitude()) : null);
                shipmentCartData.setDestinationPostalCode(userAddress.getPostalCode());
                shipmentCartData.setOriginDistrictId(String.valueOf(groupShop.getShop().getDistrictId()));
                shipmentCartData.setOriginLatitude(!TextUtils.isEmpty(groupShop.getShop().getLatitude()) ?
                        Double.parseDouble(groupShop.getShop().getLatitude()) : null);
                shipmentCartData.setOriginLongitude(!TextUtils.isEmpty(groupShop.getShop().getLongitude()) ?
                        Double.parseDouble(groupShop.getShop().getLongitude()) : null);
                shipmentCartData.setOriginPostalCode(groupShop.getShop().getPostalCode());
                shipmentCartData.setCategoryIds(getCategoryIds(groupShop.getProducts()));
                shipmentCartData.setProductInsurance(isForceInsurance(groupShop.getProducts()) ? 1 : 0);
                shipmentCartData.setShopShipments(groupShop.getShopShipments());
                String shippingNames = getShippingNames(groupShop.getShopShipments());
                shipmentCartData.setShippingNames(shippingNames);
                String shippingServices = getShippingServices(groupShop.getShopShipments());
                shipmentCartData.setShippingServices(shippingServices);
                shipmentCartData.setOrderValue(((Double) cartSellerItemModel.getTotalPrice()).intValue());
                shipmentCartData.setWeight(cartSellerItemModel.getTotalWeight());
                shipmentCartData.setInsurance(1);
                shipmentCartData.setDeliveryPriceTotal(0);
            }
            return shipmentCartData;
        }

        private String getCategoryIds(List<Product> products) {
            List<Integer> categoryIds = new ArrayList<>();
            for (int i = 0; i < products.size(); i++) {
                int categoryId = products.get(i).getProductCatId();
                if (!categoryIds.contains(categoryId)) {
                    categoryIds.add(categoryId);
                }
            }
            return TextUtils.join(",", categoryIds);
        }

        private boolean isForceInsurance(List<Product> products) {
            for (Product product : products) {
                if (product.isProductFinsurance()) {
                    return true;
                }
            }
            return false;
        }

        private String getShippingNames(List<ShopShipment> shopShipments) {
            List<String> shippingNames = new ArrayList<>();
            for (int i = 0; i < shopShipments.size(); i++) {
                String shippingName = shopShipments.get(i).getShipCode();
                if (!shippingNames.contains(shippingName)) {
                    shippingNames.add(shippingName);
                }
            }
            return TextUtils.join(",", shippingNames);
        }

        private String getShippingServices(List<ShopShipment> shopShipments) {
            List<String> shippingServices = new ArrayList<>();
            for (int i = 0; i < shopShipments.size(); i++) {
                for (int j = 0; j < shopShipments.get(i).getShipProds().size(); j++) {
                    String shippingService = shopShipments.get(i).getShipProds().get(j).getShipGroupName();
                    if (!shippingServices.contains(shippingService)) {
                        shippingServices.add(shippingService);
                    }
                }
            }
            return TextUtils.join(",", shippingServices);
        }

    }

}
