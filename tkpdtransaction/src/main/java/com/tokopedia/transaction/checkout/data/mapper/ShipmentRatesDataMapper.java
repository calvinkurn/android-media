package com.tokopedia.transaction.checkout.data.mapper;

import com.tokopedia.transaction.checkout.data.entity.response.rates.Attribute;
import com.tokopedia.transaction.checkout.data.entity.response.rates.Product;
import com.tokopedia.transaction.checkout.data.entity.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.domain.datamodel.CourierItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.ShipProd;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class ShipmentRatesDataMapper {

    public ShipmentDetailData getShipmentDetailData(CartSellerItemModel cartSellerItemModel) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        shipmentDetailData.setShipmentCartData(cartSellerItemModel.getShipmentCartData());
        return shipmentDetailData;
    }

    public ShipmentDetailData getShipmentDetailData(
            MultipleAddressShipmentAdapterData multipleAddressShipmentAdapterData) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        shipmentDetailData.setShipmentCartData(multipleAddressShipmentAdapterData.getShipmentCartData());
        return shipmentDetailData;
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
        shipmentItemData.setType(WordUtils.capitalize(attribute.getShipperName()));
        getPriceRange(shipmentItemData, attribute);

        // No data
//        shipmentItemData.setDeliveryTimeRange();

        return shipmentItemData;
    }

    private void getPriceRange(ShipmentItemData shipmentItemData, Attribute attribute) {
        if (attribute.getProducts().size() > 0) {
            if (attribute.getProducts().size() > 1) {
                int maxPrice = 0;
                Product maxPriceProduct = attribute.getProducts().get(0);
                int minPrice = 0;
                Product minPriceProduct = attribute.getProducts().get(0);

                for (Product product : attribute.getProducts()) {
                    if (product.getPrice() > maxPrice) {
                        maxPrice = product.getPrice();
                        maxPriceProduct = product;
                    }

                    if (minPrice == 0) {
                        minPrice = product.getPrice();
                        minPriceProduct = product;
                    } else if (minPrice > product.getPrice()) {
                        minPrice = product.getPrice();
                        minPriceProduct = product;
                    }
                }

                if (minPrice != maxPrice) {
                    shipmentItemData.setMultiplePriceRange(minPriceProduct.getFormattedPrice() + " - " +
                            maxPriceProduct.getFormattedPrice());
                } else {
                    shipmentItemData.setSinglePriceRange(minPriceProduct.getFormattedPrice());
                }
            } else {
                shipmentItemData.setSinglePriceRange(attribute.getProducts().get(0).getFormattedPrice());
            }
        }
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
        courierItemData.setDeliveryPrice(product.getPrice());
        if (product.getMaxHoursId() != null && product.getMaxHoursId().length() > 0) {
            courierItemData.setEstimatedHourDelivery(product.getMaxHoursId());
        } else {
            courierItemData.setEstimatedDayDelivery(product.getEtd());
        }
        courierItemData.setSelected(false);

        // No Data
//        courierItemData.setDeliverySchedule();
//        courierItemData.setCourierInfo();

        return courierItemData;
    }

}
