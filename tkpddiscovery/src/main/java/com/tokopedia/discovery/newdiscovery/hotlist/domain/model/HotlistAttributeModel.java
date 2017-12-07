package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

import java.util.List;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistAttributeModel {
    private List<HotlistHashtagModel> hastTags;
    private List<HotlistBreadcrumb> breadCrumbs;

    public void setHastTags(List<HotlistHashtagModel> hastTags) {
        this.hastTags = hastTags;
    }

    public List<HotlistHashtagModel> getHastTags() {
        return hastTags;
    }

    public void setBreadCrumbs(List<HotlistBreadcrumb> breadCrumbs) {
        this.breadCrumbs = breadCrumbs;
    }

    public List<HotlistBreadcrumb> getBreadCrumbs() {
        return breadCrumbs;
    }
}
