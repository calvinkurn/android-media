package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.TopPicksDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreDomain;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class ContentFeedDomain {

    @Nullable
    private final
    String type;

    @Nullable
    private final
    int totalProduct;

    @Nullable
    private final
    List<ProductFeedDomain> products;

    @Nullable
    private final
    List<PromotionFeedDomain> promotions;

    @Nullable
    private final
    String statusActivity;

    @Nullable
    private final List<OfficialStoreDomain> officialStores;

    @Nullable
    private final List<TopPicksDomain> topPicksDomains;

    public ContentFeedDomain(@Nullable String type, @Nullable int total_product,
                             @Nullable List<ProductFeedDomain> products,
                             @Nullable List<PromotionFeedDomain> promotions,
                             @Nullable List<OfficialStoreDomain> officialStores,
                             @Nullable List<TopPicksDomain> topPicksDomains,
                             @Nullable String status_activity) {
        this.type = type;
        this.totalProduct = total_product;
        this.products = products;
        this.promotions = promotions;
        this.statusActivity = status_activity;
        this.topPicksDomains = topPicksDomains;
        this.officialStores = officialStores;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public int getTotalProduct() {
        return totalProduct;
    }

    @Nullable
    public List<ProductFeedDomain> getProducts() {
        return products;
    }

    @Nullable
    public List<PromotionFeedDomain> getPromotions() {
        return promotions;
    }

    @Nullable
    public String getStatusActivity() {
        return statusActivity;
    }

    @Nullable
    public List<OfficialStoreDomain> getOfficialStores() {
        return officialStores;
    }

    @Nullable
    public List<TopPicksDomain> getTopPicksDomains() {
        return topPicksDomains;
    }
}
