package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product;

/**
 * @author by nisie on 5/16/17.
 */

public class ProductFeedViewModel {

    private final int page;
    private String priceInt;
    private Integer productId;
    private String name;
    private String price;
    private String imageSource;
    private String imageSourceSingle;
    private String url;
    private String shopName;
    private String shopAva;
    private boolean isFavorited;

    public ProductFeedViewModel(Integer productId,
                                String name,
                                String price,
                                String imageSource,
                                String imageSourceSingle,
                                String url,
                                String shopName,
                                String shopAva,
                                boolean isFavorited,
                                String priceInt,
                                int page) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
        this.imageSourceSingle = imageSourceSingle;
        this.url = url;
        this.shopName = shopName;
        this.shopAva = shopAva;
        this.isFavorited = isFavorited;
        this.priceInt = priceInt;
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getImageSourceSingle() {
        return imageSourceSingle;
    }

    public void setImageSourceSingle(String imageSourceSingle) {
        this.imageSourceSingle = imageSourceSingle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAva() {
        return shopAva;
    }

    public void setShopAva(String shopAva) {
        this.shopAva = shopAva;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getPage() {
        return page;
    }

    public String getPriceInt() {
        return priceInt;
    }

    public void setPriceInt(String priceInt) {
        this.priceInt = priceInt;
    }
}
