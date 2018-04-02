package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class Data {

    private static final String KEY_ID = "id";
    private static final String KEY_AD_REF = "id";
    private static final String KEY_REDIRECT = "redirect";
    private static final String KEY_STICKER_ID = "sticker_id";
    private static final String KEY_STICKER_IMAGE = "sticker_image";
    private static final String KEY_PRODUCT_CLICK_URL = "product_click_url";
    private static final String KEY_SHOP_CLICK_URL = "shop_click_url";
    private static final String KEY_SHOP = "shop";
    private static final String KEY_PRODUCT = "product";

    private String id;
    private String adRefKey;
    private String redirect;
    private String stickerId;
    private String stickerImage;
    private String productClickUrl;
    private String shopClickUrl;
    private Shop shop;
    private Product product;
    private boolean favorit;

    public Data() {
    }

    public Data(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)) {
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_AD_REF)) {
            setAdRefKey(object.getString(KEY_AD_REF));
        }
        if(!object.isNull(KEY_REDIRECT)) {
            setRedirect(object.getString(KEY_REDIRECT));
        }
        if(!object.isNull(KEY_STICKER_ID)) {
            setStickerId(object.getString(KEY_STICKER_ID));
        }
        if(!object.isNull(KEY_STICKER_IMAGE)) {
            setStickerImage(object.getString(KEY_STICKER_IMAGE));
        }
        if(!object.isNull(KEY_PRODUCT_CLICK_URL)) {
            setProductClickUrl(object.getString(KEY_PRODUCT_CLICK_URL));
        }
        if(!object.isNull(KEY_SHOP_CLICK_URL)) {
            setShopClickUrl(object.getString(KEY_SHOP_CLICK_URL));
        }
        if(!object.isNull(KEY_PRODUCT)) {
            setProduct(new Product(object.getJSONObject(KEY_PRODUCT)));
        }
        if(!object.isNull(KEY_SHOP)) {
            setShop(new Shop(object.getJSONObject(KEY_SHOP)));
        }
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

    public boolean isFavorit() {
        return favorit;
    }

    public void setFavorit(boolean favorit) {
        this.favorit = favorit;
    }
}
