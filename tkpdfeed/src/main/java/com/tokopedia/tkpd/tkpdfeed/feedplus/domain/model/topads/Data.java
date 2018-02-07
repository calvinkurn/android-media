package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

/**
 * Created by errysuprayogi on 3/27/17.
 * Copied to feed by milhamj 1/18/17.
 */
public class Data {
    public static final String DISPLAY_PRODUCT = "product";
    public static final String DISPLAY_SHOP = "shop";

    private String id;
    private String adRefKey;
    private String redirect;
    private String stickerId;
    private String stickerImage;
    private String productClickUrl;
    private String shopClickUrl;
    private Shop shop;
    private Product product;
    private boolean favorite;
    private String display;

    public Data(String id, String adRefKey, String redirect, String stickerId, String
            stickerImage, String productClickUrl, String shopClickUrl, Shop shop, Product
            product, boolean favorite, String display) {
        this.id = id;
        this.adRefKey = adRefKey;
        this.redirect = redirect;
        this.stickerId = stickerId;
        this.stickerImage = stickerImage;
        this.productClickUrl = productClickUrl;
        this.shopClickUrl = shopClickUrl;
        this.shop = shop;
        this.product = product;
        this.favorite = favorite;
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerImage() {
        return stickerImage;
    }

    public void setStickerImage(String stickerImage) {
        this.stickerImage = stickerImage;
    }

    public String getProductClickUrl() {
        return productClickUrl;
    }

    public void setProductClickUrl(String productClickUrl) {
        this.productClickUrl = productClickUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
