
package com.tokopedia.seller.product.edit.view.model.edit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.ProductVariantViewModel;

//TODO need this: product_sizechart from_ig image_id
public class ProductViewModel implements ItemType, Parcelable {

    public static final int TYPE = 382;
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("product_name")
    @Expose
    private String productName = "";
    @SerializedName("product_alias")
    @Expose
    private String productAlias;

    @SerializedName("product_condition")
    @Expose
    private long productCondition; // Product Condition (new=1, used=2)

    @SerializedName("product_description")
    @Expose
    private String productDescription = "";

    @SerializedName("product_last_update_price")
    @Expose
    private String productLastUpdatePrice;

    @SerializedName("product_min_order")
    @Expose
    private long productMinOrder; // Minimum Order (1-10000)

    @SerializedName("product_max_order")
    @Expose
    private long productMaxOrder;

    @SerializedName("product_must_insurance")
    @Expose
    private boolean productMustInsurance;

    @SerializedName("product_price")
    @Expose
    private double productPrice;

    @SerializedName("product_price_currency")
    @Expose
    private long productPriceCurrency; // Price Currency ID (IDR=1;USD=2)

    @SerializedName("product_status")
    @Expose
    private int productStatus; // Product Status (Active=1, Warehouse=3, Pending=-1)

    @SerializedName("product_stock")
    @Expose
    private long productStock;

    @SerializedName("product_weight")
    @Expose
    private long productWeight; // Weight (Gram=1-500000, Kg=1-500)
    @SerializedName("product_weight_unit")
    @Expose
    private long productWeightUnit; // Weight Unit (Gram=1, Kg=2)

    @SerializedName("product_url")
    @Expose
    private String productUrl;

    @SerializedName("product_free_return")
    @Expose
    private boolean productFreeReturn;

    @SerializedName("product_sku")
    @Expose
    private String productSku;

    @SerializedName("product_gtin")
    @Expose
    private String productGtin;

    @SerializedName("product_brand")
    @Expose
    private ProductBrandViewModel productBrand;

    @SerializedName("product_catalog")
    @Expose
    private ProductCatalogViewModel productCatalog;

    @SerializedName("product_category")
    @Expose
    private ProductCategoryViewModel productCategory;

    @SerializedName("product_etalase")
    @Expose
    private ProductEtalaseViewModel productEtalase;

    @SerializedName("product_picture")
    @Expose
    private List<ProductPictureViewModel> productPictureViewModelList = new ArrayList<>();

    @SerializedName("product_preorder")
    @Expose
    private ProductPreorderViewModel productPreorder;

    @SerializedName("product_position")
    @Expose
    private ProductPositionViewModel productPosition;

    @SerializedName("product_shop")
    @Expose
    private ProductShopViewModel productShop;

    @SerializedName("product_sizechart")
    @Expose
    private ProductPictureViewModel productSizeChart;

    @SerializedName("product_wholesale")
    @Expose
    private List<ProductWholesaleViewModel> productWholesale = new ArrayList<>();

    /**
     * get from GM. set for saubmit add/edit product
     */
    @SerializedName("product_video")
    @Expose
    private List<ProductVideoViewModel> productVideo = new ArrayList<>();

    @SerializedName("product_variant")
    @Expose
    private ProductVariantViewModel productVariant;

    @SerializedName("product_name_editable")
    @Expose
    private boolean productNameEditable;

    @Expose
    private long draftId;

    public long getDraftId() {
        return draftId;
    }

