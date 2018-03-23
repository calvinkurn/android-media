package com.tokopedia.transaction.checkout.data.mapper;

import android.text.TextUtils;

import com.tokopedia.transaction.checkout.data.entity.response.rates.Attribute;
import com.tokopedia.transaction.checkout.data.entity.response.rates.Product;
import com.tokopedia.transaction.checkout.data.entity.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.UserAddress;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class ShipmentRatesDataMapper {

    private static final int DAY_IN_SECONDS = 86400;
    private static final int HOUR_IN_SECONDS = 3600;

    public ShipmentDetailData getShipmentDetailData(CartSellerItemModel cartSellerItemModel,
                                                    RecipientAddressModel recipientAddressModel) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        ShipmentCartData shipmentCartData = cartSellerItemModel.getShipmentCartData();
        shipmentCartData.setDestinationAddress(recipientAddressModel.getAddressStreet());
        shipmentCartData.setDestinationDistrictId(recipientAddressModel.getDestinationDistrictId());
        shipmentCartData.setDestinationLatitude(recipientAddressModel.getLatitude());
        shipmentCartData.setDestinationLongitude(recipientAddressModel.getLongitude());
        shipmentCartData.setDestinationPostalCode(recipientAddressModel.getAddressPostalCode());
        shipmentDetailData.setShipmentCartData(shipmentCartData);
        shipmentDetailData.setTotalQuantity(cartSellerItemModel.getTotalQuantity());
        return shipmentDetailData;
    }

    public ShipmentDetailData getShipmentDetailData(
            MultipleAddressShipmentAdapterData multipleAddressShipmentAdapterData) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        shipmentDetailData.setShipmentCartData(multipleAddressShipmentAdapterData.getShipmentCartData());
        return shipmentDetailData;
    }

    public ShipmentCartData getShipmentCartData(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                UserAddress userAddress, GroupShop groupShop,
                                                MultipleAddressShipmentAdapterData adapterData) {
        ShipmentCartData shipmentCartData = new ShipmentCartData();
        initializeShipmentCartData(cartShipmentAddressFormData, userAddress, groupShop, shipmentCartData);
        int productQuantity = Integer.parseInt(adapterData.getItemData().getProductQty());
        shipmentCartData.setOrderValue((int) adapterData.getProductPriceNumber() * productQuantity);
        shipmentCartData.setWeight(adapterData.getItemData().getProductRawWeight() * productQuantity);

        return shipmentCartData;
    }

    public ShipmentCartData getShipmentCartData(CartShipmentAddressFormData cartShipmentAddressFormData,
                                                UserAddress userAddress, GroupShop groupShop,
                                                CartSellerItemModel cartSellerItemModel) {
        ShipmentCartData shipmentCartData = new ShipmentCartData();
        initializeShipmentCartData(cartShipmentAddressFormData, userAddress, groupShop, shipmentCartData);
        shipmentCartData.setOrderValue((int) cartSellerItemModel.getTotalItemPrice());
        shipmentCartData.setWeight(cartSellerItemModel.getTotalWeight());

        return shipmentCartData;
    }

    private void initializeShipmentCartData(CartShipmentAddressFormData cartShipmentAddressFormData,
                                            UserAddress userAddress, GroupShop groupShop,
                                            ShipmentCartData shipmentCartData) {
        shipmentCartData.setToken(cartShipmentAddressFormData.getKeroToken());
        shipmentCartData.setUt(String.valueOf(cartShipmentAddressFormData.getKeroUnixTime()));
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
        shipmentCartData.setInsurance(1);
        shipmentCartData.setDeliveryPriceTotal(0);
    }

    private String getCategoryIds(List<com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Product> products) {
        List<Integer> categoryIds = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            int categoryId = products.get(i).getProductCatId();
            if (!categoryIds.contains(categoryId)) {
                categoryIds.add(categoryId);
            }
        }
        return TextUtils.join(",", categoryIds);
    }

    private boolean isForceInsurance(List<com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Product> products) {
        for (com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Product product : products) {
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

    public ShipmentDetailData getShipmentDetailData(ShipmentDetailData shipmentDetailData,
                                                    RatesResponse ratesResponse) {
        if (shipmentDetailData == null) {
            shipmentDetailData = new ShipmentDetailData();
        }
        List<ShipmentItemData> shipmentItemDataList = getShipmentItemDataList(ratesResponse);

        List<ShipProd> allShipProds = new ArrayList<>();
        for (ShopShipment shopShipment : shipmentDetailData.getShipmentCartData().getShopShipments()) {
            allShipProds.addAll(shopShipment.getShipProds());
            for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
                for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                    if (courierItemData.getShipperId() == shopShipment.getShipId()) {
                        courierItemData.setAllowDropshiper(shopShipment.isDropshipEnabled());
                    }
                }
            }
        }

        for (ShipProd shipProd : allShipProds) {
            for (ShipmentItemData shipmentItemData : shipmentItemDataList) {
                for (CourierItemData courierItemData : shipmentItemData.getCourierItemData()) {
                    if (shipProd.getShipProdId() == courierItemData.getShipperProductId()) {
                        courierItemData.setAdditionalPrice(shipProd.getAdditionalFee());
                    }
                }
            }
        }

        shipmentDetailData.setShipmentItemData(shipmentItemDataList);
        return shipmentDetailData;
    }

    private List<ShipmentItemData> getShipmentItemDataList(RatesResponse ratesResponse) {
        List<ShipmentItemData> shipmentItemDataList = new ArrayList<>();
        for (Attribute attribute : ratesResponse.getData().getAttributes()) {
            shipmentItemDataList.add(getShipmentItemData(attribute));
        }
        return shipmentItemDataList;
    }

    private ShipmentItemData getShipmentItemData(Attribute attribute) {
        ShipmentItemData shipmentItemData = new ShipmentItemData();
        shipmentItemData.setServiceId(attribute.getServiceId());
        shipmentItemData.setCourierItemData(getCourierItemDataList(attribute.getProducts()));
        shipmentItemData.setServiceId(attribute.getServiceId());
        shipmentItemData.setType(WordUtils.capitalize(attribute.getServiceName()));
        shipmentItemData.setMultiplePriceRange(attribute.getServiceRangePrice());
        shipmentItemData.setDeliveryTimeRange(attribute.getServiceEtd());
        return shipmentItemData;
    }

    private List<CourierItemData> getCourierItemDataList(List<Product> products) {
        List<CourierItemData> courierItemDataList = new ArrayList<>();
        for (Product product : products) {
            courierItemDataList.add(getCourierItemData(product));
        }
        return courierItemDataList;
    }

    private CourierItemData getCourierItemData(Product product) {
        CourierItemData courierItemData = new CourierItemData();
        courierItemData.setUsePinPoint(product.getIsShowMap() == 1);
        courierItemData.setName(product.getShipperName() + " " + product.getShipperProductName());
        courierItemData.setShipperId(product.getShipperId());
        courierItemData.setShipperProductId(product.getShipperProductId());
        courierItemData.setInsuranceUsedInfo(product.getInsuranceUsedInfo());
        courierItemData.setInsurancePrice(product.getInsurancePrice());
        courierItemData.setInsuranceType(product.getInsuranceType());
        courierItemData.setInsuranceUsedDefault(product.getInsuranceUsedDefault());
        courierItemData.setCourierInfo(product.getShipperProductDesc());
        courierItemData.setInsuranceUsedType(product.getInsuranceUsedType());
        courierItemData.setDeliveryPrice(product.getShipperPrice());
        courierItemData.setEstimatedTimeDelivery(product.getShipperEtd());
        courierItemData.setMinEtd(product.getMinEtd());
        courierItemData.setMaxEtd(product.getMaxEtd());

        return courierItemData;
    }

}
