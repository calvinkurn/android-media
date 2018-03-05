package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.data.mapper.ShipmentRatesDataMapper;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupAddress;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.GroupShop;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressShipmentPresenter implements IMultipleAddressShipmentPresenter {

    public MultipleAddressShipmentPresenter() {

    }

    @Override
    public void sendData(List<MultipleAddressShipmentAdapterData> shipmentData,
                         MultipleAddressPriceSummaryData priceData) {

    }

    @Override
    public List<MultipleAddressShipmentAdapterData> initiateAdapterData(CartShipmentAddressFormData data) {
        List<MultipleAddressShipmentAdapterData> adapterDataList = new ArrayList<>();
        for (int addressIndex = 0; addressIndex < data.getGroupAddress().size(); addressIndex++) {
            GroupAddress currentAddress = data.getGroupAddress().get(addressIndex);
            List<GroupShop> groupShopList = data.getGroupAddress().get(addressIndex).getGroupShop();
            for (int shopIndex = 0; shopIndex < groupShopList.size(); shopIndex++) {
                GroupShop currentGroupShop = groupShopList.get(shopIndex);
                List<Product> productList = currentGroupShop.getProducts();
                for (int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    MultipleAddressShipmentAdapterData adapterData =
                            new MultipleAddressShipmentAdapterData();
                    Product currentProduct = productList.get(productIndex);
                    adapterData.setProductName(currentProduct.getProductName());
                    adapterData.setProductPriceNumber(currentProduct.getProductPrice());
                    adapterData.setProductPrice(String.valueOf(currentProduct.getProductPrice()));
                    MultipleAddressItemData addressItemData = new MultipleAddressItemData();
                    addressItemData.setCartPosition(productIndex);
                    addressItemData.setAddressPosition(0);
                    addressItemData.setProductWeight(currentProduct.getProductWeightFmt());
                    addressItemData.setProductRawWeight(currentProduct.getProductWeight());
                    addressItemData.setProductNotes(currentProduct.getProductNotes());
                    addressItemData.setProductQty(
                            String.valueOf(currentProduct.getProductQuantity())
                    );
                    addressItemData.setAddressId(
                            String.valueOf(currentAddress.getUserAddress().getAddressId())
                    );
                    addressItemData.setAddressStreet(currentAddress.getUserAddress().getAddress());
                    addressItemData.setAddressCityName(currentAddress.getUserAddress().getCityName());
                    addressItemData.setAddressProvinceName(
                            currentAddress.getUserAddress().getProvinceName()
                    );
                    addressItemData.setAddressCountryName(currentAddress.getUserAddress()
                            .getCountry());
                    adapterData.setItemData(addressItemData);
                    adapterData.setShipmentCartData(new ShipmentRatesDataMapper()
                            .getShipmentCartData(data, currentAddress.getUserAddress(),
                                    currentGroupShop, adapterData));
                    adapterDataList.add(adapterData);
                }
            }
        }
        return adapterDataList;
    }

}
