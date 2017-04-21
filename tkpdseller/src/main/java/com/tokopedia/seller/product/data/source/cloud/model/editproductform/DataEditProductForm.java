
package com.tokopedia.seller.product.data.source.cloud.model.editproductform;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataEditProductForm {

    @SerializedName("shop_is_gold")
    @Expose
    private int shopIsGold;
    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("catalog")
    @Expose
    private Catalog catalog;
    @SerializedName("preorder")
    @Expose
    private Preorder preorder;
    @SerializedName("server_id")
    @Expose
    private String serverId;
    @SerializedName("wholesale_price")
    @Expose
    private List<Object> wholesalePrice = null;
    @SerializedName("etalase")
    @Expose
    private List<Etalase> etalase = null;
    @SerializedName("breadcrumb")
    @Expose
    private List<Breadcrumb> breadcrumb = null;
    @SerializedName("product")
    @Expose
    private ProductEditForm product;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = null;
    @SerializedName("condition")
    @Expose
    private List<Condition> condition = null;

    public int getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Preorder getPreorder() {
        return preorder;
    }

    public void setPreorder(Preorder preorder) {
        this.preorder = preorder;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public List<Object> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<Object> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public List<Etalase> getEtalase() {
        return etalase;
    }

    public void setEtalase(List<Etalase> etalase) {
        this.etalase = etalase;
    }

    public List<Breadcrumb> getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(List<Breadcrumb> breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductEditForm getProduct() {
        return product;
    }

    public void setProduct(ProductEditForm product) {
        this.product = product;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public List<Condition> getCondition() {
        return condition;
    }

    public void setCondition(List<Condition> condition) {
        this.condition = condition;
    }

}
