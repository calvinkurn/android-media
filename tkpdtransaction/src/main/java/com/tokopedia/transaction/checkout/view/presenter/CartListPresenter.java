package com.tokopedia.transaction.checkout.view.presenter;

import android.content.Intent;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.transaction.checkout.domain.ICartListInteractor;
import com.tokopedia.transaction.checkout.view.activity.CartShipmentActivity;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;
import com.tokopedia.transaction.checkout.view.view.ICartListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartListPresenter implements ICartListPresenter {
    private final ICartListView view;
    private final ICartListInteractor cartListInteractor;


    @Inject
    public CartListPresenter(ICartListView cartListView, ICartListInteractor cartListInteractor) {
        this.view = cartListView;
        this.cartListInteractor = cartListInteractor;
    }

    @Override
    public void processGetCartData() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("lang", "id");
        cartListInteractor.getCartList(new Subscriber<List<CartItemData>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<CartItemData> cartItemDataList) {
                view.renderCartListData(cartItemDataList);
            }
        }, view.getGeneratedAuthParamNetwork(param));

        // getDummyCartList();

    }

    private void getDummyCartList() {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CartItemData cartItemData = new CartItemData();
            CartItemData.OriginData originData = new CartItemData.OriginData();
            CartItemData.UpdatedData updatedData = new CartItemData.UpdatedData();
            originData.setCashBack(false);
            originData.setCashBackInfo("");
            originData.setFreeReturn(true);
            originData.setMinimalQtyOrder(2);
            originData.setMaximalQtyOrder(100);
            originData.setFavorite(true);
            originData.setPreOrder(false);
            originData.setPriceCurrency(1);
            originData.setPricePlan(2000);
            originData.setPriceFormatted("Rp. 2000");
            originData.setProductId("12345" + i);
            originData.setProductName("Product Dummy " + i);
            originData.setWeightPlan(200);
            originData.setWeightUnit(1);
            originData.setWeightFormatted("200 Gram");
            originData.setShopName("Toko Kelontong");
            originData.setShopId("2346");
            originData.setProductVarianRemark("Kuning Kelabu");

            updatedData.setQuantity(originData.getMinimalQtyOrder());
            updatedData.setRemark(originData.getProductVarianRemark());

            cartItemData.setOriginData(originData);
            cartItemData.setUpdatedData(updatedData);
            cartItemDataList.add(cartItemData);
        }

        view.renderCartListData(cartItemDataList);
    }

    @Override
    public void processToShipmentStep() {
        List<CartItemData> cartItemDataList = extractCartItemList(view.getFinalCartList());
        Intent intent = CartShipmentActivity.createInstanceSingleAddress(
                view.getActivityContext(), cartItemDataList
        );
        view.navigateToActivity(intent);

    }

    @Override
    public void reCalculateSubTotal(List<CartItemHolderData> dataList) {
        double subtotalPrice = 0;
        int qty = 0;
        for (CartItemHolderData data : dataList) {
            qty = qty + data.getCartItemData().getUpdatedData().getQuantity();
            subtotalPrice = subtotalPrice + (data.getCartItemData().getUpdatedData().getQuantity() * data.getCartItemData().getOriginData().getPricePlan());
        }

        view.renderDetailInfoSubTotal(String.valueOf(qty), CurrencyFormatHelper.ConvertToRupiah(String.valueOf((int) subtotalPrice)));
    }

    private List<CartItemData> extractCartItemList(List<CartItemHolderData> finalCartList) {
        List<CartItemData> cartItemDataList = new ArrayList<>();
        for (CartItemHolderData data : finalCartList) {
            cartItemDataList.add(data.getCartItemData());
        }
        return cartItemDataList;
    }
}
