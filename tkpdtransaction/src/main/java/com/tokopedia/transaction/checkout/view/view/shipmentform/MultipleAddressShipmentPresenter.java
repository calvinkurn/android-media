package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ShopProductCheckoutRequest;
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

public class MultipleAddressShipmentPresenter implements IMultipleAddressShipmentPresenter{

    public MultipleAddressShipmentPresenter() {

    }

    @Override
    public CheckoutRequest generateCheckoutRequest(List<MultipleAddressShipmentAdapterData> shipmentData,
                                                   MultipleAddressPriceSummaryData priceData) {
        CheckoutRequest.Builder checkoutRequest = new CheckoutRequest.Builder();
        List<DataCheckoutRequest> dataCheckoutRequests = new ArrayList<>();
        for(int i = 0; i < shipmentData.size(); i++) {
            DataCheckoutRequest.Builder checkoutData = new DataCheckoutRequest.Builder();
            List<ShopProductCheckoutRequest> shopCheckoutRequests = new ArrayList<>();
            ShopProductCheckoutRequest.Builder shopCheckoutRequest =
                    new ShopProductCheckoutRequest.Builder();

            List<ProductDataCheckoutRequest> productDataCheckoutRequests = new ArrayList<>();
            ProductDataCheckoutRequest.Builder productDataCheckoutRequest =
                    new ProductDataCheckoutRequest.Builder();
            productDataCheckoutRequests.add(productDataCheckoutRequest
                    .productId(shipmentData.get(i).getProductId()).build());


            /*shopCheckoutRequest.productData(productDataCheckoutRequests).shippingInfo()*/
            /*checkoutData.addressId(Integer
                    .parseInt(shipmentData.get(i).getItemData().getAddressId()))
                    .shopProducts().build();*/
        }
        checkoutRequest.data(dataCheckoutRequests).build();
        return null;
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
                for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    MultipleAddressShipmentAdapterData adapterData =
                            new MultipleAddressShipmentAdapterData();
                    Product currentProduct = productList.get(productIndex);
                    adapterData.setProductId(currentProduct.getProductId());
                    adapterData.setProductName(currentProduct.getProductName());
                    adapterData.setProductPriceNumber(currentProduct.getProductPrice());
                    adapterData.setProductImageUrl(currentProduct.getProductImageSrc200Square());
                    adapterData.setProductPrice(String.valueOf(currentProduct.getProductPrice()));
                    adapterData.setSenderName(currentGroupShop.getShop().getShopName());
                    MultipleAddressItemData addressItemData = new MultipleAddressItemData();
                    addressItemData.setCartPosition(productIndex);
                    addressItemData.setAddressPosition(0);
                    addressItemData.setProductWeight(currentProduct.getProductWeightFmt());
                    addressItemData.setProductNotes(currentProduct.getProductNotes());
                    addressItemData.setProductQty(
                            String.valueOf(currentProduct.getProductQuantity())
                    );
                    addressItemData.setAddressId(
                            String.valueOf(currentAddress.getUserAddress().getAddressId())
                    );
                    addressItemData.setAddressTitle(currentAddress.getUserAddress()
                            .getAddressName());
                    addressItemData.setAddressReceiverName(currentAddress.getUserAddress()
                            .getReceiverName());
                    addressItemData.setAddressStreet(currentAddress.getUserAddress().getAddress());
                    addressItemData.setAddressCityName(currentAddress.getUserAddress().getCityName());
                    addressItemData.setAddressProvinceName(
                            currentAddress.getUserAddress().getProvinceName()
                    );
                    addressItemData.setRecipientPhoneNumber(
                            currentAddress.getUserAddress().getPhone()
                    );
                    addressItemData.setAddressCountryName(currentAddress.getUserAddress()
                            .getCountry());
                    adapterData.setItemData(addressItemData);
                    adapterDataList.add(adapterData);
                }
            }
        }
        return adapterDataList;
    }

}
