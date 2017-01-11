package com.tokopedia.seller.topads.model.request;

import android.text.TextUtils;

import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Nathaniel on 12/21/2016.
 */

public class SearchAdRequest {

    private String shopId;
    private Date startDate;
    private Date endDate;
    private String keyword;
    private int status;
    private int group;
    private int page;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        params.put(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        params.put(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        if (!TextUtils.isEmpty(keyword)) {
            params.put(TopAdsNetworkConstant.PARAM_KEYWORD, keyword);
        }
        if (status >= 0) {
            params.put(TopAdsNetworkConstant.PARAM_STATUS, String.valueOf(status));
        }
        params.put(TopAdsNetworkConstant.PARAM_GROUP, String.valueOf(group));
        if (page >= 0) {
            params.put(TopAdsNetworkConstant.PARAM_PAGE, String.valueOf(page));
        }
        return params;
    }

    public HashMap<String, String> getShopParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        params.put(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        params.put(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        return params;
    }
}
