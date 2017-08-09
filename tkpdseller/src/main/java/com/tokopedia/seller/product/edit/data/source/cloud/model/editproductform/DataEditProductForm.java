
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private List<WholesalePrice> wholesalePriceList = null;
    @SerializedName("etalase")
    @Expose
    private List<Etalase> etalaseList = null;
    @SerializedName("breadcrumb")
    @Expose
    private List<Breadcrumb> breadcrumbList = null;
    @SerializedName("product")
    @Expose
    private ProductEditForm product;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImageList = null;
    @SerializedName("condition")
    @Expose
    private List<Condition> conditionList = null;

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

    public List<WholesalePrice> getWholesalePriceList() {
        return wholesalePriceList;
    }

    public void setWholesalePriceList(List<WholesalePrice> wholesalePriceList) {
        this.wholesalePriceList = wholesalePriceList;
    }

    public List<Etalase> getEtalaseList() {
        return etalaseList;
    }

    public void setEtalaseList(List<Etalase> etalaseList) {
        this.etalaseList = etalaseList;
    }

    public List<Breadcrumb> getBreadcrumbList() {
        return breadcrumbList;
    }

    public void setBreadcrumbList(List<Breadcrumb> breadcrumbList) {
        this.breadcrumbList = breadcrumbList;
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

    public List<ProductImage> getProductImageList() {
        return productImageList;
    }

    public void setProductImageList(List<ProductImage> productImageList) {
        this.productImageList = productImageList;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

}
