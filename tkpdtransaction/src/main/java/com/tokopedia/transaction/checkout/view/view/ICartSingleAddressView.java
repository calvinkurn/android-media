package com.tokopedia.transaction.checkout.view.view;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ICartSingleAddressView<T> {

    void show(T t);

    void showError();

}
