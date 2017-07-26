package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

/**
 * @author by nisie on 7/24/17.
 */

public class OfficialStoreCampaignProductViewModel {

    private Integer productId;
    private String name;
    private String price;
    private String imageSource;
    private String imageSourceSingle;
    private String url;
    private String shopName;
    private String shopAva;
    private boolean isFavorited;

    public OfficialStoreCampaignProductViewModel(Integer productId,
                                                 String name,
                                                 String price,
                                                 String imageSource,
                                                 String imageSourceSingle,
                                                 String url,
                                                 String shopName,
                                                 String shopAva,
                                                 boolean isFavorited) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
        this.imageSourceSingle = imageSourceSingle;
        this.url = url;
        this.shopName = shopName;
        this.shopAva = shopAva;
        this.isFavorited = isFavorited;
    }

    public Integer getProductId() {
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

    public boolean isFavorited() {
        return isFavorited;
    }
}
