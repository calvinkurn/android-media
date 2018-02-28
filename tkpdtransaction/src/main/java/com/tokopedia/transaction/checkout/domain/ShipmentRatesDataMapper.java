package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.rates.Attribute;
import com.tokopedia.transaction.checkout.domain.response.rates.Product;
import com.tokopedia.transaction.checkout.domain.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.view.data.ShipmentCartData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.data.cartshipmentform.ShipProd;
import com.tokopedia.transaction.checkout.view.data.cartshipmentform.ShopShipment;

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
        List<ShipmentItemData> shipmentItemDataList = getShipmentItemDataList(shipmentDetailData, ratesResponse);
        shipmentDetailData.setShipmentItemData(shipmentItemDataList);

        return shipmentDetailData;
    }

    private List<ShipmentItemData> getShipmentItemDataList(ShipmentDetailData shipmentDetailData,
                                                           RatesResponse ratesResponse) {
        List<ShipmentItemData> shipmentItemDataList = new ArrayList<>();
        for (Attribute attribute : ratesResponse.getData().getAttributes()) {
            ShipmentItemData shipmentItemData = null;

            if (shipmentDetailData.getShipmentCartData() == null) {
                ShipmentCartData shipmentCartData = new ShipmentCartData();
                shipmentDetailData.setShipmentCartData(shipmentCartData);
                shipmentCartData.setShopShipments(new ArrayList<ShopShipment>());
            } else {
                if (shipmentDetailData.getShipmentCartData().getShopShipments() == null) {
                    shipmentDetailData.getShipmentCartData().setShopShipments(new ArrayList<ShopShipment>());
                }
            }

            for (ShopShipment shopShipment : shipmentDetailData.getShipmentCartData().getShopShipments()) {
                if (attribute.getServiceId() == shopShipment.getShipProds().get(0).getShipGroupId()) {
                    shipmentItemData = getShipmentItemData(shopShipment, attribute);
                    break;
                }
            }

            if (shipmentDetailData.getShipmentCartData() != null &&
                    shipmentDetailData.getShipmentCartData().getShopShipments() != null) {
                for (ShopShipment shopShipment : shipmentDetailData.getShipmentCartData().getShopShipments()) {
                    if (shipmentItemData != null && shopShipment.getShipId() == shipmentItemData.getServiceId()) {
                        shipmentItemData.setAllowDropshiper(shopShipment.isDropshipEnabled());
                        break;
                    }
                }
            }

            if (shipmentItemData != null) {
                shipmentItemDataList.add(shipmentItemData);
            }
        }
        return shipmentItemDataList;
    }

    private ShipmentItemData getShipmentItemData(ShopShipment shopShipment, Attribute attribute) {
        ShipmentItemData shipmentItemData = new ShipmentItemData();
        shipmentItemData.setServiceId(attribute.getServiceId());
        shipmentItemData.setCourierItemData(getCourierItemDataList(
                shopShipment.getShipProds(), attribute.getProducts()));
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

    private List<CourierItemData> getCourierItemDataList(List<ShipProd> shipProds, List<Product> products) {
        List<CourierItemData> courierItemDataList = new ArrayList<>();
        for (Product product : products) {
            for (ShipProd shipProd : shipProds) {
                if (shipProd.getShipProdId() == product.getShipperProductId()) {
                    courierItemDataList.add(getCourierItemData(shipProd, product));
                }
            }
        }
        return courierItemDataList;
    }

    private CourierItemData getCourierItemData(ShipProd shipProd, Product product) {
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
        courierItemData.setAdditionalPrice(shipProd.getAdditionalFee());

        // No Data
//        courierItemData.setDeliverySchedule();
//        courierItemData.setCourierInfo();

        return courierItemData;
    }

}
