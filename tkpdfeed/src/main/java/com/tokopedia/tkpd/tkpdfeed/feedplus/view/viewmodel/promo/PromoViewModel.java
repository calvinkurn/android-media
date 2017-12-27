package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo;

import android.text.Spanned;

import com.tokopedia.core.util.MethodChecker;

/**
 * Created by stevenfredian on 5/16/17.
 */

public class PromoViewModel {

    private final int page;
    private String id;
    private String link;
    private String description;


    private String period;
    private String promoCode;
    private String imageUrl;
    private String name;

    public PromoViewModel(String id, String description, String period, String promoCode, String imageUrl,
                          String link, String name, int page) {
        this.id = id;
        this.description = description;
        this.period = period;
        this.promoCode = promoCode;
        this.imageUrl = imageUrl;
        this.link = link;
        this.name = name;
        this.page = page;
    }

    public PromoViewModel(int page) {
        this.page = page;
    }

    public String getDescription() {
        return description;
    }

    public Spanned getDescriptionSpanned() {
        return MethodChecker.fromHtml(getDescription());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPage() {
        return page;
    }

    public String getId() {
        return id;
    }
}
