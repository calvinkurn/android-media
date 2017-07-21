package com.tokopedia.seller.goldmerchant.statistic.domain;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.seller.gmstat.models.GetKeyword;
import com.tokopedia.seller.gmstat.models.GetShopCategory;

import java.util.List;

import retrofit2.Response;

/**
 * Created by normansyahputa on 7/20/17.
 */

public class KeywordModel {
    private List<GetKeyword> keywords;
    private GetShopCategory shopCategory;
    private List<Response<GetKeyword>> responseList;
    private List<HadesV1Model> hadesv1Models;

    public List<GetKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<GetKeyword> keywords) {
        this.keywords = keywords;
    }

    public GetShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(GetShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

    public List<Response<GetKeyword>> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<Response<GetKeyword>> responseList) {
        this.responseList = responseList;
    }

    public List<HadesV1Model> getHadesv1Models() {
        return hadesv1Models;
    }

    public void setHadesv1Models(List<HadesV1Model> hadesv1Models) {
        this.hadesv1Models = hadesv1Models;
    }
}