    public void setDraftId(long draftId) {
        this.draftId = draftId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public long getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(long productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public long getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(long productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public long getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(long productWeight) {
        this.productWeight = productWeight;
    }

    public long getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(long productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public long getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(long productCondition) {
        this.productCondition = productCondition;
    }

    public ProductEtalaseViewModel getProductEtalase() {
        return productEtalase;
    }

    public void setProductEtalase(ProductEtalaseViewModel productEtalase) {
        this.productEtalase = productEtalase;
    }

    public int getImageCount(){
        return productPictureViewModelList == null? 0: productPictureViewModelList.size();
    }

    public int getMinimumImageResolution(){
        if (getImageCount() == 0) {
            return 0;
        }
        int minResolution = Integer.MAX_VALUE;
        for (int i = 0, sizei = productPictureViewModelList.size() ; i<sizei ; i++) {
            ProductPictureViewModel productPictureViewModel = productPictureViewModelList.get(i);
            int resolution = (int) Math.min(productPictureViewModel.getX(), productPictureViewModel.getY());
            if (minResolution > resolution) {
                minResolution = resolution;
            }
        }
        return minResolution == Integer.MAX_VALUE? 0: minResolution;
    }

    public List<ProductPictureViewModel> getProductPictureViewModelList() {
        return productPictureViewModelList;
    }

    public void setProductPictureViewModelList(List<ProductPictureViewModel> productPictureViewModelList) {
        this.productPictureViewModelList = productPictureViewModelList;
    }

    public ProductShopViewModel getProductShop() {
        return productShop;
    }

    public void setProductShop(ProductShopViewModel productShop) {
        this.productShop = productShop;
    }

    public String getProductAlias() {
        return productAlias;
    }

    public void setProductAlias(String productAlias) {
        this.productAlias = productAlias;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductLastUpdatePrice() {
        return productLastUpdatePrice;
    }

    public void setProductLastUpdatePrice(String productLastUpdatePrice) {
        this.productLastUpdatePrice = productLastUpdatePrice;
    }

    public ProductPositionViewModel getProductPosition() {
        return productPosition;
    }

    public void setProductPosition(ProductPositionViewModel productPosition) {
        this.productPosition = productPosition;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public boolean isProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(boolean productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public boolean isProductFreeReturn() {
        return productFreeReturn;
    }

    public void setProductFreeReturn(boolean productFreeReturn) {
        this.productFreeReturn = productFreeReturn;
    }

    public long getProductStock() {
        return productStock;
    }

    public void setProductStock(long productStock) {
        this.productStock = productStock;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public long getProductMaxOrder() {
        return productMaxOrder;
    }

    public void setProductMaxOrder(long productMaxOrder) {
        this.productMaxOrder = productMaxOrder;
    }

    public ProductCategoryViewModel getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryViewModel productCategory) {
        this.productCategory = productCategory;
    }

    public ProductCatalogViewModel getProductCatalog() {
        return productCatalog;
    }

    public void setProductCatalog(ProductCatalogViewModel productCatalog) {
        this.productCatalog = productCatalog;
    }

    public List<ProductWholesaleViewModel> getProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(List<ProductWholesaleViewModel> productWholesale) {
        this.productWholesale = productWholesale;
    }

    public ProductPreorderViewModel getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(ProductPreorderViewModel productPreorder) {
        this.productPreorder = productPreorder;
    }

    public ProductBrandViewModel getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(ProductBrandViewModel productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductGtin() {
        return productGtin;
    }

    public void setProductGtin(String productGtin) {
        this.productGtin = productGtin;
    }

    public List<ProductVideoViewModel> getProductVideo() {
        return productVideo;
    }

    public void setProductVideo(List<ProductVideoViewModel> productVideo) {
        this.productVideo = productVideo;
    }

    public boolean hasVariant(){
        return productVariant!= null && productVariant.getProductVariant()!= null &&
                productVariant.getProductVariant().size() > 0;
    }

    public ProductVariantViewModel getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariantViewModel productVariant) {
        this.productVariant = productVariant;
    }

    public void setProductNameEditable(boolean productNameEditable) {
        this.productNameEditable = productNameEditable;
    }

    public boolean isProductNameEditable() {
        return productNameEditable;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productAlias);
        dest.writeLong(this.productCondition);
        dest.writeString(this.productDescription);
        dest.writeString(this.productLastUpdatePrice);
        dest.writeLong(this.productMinOrder);
        dest.writeLong(this.productMaxOrder);
        dest.writeByte(this.productMustInsurance ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.productPrice);
        dest.writeLong(this.productPriceCurrency);
        dest.writeInt(this.productStatus);
        dest.writeLong(this.productStock);
        dest.writeLong(this.productWeight);
        dest.writeLong(this.productWeightUnit);
        dest.writeString(this.productUrl);
        dest.writeByte(this.productFreeReturn ? (byte) 1 : (byte) 0);
        dest.writeString(this.productSku);
        dest.writeString(this.productGtin);
        dest.writeParcelable(this.productBrand, flags);
        dest.writeParcelable(this.productCatalog, flags);
        dest.writeParcelable(this.productCategory, flags);
        dest.writeParcelable(this.productEtalase, flags);
        dest.writeList(this.productPictureViewModelList);
        dest.writeParcelable(this.productPreorder, flags);
        dest.writeParcelable(this.productPosition, flags);
        dest.writeParcelable(this.productShop, flags);
        dest.writeParcelable(this.productSizeChart, flags);
        dest.writeTypedList(this.productWholesale);
        dest.writeList(this.productVideo);
        dest.writeParcelable(this.productVariant, flags);
        dest.writeByte(this.productNameEditable ? (byte) 1 : (byte) 0);
        dest.writeLong(this.draftId);
    }

    public ProductViewModel() {
    }

    protected ProductViewModel(Parcel in) {
        this.productId = in.readLong();
        this.productName = in.readString();
        this.productAlias = in.readString();
        this.productCondition = in.readLong();
        this.productDescription = in.readString();
        this.productLastUpdatePrice = in.readString();
        this.productMinOrder = in.readLong();
        this.productMaxOrder = in.readLong();
        this.productMustInsurance = in.readByte() != 0;
        this.productPrice = in.readDouble();
        this.productPriceCurrency = in.readLong();
        this.productStatus = in.readInt();
        this.productStock = in.readLong();
        this.productWeight = in.readLong();
        this.productWeightUnit = in.readLong();
        this.productUrl = in.readString();
        this.productFreeReturn = in.readByte() != 0;
        this.productSku = in.readString();
        this.productGtin = in.readString();
        this.productBrand = in.readParcelable(ProductBrandViewModel.class.getClassLoader());
        this.productCatalog = in.readParcelable(ProductCatalogViewModel.class.getClassLoader());
        this.productCategory = in.readParcelable(ProductCategoryViewModel.class.getClassLoader());
        this.productEtalase = in.readParcelable(ProductEtalaseViewModel.class.getClassLoader());
        this.productPictureViewModelList = new ArrayList<ProductPictureViewModel>();
        in.readList(this.productPictureViewModelList, ProductPictureViewModel.class.getClassLoader());
        this.productPreorder = in.readParcelable(ProductPreorderViewModel.class.getClassLoader());
        this.productPosition = in.readParcelable(ProductPositionViewModel.class.getClassLoader());
        this.productShop = in.readParcelable(ProductShopViewModel.class.getClassLoader());
        this.productSizeChart = in.readParcelable(ProductPictureViewModel.class.getClassLoader());
        this.productWholesale = in.createTypedArrayList(ProductWholesaleViewModel.CREATOR);
        this.productVideo = new ArrayList<ProductVideoViewModel>();
        in.readList(this.productVideo, ProductVideoViewModel.class.getClassLoader());
        this.productVariant = in.readParcelable(ProductVariantViewModel.class.getClassLoader());
        this.productNameEditable = in.readByte() != 0;
        this.draftId = in.readLong();
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel source) {
            return new ProductViewModel(source);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };
}
