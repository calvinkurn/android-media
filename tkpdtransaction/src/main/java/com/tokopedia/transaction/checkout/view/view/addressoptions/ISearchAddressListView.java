package com.tokopedia.transaction.checkout.view.view.addressoptions;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ISearchAddressListView<T> {

    void showList(T t);

    void showListEmpty();

    void showError();

}
