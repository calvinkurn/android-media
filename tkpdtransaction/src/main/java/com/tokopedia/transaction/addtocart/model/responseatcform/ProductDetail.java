package com.tokopedia.transaction.addtocart.model.responseatcform;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail implements Parcelable {

    @SerializedName("product_picture")
    @Expose
    private String productPicture;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_cat_name")
    @Expose
    private String productCatName;
    @SerializedName("product_cat_name_tracking")
    @Expose
    private String productCatNameTracking;
    @SerializedName("product_min_order")
    @Expose
    private String productMinOrder;
    @SerializedName("product_must_insurance")
    @Expose
    private Integer productMustInsurance;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_cat_id")
    @Expose
    private String productCatId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_weight_gram")
    @Expose
    private String productWeightGram;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder;

    public String getProductCatNameTracking() {
        return productCatNameTracking;
    }

    public void setProductCatNameTracking(String productCatNameTracking) {
        this.productCatNameTracking = productCatNameTracking;
    }

    /**
     * @return The productPicture
     */
    public String getProductPicture() {
        return productPicture;
    }

    /**
     * @param productPicture The product_picture
     */
    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    /**
     * @return The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     * @param productPrice The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * @return The productCatName
     */
    public String getProductCatName() {
        return productCatName;
    }

    /**
     * @param productCatName The product_cat_name
     */
    public void setProductCatName(String productCatName) {
        this.productCatName = productCatName;
    }

    /**
     * @return The productMinOrder
     */
    public String getProductMinOrder() {
        return productMinOrder;
    }

    /**
     * @param productMinOrder The product_min_order
     */
    public void setProductMinOrder(String productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    /**
     * @return The productMustInsurance
     */
    public Integer getProductMustInsurance() {
        return productMustInsurance;
    }

    /**
     * @param productMustInsurance The product_must_insurance
     */
    public void setProductMustInsurance(Integer productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    /**
     * @return The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId The product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * @return The productWeight
     */
    public String getProductWeight() {
        return productWeight;
    }

    /**
     * @param productWeight The product_weight
     */
    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    /**
     * @return The productCatId
     */
    public String getProductCatId() {
        return productCatId;
    }

    /**
     * @param productCatId The product_cat_id
     */
    public void setProductCatId(String productCatId) {
        this.productCatId = productCatId;
    }

    /**
     * @return The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return The productWeightGram
     */
    public String getProductWeightGram() {
        return productWeightGram;
    }

    /**
     * @param productWeightGram The product_weight_gram
     */
    public void setProductWeightGram(String productWeightGram) {
        this.productWeightGram = productWeightGram;
    }

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(ProductPreorder productPreorder) {
        this.productPreorder = productPreorder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productPicture);
        dest.writeString(this.productPrice);
        dest.writeString(this.productCatName);
        dest.writeString(this.productCatNameTracking);
        dest.writeString(this.productMinOrder);
        dest.writeValue(this.productMustInsurance);
        dest.writeString(this.productId);
        dest.writeString(this.productWeight);
        dest.writeString(this.productCatId);
        dest.writeString(this.productName);
        dest.writeString(this.productWeightGram);
        dest.writeParcelable(this.productPreorder, flags);
    }

    public ProductDetail() {
    }

    protected ProductDetail(Parcel in) {
        this.productPicture = in.readString();
        this.productPrice = in.readString();
        this.productCatName = in.readString();
        this.productCatNameTracking = in.readString();
        this.productMinOrder = in.readString();
        this.productMustInsurance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = in.readString();
        this.productWeight = in.readString();
        this.productCatId = in.readString();
        this.productName = in.readString();
        this.productWeightGram = in.readString();
        this.productPreorder = in.readParcelable(ProductPreorder.class.getClassLoader());
    }

    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel source) {
            return new ProductDetail(source);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };
}
