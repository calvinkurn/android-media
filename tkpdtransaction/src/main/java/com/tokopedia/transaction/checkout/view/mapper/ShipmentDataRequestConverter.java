package com.tokopedia.transaction.checkout.view.mapper;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transaction.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.DropshipDataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ProductDataCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ShippingInfoCheckoutRequest;
import com.tokopedia.transaction.checkout.data.entity.request.ShopProductCheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartItemModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 07/03/18
 */

public class ShipmentDataRequestConverter {

    @Inject
    public ShipmentDataRequestConverter() {

    }

    public SingleAddressShipmentAdapter.RequestData generateRequestData(
            List<CartSellerItemModel> cartSellerItemList, RecipientAddressModel recipientAddress) {

        List<ShopProductCheckoutRequest> shopProductCheckoutRequest = new ArrayList<>();
        List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProductPromoRequest = new ArrayList<>();

        for (CartSellerItemModel cartSellerItem : cartSellerItemList) {
            shopProductCheckoutRequest.add(getProductCheckoutRequest(cartSellerItem));
            shopProductPromoRequest.add(getShopProductPromoRequest(cartSellerItem));
        }

        SingleAddressShipmentAdapter.RequestData requestData = new SingleAddressShipmentAdapter.RequestData();

        requestData.setCheckoutRequestData(createCheckoutRequestData(shopProductCheckoutRequest, recipientAddress));
        requestData.setPromoRequestData(createPromoRequestData(shopProductPromoRequest, recipientAddress));

        return requestData;
    }

    private ShopProductCheckoutRequest getProductCheckoutRequest(CartSellerItemModel cartSellerItem) {
        ShipmentDetailData shipmentDetailData = cartSellerItem.getSelectedShipmentDetailData();
        CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

        // Create shop product model for shipment
        ShopProductCheckoutRequest.Builder shopProductCheckoutBuilder = new ShopProductCheckoutRequest.Builder()
                .shopId(Integer.valueOf(cartSellerItem.getShopId()))
                .isPreorder(cartSellerItem.getIsPreOrder())
                .finsurance(shipmentDetailData.getUseInsurance() ? 1 : 0)
                .shippingInfo(new ShippingInfoCheckoutRequest.Builder()
                        .shippingId(courierItemData.getShipperId())
                        .spId(courierItemData.getShipperProductId())
                        .build())
                .productData(convertToProductDataCheckout(cartSellerItem.getCartItemModels()))
                .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0);

        if (shipmentDetailData.getUseDropshipper()) {
            shopProductCheckoutBuilder.isDropship(1)
                    .dropshipData(new DropshipDataCheckoutRequest.Builder()
                            .name(shipmentDetailData.getDropshipperName())
                            .telpNo(shipmentDetailData.getDropshipperPhone())
                            .build());
        } else {
            shopProductCheckoutBuilder.isDropship(0);
        }

        return shopProductCheckoutBuilder.build();
    }

    private CheckPromoCodeCartShipmentRequest.ShopProduct getShopProductPromoRequest(CartSellerItemModel cartSellerItem) {
        ShipmentDetailData shipmentDetailData = cartSellerItem.getSelectedShipmentDetailData();
        CourierItemData courierItemData = shipmentDetailData.getSelectedCourier();

        // Create shop product model for promo request
        CheckPromoCodeCartShipmentRequest.ShopProduct.Builder shopProductBuilder = new CheckPromoCodeCartShipmentRequest.ShopProduct.Builder()
                .shopId(Integer.valueOf(cartSellerItem.getShopId()))
                .isPreorder(cartSellerItem.getIsPreOrder())
                .finsurance(shipmentDetailData.getUseInsurance() ? 1 : 0)
                .shippingInfo(new CheckPromoCodeCartShipmentRequest.ShippingInfo.Builder()
                        .shippingId(courierItemData.getShipperId())
                        .spId(courierItemData.getShipperProductId())
                        .build())
                .productData(convertToProductData(cartSellerItem.getCartItemModels()))
                .fcancelPartial(shipmentDetailData.getUsePartialOrder() ? 1 : 0);

        if (shipmentDetailData.getUseDropshipper()) {
            shopProductBuilder.dropshipData(new CheckPromoCodeCartShipmentRequest.DropshipData.Builder()
                    .name(shipmentDetailData.getDropshipperName())
                    .telpNo(shipmentDetailData.getDropshipperPhone())
                    .build());
        }

        return shopProductBuilder.build();
    }

    private List<ProductDataCheckoutRequest> convertToProductDataCheckout(List<CartItemModel> cartItems) {
        List<ProductDataCheckoutRequest> productDataList = new ArrayList<>();
        for (CartItemModel cartItem : cartItems) {
            productDataList.add(convertToProductDataCheckout(cartItem));
        }

        return productDataList;
    }

    private ProductDataCheckoutRequest convertToProductDataCheckout(CartItemModel cartItem) {
        return new ProductDataCheckoutRequest.Builder()
                .productId(cartItem.getId())
                .build();
    }

    private List<DataCheckoutRequest> createCheckoutRequestData(
            List<ShopProductCheckoutRequest> shopProducts,
            RecipientAddressModel recipientAddress) {

        List<DataCheckoutRequest> checkoutRequestData = new ArrayList<>();
        checkoutRequestData.add(new DataCheckoutRequest.Builder()
                .addressId(Integer.valueOf(recipientAddress.getId()))
                .shopProducts(shopProducts)
                .build());

        return checkoutRequestData;
    }

    private List<CheckPromoCodeCartShipmentRequest.ProductData> convertToProductData(List<CartItemModel> cartItems) {
        List<CheckPromoCodeCartShipmentRequest.ProductData> productDataList = new ArrayList<>();
        for (CartItemModel cartItem : cartItems) {
            productDataList.add(convertToProductData(cartItem));
        }

        return productDataList;
    }

    private CheckPromoCodeCartShipmentRequest.ProductData convertToProductData(CartItemModel cartItem) {
        return new CheckPromoCodeCartShipmentRequest.ProductData.Builder()
                .productId(cartItem.getId())
                .productNotes(cartItem.getNoteToSeller())
                .productQuantity(cartItem.getQuantity())
                .build();
    }

    private List<CheckPromoCodeCartShipmentRequest.Data> createPromoRequestData(
            List<CheckPromoCodeCartShipmentRequest.ShopProduct> shopProducts,
            RecipientAddressModel recipientAddress) {

        List<CheckPromoCodeCartShipmentRequest.Data> promoRequestData = new ArrayList<>();
        promoRequestData.add(new CheckPromoCodeCartShipmentRequest.Data.Builder()
                .addressId(Integer.valueOf(recipientAddress.getId()))
                .shopProducts(shopProducts)
                .build());

        return promoRequestData;
    }

}