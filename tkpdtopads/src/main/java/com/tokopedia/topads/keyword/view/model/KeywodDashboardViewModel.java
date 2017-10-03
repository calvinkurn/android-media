package com.tokopedia.topads.keyword.view.model;

import com.tokopedia.topads.keyword.domain.model.Page;

import java.util.List;

/**
 * Created by normansyahputa on 6/2/17.
 */

public class KeywodDashboardViewModel {
    private Page page;
    private List<KeywordAd> data = null;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<KeywordAd> getData() {
        return data;
    }

    public void setData(List<KeywordAd> data) {
        this.data = data;
    }
}
