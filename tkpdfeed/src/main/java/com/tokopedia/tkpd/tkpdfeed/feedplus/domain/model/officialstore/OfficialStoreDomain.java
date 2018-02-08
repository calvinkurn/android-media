package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author by nisie on 7/26/17.
 */

public class OfficialStoreDomain {

    private final
    @Nullable
    int shop_id;

    private final
    @Nullable
    String shop_apps_url;

    private final
    @Nullable
    String shop_name;

    private final
    @Nullable
    String logo_url;

    private final
    @Nullable
    String microsite_url;

    private final
    @Nullable
    String brand_img_url;

    private final
    @Nullable
    boolean is_owner;

    private final
    @Nullable
    String shop_tagline;

    private final
    @Nullable
    boolean is_new;

    private final
    @Nullable
    String title;

    private final
    @Nullable
    String mobile_img_url;

    private final
    @Nullable
    String feed_hexa_color;

    private final
    @Nullable
    String redirect_url_app;

    private final
    @Nullable
    List<OfficialStoreProductDomain> products;


    public OfficialStoreDomain(int shop_id, String shop_apps_url, String shop_name,
                               String logo_url, String microsite_url, String brand_img_url,
                               boolean is_owner, String shop_tagline, boolean is_new,
                               String title, String mobile_img_url, String feed_hexa_color,
                               String redirect_url_app, List<OfficialStoreProductDomain> products) {
        this.shop_id = shop_id;
        this.shop_apps_url = shop_apps_url;
        this.shop_name = shop_name;
        this.logo_url = logo_url;
        this.microsite_url = microsite_url;
        this.brand_img_url = brand_img_url;
        this.is_owner = is_owner;
        this.shop_tagline = shop_tagline;
        this.is_new = is_new;
        this.title = title;
        this.mobile_img_url = mobile_img_url;
        this.feed_hexa_color = feed_hexa_color;
        this.redirect_url_app = redirect_url_app;
        this.products = products;
    }

    @Nullable
    public int getShop_id() {
        return shop_id;
    }

    @Nullable
    public String getShop_apps_url() {
        return shop_apps_url;
    }

    @Nullable
    public String getShop_name() {
        return shop_name;
    }

    @Nullable
    public String getLogo_url() {
        return logo_url;
    }

    @Nullable
    public String getMicrosite_url() {
        return microsite_url;
    }

    @Nullable
    public String getBrand_img_url() {
        return brand_img_url;
    }

    @Nullable
    public boolean getIs_owner() {
        return is_owner;
    }

    @Nullable
    public String getShop_tagline() {
        return shop_tagline;
    }

    @Nullable
    public boolean getIs_new() {
        return is_new;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getMobile_img_url() {
        return mobile_img_url;
    }

    @Nullable
    public String getFeed_hexa_color() {
        return feed_hexa_color;
    }

    @Nullable
    public String getRedirect_url_app() {
        return redirect_url_app;
    }

    @Nullable
    public List<OfficialStoreProductDomain> getProducts() {
        return products;
    }
}
