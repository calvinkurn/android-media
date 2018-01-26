package com.tokopedia.transaction.checkout.view.view;

public interface ISearchAddressListView<T> {

    void showList(T t);

    void showListEmpty();

    void showError();

}
