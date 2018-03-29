package com.tokopedia.shop.product.view.model;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductLimitedProductViewModel implements ShopProductBaseViewModel {
    private PagingList<ShopProductViewModel> shopProductViewModelList;

    public PagingList<ShopProductViewModel> getShopProductViewModelList() {
        return shopProductViewModelList;
    }

    public void setShopProductViewModelList(PagingList<ShopProductViewModel> shopProductViewModelList) {
        this.shopProductViewModelList = shopProductViewModelList;
    }

    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
