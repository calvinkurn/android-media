package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public interface IRemoveProductListView<T> {

    void showList(T t);

    void showListEmpty();

    void showError();

    T getSelectedCartList();

    TKPDMapParam<String,String> getGenerateParamAuth(TKPDMapParam<String, String> param);

    void renderSuccessDeleteCart(String message);

    void renderOnFailureDeleteCart(String message);

}
