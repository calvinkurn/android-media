package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartProduct implements Parcelable {

    @SerializedName("product_total_weight")
    @Expose
    private String productTotalWeight;
    @SerializedName("product_error_msg")
    @Expose
    private String productErrorMsg;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_hide_edit")
    @Expose
    private Integer productHideEdit;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_quantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("product_must_insurance")
    @Expose
    private String productMustInsurance;
    @SerializedName("product_cart_id")
    @Expose
    private String productCartId;
    @SerializedName("product_price_currency_value")
    @Expose
    private Integer productPriceCurrencyValue;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_pic")
    @Expose
    private String productPic;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("product_price_currency")
    @Expose
    private String productPriceCurrency;
    @SerializedName("product_returnable")
    @Expose
    private Integer productReturnable;
    @SerializedName("product_total_price")
    @Expose
    private String productTotalPrice;
    @SerializedName("product_price_last")
    @Expose
    private String productPriceLast;
    @SerializedName("product_price_updated")
    @Expose
    private String productPriceUpdated;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_price_idr")
    @Expose
    private String productPriceIdr;
    @SerializedName("product_price_original")
    @Expose
    private String productPriceOriginal;
    @SerializedName("product_use_insurance")
    @Expose
    private Integer productUseInsurance;
    @SerializedName("product_total_price_idr")
    @Expose
    private String productTotalPriceIdr;
    @SerializedName("product_min_order")
    @Expose
    private String productMinOrder;
    @SerializedName("home_attribution")
    @Expose
    private String homeAttribution;
    @SerializedName("list_name_product")
    @Expose
    private String listNameProduct;


    public String getProductTotalWeight() {
        return productTotalWeight;
    }

    public void setProductTotalWeight(String productTotalWeight) {
        this.productTotalWeight = productTotalWeight;
    }

    public String getProductErrorMsg() {
        return productErrorMsg;
    }

    public void setProductErrorMsg(String productErrorMsg) {
        this.productErrorMsg = productErrorMsg;
    }

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(ProductPreorder productPreorder) {
        this.productPreorder = productPreorder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getProductHideEdit() {
        return productHideEdit;
    }

    public void setProductHideEdit(Integer product_hide_edit) {
        this.productHideEdit = product_hide_edit;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(String productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public String getProductCartId() {
        return productCartId;
    }

    public void setProductCartId(String productCartId) {
        this.productCartId = productCartId;
    }

    public Integer getProductPriceCurrencyValue() {
        return productPriceCurrencyValue;
    }

    public void setProductPriceCurrencyValue(Integer productPriceCurrencyValue) {
        this.productPriceCurrencyValue = productPriceCurrencyValue;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(String productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public Integer getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(Integer productReturnable) {
        this.productReturnable = productReturnable;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getProductPriceLast() {
        return productPriceLast;
    }

    public void setProductPriceLast(String productPriceLast) {
        this.productPriceLast = productPriceLast;
    }

    public String getProductPriceUpdated() {
        return productPriceUpdated;
    }

    public void setProductPriceUpdated(String productPriceUpdated) {
        this.productPriceUpdated = productPriceUpdated;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getProductPriceIdr() {
        return productPriceIdr;
    }

    public void setProductPriceIdr(String productPriceIdr) {
        this.productPriceIdr = productPriceIdr;
    }

    public String getProductPriceOriginal() {
        return productPriceOriginal;
    }

    public void setProductPriceOriginal(String productPriceOriginal) {
        this.productPriceOriginal = productPriceOriginal;
    }

    public Integer getProductUseInsurance() {
        return productUseInsurance;
    }

    public void setProductUseInsurance(Integer productUseInsurance) {
        this.productUseInsurance = productUseInsurance;
    }

    public String getProductTotalPriceIdr() {
        return productTotalPriceIdr;
    }

    public void setProductTotalPriceIdr(String productTotalPriceIdr) {
        this.productTotalPriceIdr = productTotalPriceIdr;
    }

    public String getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(String productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public String getHomeAttribution() {
        return homeAttribution;
    }

    public void setHomeAttribution(String homeAttribution) {
        this.homeAttribution = homeAttribution;
    }

    public String getListNameProduct() {
        return listNameProduct;
    }

    public void setListNameProduct(String listNameProduct) {
        this.listNameProduct = listNameProduct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productTotalWeight);
        dest.writeString(this.productErrorMsg);
        dest.writeParcelable(this.productPreorder, flags);
        dest.writeString(this.productId);
        dest.writeValue(this.productHideEdit);
        dest.writeString(this.productPrice);
        dest.writeString(this.productName);
        dest.writeValue(this.productQuantity);
        dest.writeString(this.productMustInsurance);
        dest.writeString(this.productCartId);
        dest.writeValue(this.productPriceCurrencyValue);
        dest.writeString(this.productUrl);
        dest.writeString(this.productWeight);
        dest.writeString(this.productPic);
        dest.writeString(this.productStatus);
        dest.writeString(this.productPriceCurrency);
        dest.writeValue(this.productReturnable);
        dest.writeString(this.productTotalPrice);
        dest.writeString(this.productPriceLast);
        dest.writeString(this.productPriceUpdated);
        dest.writeString(this.productNotes);
        dest.writeString(this.productPriceIdr);
        dest.writeString(this.productPriceOriginal);
        dest.writeValue(this.productUseInsurance);
        dest.writeString(this.productTotalPriceIdr);
        dest.writeString(this.productMinOrder);
        dest.writeString(this.homeAttribution);
        dest.writeString(this.listNameProduct);
    }

    public CartProduct() {
    }

    protected CartProduct(Parcel in) {
        this.productTotalWeight = in.readString();
        this.productErrorMsg = in.readString();
        this.productPreorder = in.readParcelable(ProductPreorder.class.getClassLoader());
        this.productId = in.readString();
        this.productHideEdit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productPrice = in.readString();
        this.productName = in.readString();
        this.productQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productMustInsurance = in.readString();
        this.productCartId = in.readString();
        this.productPriceCurrencyValue = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productUrl = in.readString();
        this.productWeight = in.readString();
        this.productPic = in.readString();
        this.productStatus = in.readString();
        this.productPriceCurrency = in.readString();
        this.productReturnable = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productTotalPrice = in.readString();
        this.productPriceLast = in.readString();
        this.productPriceUpdated = in.readString();
        this.productNotes = in.readString();
        this.productPriceIdr = in.readString();
        this.productPriceOriginal = in.readString();
        this.productUseInsurance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productTotalPriceIdr = in.readString();
        this.productMinOrder = in.readString();
        this.homeAttribution = in.readString();
        this.listNameProduct = in.readString();
    }

    public static final Creator<CartProduct> CREATOR = new Creator<CartProduct>() {
        @Override
        public CartProduct createFromParcel(Parcel source) {
            return new CartProduct(source);
        }

        @Override
        public CartProduct[] newArray(int size) {
            return new CartProduct[size];
        }
    };
}
