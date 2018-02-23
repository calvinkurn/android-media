package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.rates.Attribute;
import com.tokopedia.transaction.checkout.domain.response.rates.Product;
import com.tokopedia.transaction.checkout.domain.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class ShipmentRatesDataMapper {

    public ShipmentDetailData getShipmentDetailData(CartSingleAddressData cartSingleAddressData) {
        ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
        shipmentDetailData.setShipmentCartData(cartSingleAddressData.getShipmentCartData());
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
        shipmentDetailData.setShipmentItemData(getShipmentItemDataList(ratesResponse));

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
//        courierItemData.setAdditionalPrice();
//        courierItemData.setAllowDropshiper();
//        courierItemData.setCourierInfo();

        return courierItemData;
    }

}
