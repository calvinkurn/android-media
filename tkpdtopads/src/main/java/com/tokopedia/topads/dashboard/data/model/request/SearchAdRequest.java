package com.tokopedia.topads.dashboard.data.model.request;

import android.text.TextUtils;

import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

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
    private long group;
    private int page;
    private String adId;
    private String groupId;

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

    public long getGroup() {
        return group;
    }

    public void setGroup(long group) {
        this.group = group;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        if (startDate != null) {
            params.put(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        }
        if (endDate != null) {
            params.put(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        }
        if (!TextUtils.isEmpty(groupId)) {
            params.put(TopAdsNetworkConstant.PARAM_GROUP_ID, groupId);
            return params;
        }
        if (!TextUtils.isEmpty(adId)) {
            params.put(TopAdsNetworkConstant.PARAM_AD_ID, adId);
            return params;
        }
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
