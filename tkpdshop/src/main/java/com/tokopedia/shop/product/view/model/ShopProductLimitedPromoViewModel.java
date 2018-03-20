package com.tokopedia.shop.product.view.model;

import com.tokopedia.shop.product.view.adapter.ShopProductLimitedAdapterTypeFactory;
import com.tokopedia.shop.product.view.listener.ShopProductUserVisibleHintListener;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ShopProductLimitedPromoViewModel implements ShopProductBaseViewModel {

    private String url;
    private boolean login;
    private String userId;
    private ShopProductUserVisibleHintListener shopProductUserVisibleHintListener;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ShopProductUserVisibleHintListener getShopProductUserVisibleHintListener() {
        return shopProductUserVisibleHintListener;
    }

    public void setShopProductUserVisibleHintListener(ShopProductUserVisibleHintListener shopProductUserVisibleHintListener) {
        this.shopProductUserVisibleHintListener = shopProductUserVisibleHintListener;
    }

    @Override
    public int type(ShopProductLimitedAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
