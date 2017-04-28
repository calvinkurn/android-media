package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.view.listener.ProductAddView;

import java.util.List;

/**
 * @author sebastianuskh on 4/28/17.
 */

public interface ProductPopulateView extends ProductAddView{

    void populateCategory(List<String> strings);

}
