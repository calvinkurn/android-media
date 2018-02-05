package com.tokopedia.transaction.checkout.view.view;

/**
 * @author Aghny A. Putra on 05/02/18
 */
public interface IRemoveProductListView<T> {

    void showList(T t);

    void showListEmpty();

    void showError();

}
