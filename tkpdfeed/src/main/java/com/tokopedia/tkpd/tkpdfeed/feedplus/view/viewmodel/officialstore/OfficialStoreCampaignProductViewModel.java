package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.LabelsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/24/17.
 */

public class OfficialStoreCampaignProductViewModel {

    private int productId;
    private String name;
    private String price;
    private String imageSource;
    private String imageSourceSingle;
    private String url;
    private String shopName;
    private String shopAva;
    private String originalPrice;
    private int discount;
    private String shopUrl;
    private List<LabelsViewModel> labels;
    private boolean isFreeReturn;

    public OfficialStoreCampaignProductViewModel(int productId,
                                                 String name,
                                                 String price,
                                                 String originalPrice,
                                                 int discount,
                                                 String imageSource,
                                                 String imageSourceSingle,
                                                 String url,
                                                 String shopName,
                                                 String shopAva,
                                                 String shopUrl,
                                                 List<LabelsViewModel> labels,
                                                 boolean isFreeReturn) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.imageSource = imageSource;
        this.imageSourceSingle = imageSourceSingle;
        this.url = url;
        this.shopName = shopName;
        this.shopAva = shopAva;
        this.shopUrl = shopUrl;
        this.labels = labels;
        this.isFreeReturn = isFreeReturn;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getImageSourceSingle() {
        return imageSourceSingle;
    }

    public String getUrl() {
        return url;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopAva() {
        return shopAva;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public List<LabelsViewModel> getLabels() {
        return labels;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }
}
