package com.tokopedia.seller.topads.model.request;

import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Nathaniel on 11/25/2016.
 */

public class StatisticRequest {

    private String shopId;
    private int type;
    private Date startDate;
    private Date endDate;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getFormattedStartDate() {
        return new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate);
    }

    public String getFormattedEndDate() {
        return new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate);
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        params.put(TopAdsNetworkConstant.PARAM_TYPE, String.valueOf(type));
        params.put(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        params.put(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(TopAdsConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate));
        return params;
    }
}