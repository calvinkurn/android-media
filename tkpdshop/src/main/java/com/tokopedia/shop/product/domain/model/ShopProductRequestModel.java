package com.tokopedia.shop.product.domain.model;

import android.text.TextUtils;

import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.constant.ShopParamApiConstant;

import java.util.HashMap;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductRequestModel {

    private String shopId;
    private String keyword;
    private String etalaseId;
    private int page;
    private int orderBy;
    private int perPage;
    private int wholesale;

    private boolean shopClosed;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getWholesale() {
        return wholesale;
    }

    public void setWholesale(int wholesale) {
        this.wholesale = wholesale;
    }

    public boolean isShopClosed() {
        return shopClosed;
    }

    public void setShopClosed(boolean shopClosed) {
        this.shopClosed = shopClosed;
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(shopId)) {
            hashMap.put(ShopCommonParamApiConstant.SHOP_ID, shopId);
        }
        if (!TextUtils.isEmpty(keyword)) {
            hashMap.put(ShopParamApiConstant.KEYWORD, keyword);
        }
        if (!TextUtils.isEmpty(etalaseId)) {
            hashMap.put(ShopParamApiConstant.ETALASE_ID, etalaseId);
        }

        hashMap.put(ShopParamApiConstant.PAGE, String.valueOf(page));

        if (orderBy > 0) {
            hashMap.put(ShopParamApiConstant.ORDER_BY, String.valueOf(orderBy));
        }
        if (perPage > 0) {
            hashMap.put(ShopParamApiConstant.PER_PAGE, String.valueOf(perPage));
        }
        if (wholesale > 0) {
            hashMap.put(ShopParamApiConstant.WHOLESALE, String.valueOf(wholesale));
        }
        return hashMap;
    }
}
