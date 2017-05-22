package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model;

import com.tkpdfeed.feeds.Feeds;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class ContentFeedDomain {

    private final @Nullable
    String type;

    private final @Nullable Integer totalProduct;

    private final @Nullable
    List<ProductFeedDomain> products;

    private final @Nullable List<PromotionFeedDomain> promotions;

    private final @Nullable String statusActivity;

    public ContentFeedDomain(@Nullable String type, @Nullable Integer total_product,
                   @Nullable List<ProductFeedDomain> products, @Nullable List<PromotionFeedDomain> promotions,
                   @Nullable String status_activity) {
        this.type = type;
        this.totalProduct = total_product;
        this.products = products;
        this.promotions = promotions;
        this.statusActivity = status_activity;
    }

    @Nullable
    public Integer getTotalProduct() {
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
}
