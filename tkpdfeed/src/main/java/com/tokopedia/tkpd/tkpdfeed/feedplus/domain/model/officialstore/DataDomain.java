package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author by nisie on 7/26/17.
 */

public class DataDomain {

    private final
    @Nullable
    Integer id;

    private final
    @Nullable
    String name;

    private final
    @Nullable
    String url_app;

    private final
    @Nullable
    String image_url;

    private final
    @Nullable
    String image_url_700;

    private final
    @Nullable
    String price;

    private final
    @Nullable
    ShopDomain shop;

    private final
    @Nullable
    String original_price;

    private final
    @Nullable
    Integer discount_percentage;

    private final
    @Nullable
    String discount_expired;

    private final
    @Nullable
    List<BadgeDomain> badges;

    private final
    @Nullable
    List<LabelDomain> labels;

    public DataDomain(Integer id, String name, String url_app,
                      String image_url, String image_url_700,
                      String price, ShopDomain shop, String original_price,
                      Integer discount_percentage, String discount_expired,
                      List<BadgeDomain> badges, List<LabelDomain> labels) {
        this.id = id;
        this.name = name;
        this.url_app = url_app;
        this.image_url = image_url;
        this.image_url_700 = image_url_700;
        this.price = price;
        this.shop = shop;
        this.original_price = original_price;
        this.discount_percentage = discount_percentage;
        this.discount_expired = discount_expired;
        this.badges = badges;
        this.labels = labels;
    }
}
