package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Product {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_URI = "uri";
    private static final String KEY_RELATIVE_URI = "relative_uri";
    private static final String KEY_PRICE_FORMAT = "price_format";
    private static final String KEY_COUNT_TALK_FORMAT = "count_talk_format";
    private static final String KEY_COUNT_REVIEW_FORMAT = "count_review_format";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRODUCT_PREORDER = "product_preorder";
    private static final String KEY_PRODUCT_WHOLESALE = "product_wholesale";
    private static final String KEY_FREERETURN = "free_feturn";
    private static final String KEY_PRODUCT_CASHBACK = "product_cashback";
    private static final String KEY_PRODUCT_CASHBACK_RATE = "product_cashback_rate";
    private static final String KEY_PRODUCT_RATE = "product_rating";
    private static final String KEY_WHOLESALE_PRICE = "wholesale_price";
    private static final String KEY_LABELS = "labels";

    private String id;
    private String adRefKey;
    private String name;
    private ProductImage image;
    private String uri;
    private String relativeUri;
    private String priceFormat;
    private String countTalkFormat;
    private String countReviewFormat;
    private Category category;
    private boolean productPreorder;
    private boolean productWholesale;
    private String freeReturn;
    private boolean productCashback;
    private String productCashbackRate;
    private int productRating;
    private List<WholesalePrice> wholesalePrice = new ArrayList<>();
    private List<Label> labels = new ArrayList<>();

    public Product() {
    }

    public Product(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_NAME)){
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_IMAGE)){
            setImage(new ProductImage(object.getJSONObject(KEY_IMAGE)));
        }
        if(!object.isNull(KEY_URI)){
            setUri(object.getString(KEY_URI));
        }
        if(!object.isNull(KEY_RELATIVE_URI)){
            setRelativeUri(object.getString(KEY_RELATIVE_URI));
        }
        if(!object.isNull(KEY_PRICE_FORMAT)){
            setPriceFormat(object.getString(KEY_PRICE_FORMAT));
        }
        if(!object.isNull(KEY_COUNT_TALK_FORMAT)){
            setCountTalkFormat(object.getString(KEY_COUNT_TALK_FORMAT));
        }
        if(!object.isNull(KEY_COUNT_REVIEW_FORMAT)){
            setCountReviewFormat(object.getString(KEY_COUNT_REVIEW_FORMAT));
        }
        if(!object.isNull(KEY_CATEGORY)){
            setCategory(new Category(object.getJSONObject(KEY_CATEGORY)));
        }
        if(!object.isNull(KEY_PRODUCT_PREORDER)){
            setProductPreorder(object.getBoolean(KEY_PRODUCT_PREORDER));
        }
        if(!object.isNull(KEY_PRODUCT_WHOLESALE)){
            setProductWholesale(object.getBoolean(KEY_PRODUCT_WHOLESALE));
        }
        if(!object.isNull(KEY_FREERETURN)){
            setFreeReturn(object.getString(KEY_FREERETURN));
        }
        if(!object.isNull(KEY_PRODUCT_CASHBACK)){
            setProductCashback(object.getBoolean(KEY_PRODUCT_CASHBACK));
        }
        if(!object.isNull(KEY_PRODUCT_CASHBACK_RATE)){
            setProductCashbackRate(object.getString(KEY_PRODUCT_CASHBACK_RATE));
        }
        if(!object.isNull(KEY_PRODUCT_RATE)){
            setProductRating(object.getInt(KEY_PRODUCT_RATE));
        }
        if(!object.isNull(KEY_WHOLESALE_PRICE)){
            JSONArray wholesalePriceArray = object.getJSONArray(KEY_WHOLESALE_PRICE);
            for (int i = 0; i < wholesalePriceArray.length(); i++) {
                wholesalePrice.add(new WholesalePrice(wholesalePriceArray.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_LABELS)) {
            JSONArray labelArray = object.getJSONArray(KEY_LABELS);
            for (int i = 0; i < labelArray.length(); i++) {
                labels.add(new Label(labelArray.getJSONObject(i)));
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductImage getImage() {
        return image;
    }

    public void setImage(ProductImage image) {
        this.image = image;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRelativeUri() {
        return relativeUri;
    }

    public void setRelativeUri(String relativeUri) {
        this.relativeUri = relativeUri;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }

    public String getCountTalkFormat() {
        return countTalkFormat;
    }

    public void setCountTalkFormat(String countTalkFormat) {
        this.countTalkFormat = countTalkFormat;
    }

    public String getCountReviewFormat() {
        return countReviewFormat;
    }

    public void setCountReviewFormat(String countReviewFormat) {
        this.countReviewFormat = countReviewFormat;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(boolean productPreorder) {
        this.productPreorder = productPreorder;
    }

    public boolean isProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(boolean productWholesale) {
        this.productWholesale = productWholesale;
    }

    public String getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(String freeReturn) {
        this.freeReturn = freeReturn;
    }

    public boolean isProductCashback() {
        return productCashback;
    }

    public void setProductCashback(boolean productCashback) {
        this.productCashback = productCashback;
    }

    public String getProductCashbackRate() {
        return productCashbackRate;
    }

    public void setProductCashbackRate(String productCashbackRate) {
        this.productCashbackRate = productCashbackRate;
    }

    public List<WholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }
}
