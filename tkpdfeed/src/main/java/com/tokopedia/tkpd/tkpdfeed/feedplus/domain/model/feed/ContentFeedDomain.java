package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.TopPicksDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore.OfficialStoreDomain;
import com.tokopedia.topads.sdk.domain.model.Data;

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

    @Nullable
    private final List<InspirationDomain> inspirationDomains;

    @Nullable
    private final List<Data> topAdsList;

    @Nullable
    private final KolPostDomain kolPostDomain;

    @Nullable
    private final KolRecommendationDomain kolRecommendations;

    @Nullable
    private final FavoriteCtaDomain favoriteCtaDomain;

    @Nullable
    private final KolCtaDomain kolCtaDomain;


    public ContentFeedDomain(@Nullable String type, @Nullable int total_product,
                             @Nullable List<ProductFeedDomain> products,
                             @Nullable List<PromotionFeedDomain> promotions,
                             @Nullable List<OfficialStoreDomain> officialStores,
                             @Nullable List<TopPicksDomain> topPicksDomains,
                             @Nullable List<InspirationDomain> inspirationDomains,
                             @Nullable List<Data> topAdsList,
                             @Nullable KolPostDomain kolPostDomain,
                             @Nullable KolRecommendationDomain kolRecommendations,
                             @Nullable FavoriteCtaDomain favoriteCtaDomain,
                             @Nullable KolCtaDomain kolCtaDomain,
                             @Nullable String status_activity) {
        this.type = type;
        this.totalProduct = total_product;
        this.products = products;
        this.promotions = promotions;
        this.statusActivity = status_activity;
        this.topPicksDomains = topPicksDomains;
        this.officialStores = officialStores;
        this.inspirationDomains = inspirationDomains;
        this.topAdsList = topAdsList;
        this.kolPostDomain = kolPostDomain;
        this.kolRecommendations = kolRecommendations;
        this.favoriteCtaDomain = favoriteCtaDomain;
        this.kolCtaDomain = kolCtaDomain;
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

    @Nullable
    public List<InspirationDomain> getInspirationDomains() {
        return inspirationDomains;
    }

    @Nullable
    public KolPostDomain getKolPostDomain() {
        return kolPostDomain;
    }

    @Nullable
    public KolRecommendationDomain getKolRecommendations() {
        return kolRecommendations;
    }

    @Nullable
    public FavoriteCtaDomain getFavoriteCtaDomain() {
        return favoriteCtaDomain;
    }

    @Nullable
    public KolCtaDomain getKolCtaDomain() {
        return kolCtaDomain;
    }

    @Nullable
    public List<Data> getTopAdsList() {
        return topAdsList;
    }
}
