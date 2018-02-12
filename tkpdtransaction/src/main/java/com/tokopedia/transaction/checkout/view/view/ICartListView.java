package com.tokopedia.transaction.checkout.view.view;

import android.content.Context;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListView extends IBaseView {

    void renderCartListData(List<CartItemData> cartItemDataList);

    void renderErrorGetCartListData(String message);

    void renderErrorHttpGetCartListData(String message);

    void renderErrorNoConnectionGetCartListData(String message);

    void renderErrorTimeoutConnectionGetCartListData(String message);

    void renderEmptyCartData();

    void disableSwipeRefresh();

    void enableSwipeRefresh();

    List<CartItemHolderData> getFinalCartList();

    Context getActivityContext();

    void renderDetailInfoSubTotal(String qty, String subtotalPrice);
}
