package com.tokopedia.gm.statistic.domain;

import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.gm.statistic.data.source.cloud.model.graph.GetShopCategory;

import java.util.List;

import retrofit2.Response;

/**
 * Created by normansyahputa on 7/20/17.
 */

public class KeywordModel {
    private List<GetKeyword> keywords;
    private GetShopCategory shopCategory;
    private List<Response<GetKeyword>> responseList;
    private List<String> categoryName;

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

    public List<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryNames(List<String> categoryName) {
        this.categoryName = categoryName;
    }
}
