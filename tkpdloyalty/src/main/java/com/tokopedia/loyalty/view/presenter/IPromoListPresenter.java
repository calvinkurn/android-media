package com.tokopedia.loyalty.view.presenter;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public interface IPromoListPresenter {

    void processGetPromoList(String subCategories);

    void processGetPromoListLoadMore(String subCategories);

    void setPage(int page);
}
