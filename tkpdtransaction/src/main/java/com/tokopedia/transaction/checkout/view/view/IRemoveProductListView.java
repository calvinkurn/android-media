package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public interface IRemoveProductListView<T> {

    void showList(T t);

    void showListEmpty();

    void showError();

    TKPDMapParam<String,String> getGenerateParamAuth(TKPDMapParam<String, String> param);

    void renderSuccessDeletePartialCart(String message);

    void renderSuccessDeleteAllCart(String message);

    void renderOnFailureDeleteCart(String message);

    T getAllCartItemList();
}
