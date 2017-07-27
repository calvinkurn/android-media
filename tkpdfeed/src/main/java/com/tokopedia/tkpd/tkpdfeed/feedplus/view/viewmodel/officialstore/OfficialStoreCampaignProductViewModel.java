package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

import android.graphics.PointF;

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
    private String cashback;
    private boolean wholesale;
    private boolean preorder;
    private String shopUrl;


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
                                                 String cashback,
                                                 boolean wholesale,
                                                 boolean preorder,
                                                 String shopUrl) {
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
        this.cashback = cashback;
        this.wholesale = wholesale;
        this.preorder = preorder;
        this.shopUrl = shopUrl;
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

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public boolean isWholesale() {
        return wholesale;
    }

    public void setWholesale(boolean wholesale) {
        this.wholesale = wholesale;
    }

    public boolean isPreorder() {
        return preorder;
    }

    public void setPreorder(boolean preorder) {
        this.preorder = preorder;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }
}
