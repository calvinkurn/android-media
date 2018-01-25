package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import java.util.List;

/**
 * @author by nisie on 11/8/17.
 */

public class KolRecommendationDomain {
    private final String headerTitle;
    private final String exploreLink;
    private final String exploreText;
    private final List<KolRecommendationItemDomain> listRecommendation;

    public KolRecommendationDomain(String headerTitle, String exploreLink, String exploreText,
                                   List<KolRecommendationItemDomain> listRecommendation) {
        this.headerTitle = headerTitle;
        this.exploreLink = exploreLink;
        this.exploreText = exploreText;
        this.listRecommendation = listRecommendation;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getExploreLink() {
        return exploreLink;
    }

    public String getExploreText() {
        return exploreText;
    }

    public List<KolRecommendationItemDomain> getListRecommendation() {
        return listRecommendation;
    }
}
