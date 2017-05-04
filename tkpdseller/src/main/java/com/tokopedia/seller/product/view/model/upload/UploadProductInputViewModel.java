package com.tokopedia.seller.product.view.model.upload;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.product.constant.SwitchTypeDef;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

import com.tokopedia.seller.product.constant.CurrencyTypeDef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class UploadProductInputViewModel implements Parcelable {
    private ProductPhotoListViewModel productPhotos;
    private List<ProductWholesaleViewModel> productWholesaleList;
    private List<String> productVideos;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int productChangePhoto;
    private long productCatalogId;
    private String productCatalogName;
    private long productDepartmentId;
    private int productCondition;
    private long productEtalaseId;
    private String productEtalaseName;
    private int productMinOrder;
    private int productMustInsurance;
    @CurrencyTypeDef
    private int productPriceCurrency;
    private int productReturnable;
    private int productUploadTo;
    @SwitchTypeDef
    private int productInvenageSwitch;
    private int productInvenageValue;
    private int productWeight;
    private int productWeightUnit;
    private int poProcessType;
    private int poProcessValue;
    private int serverId;
    @ProductStatus
    private int productStatus;
    private String productId;
    private int productNameEditable;

    public UploadProductInputViewModel() {
        productPhotos = new ProductPhotoListViewModel();
        productWholesaleList = new ArrayList<>();
        productVideos = new ArrayList<>();
        productName = "";
        productDescription = "";
    }

    public ProductPhotoListViewModel getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhotoListViewModel productPhotos) {
        this.productPhotos = productPhotos;
    }

    public List<ProductWholesaleViewModel> getProductWholesaleList() {
        return productWholesaleList;
    }

    public void setProductWholesaleList(List<ProductWholesaleViewModel> productWholesaleList) {
        this.productWholesaleList = productWholesaleList;
    }

    public List<String> getProductVideos() {
        return productVideos;
    }

    public void setProductVideos(List<String> productVideos) {
        this.productVideos = productVideos;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductChangePhoto() {
        return productChangePhoto;
    }

    public void setProductChangePhoto(int productChangePhoto) {
        this.productChangePhoto = productChangePhoto;
    }

    public long getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(long productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
    }

    public long getProductDepartmentId() {
        return productDepartmentId;
    }

    public void setProductDepartmentId(long productDepartmentId) {
        this.productDepartmentId = productDepartmentId;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public long getProductEtalaseId() {
        return productEtalaseId;
    }

    public void setProductEtalaseId(long productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public String getProductEtalaseName() {
        return productEtalaseName;
    }

    public void setProductEtalaseName(String productEtalaseName) {
        this.productEtalaseName = productEtalaseName;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(int productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public int getProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(int productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @CurrencyTypeDef
    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(int productReturnable) {
        this.productReturnable = productReturnable;
    }

    public int getProductUploadTo() {
        return productUploadTo;
    }

    public void setProductUploadTo(int productUploadTo) {
        this.productUploadTo = productUploadTo;
    }

    @SwitchTypeDef
    public int getProductInvenageSwitch() {
        return productInvenageSwitch;
    }

    public void setProductInvenageSwitch(int productInvenageSwitch) {
        this.productInvenageSwitch = productInvenageSwitch;
    }

    public int getProductInvenageValue() {
        return productInvenageValue;
    }

    public void setProductInvenageValue(int productInvenageValue) {
        this.productInvenageValue = productInvenageValue;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public int getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(int productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public int getPoProcessType() {
        return poProcessType;
    }

    public void setPoProcessType(int poProcessType) {
        this.poProcessType = poProcessType;
    }

    public int getPoProcessValue() {
        return poProcessValue;
    }

    public void setPoProcessValue(int poProcessValue) {
        this.poProcessValue = poProcessValue;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    @ProductStatus
    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(@ProductStatus int productStatus) {
        this.productStatus = productStatus;
    }

    public void setProductNameEditable(int productNameEditable) {
        this.productNameEditable = productNameEditable;
    }

    public int getProductNameEditable() {
        return productNameEditable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productPhotos, flags);
        dest.writeList(this.productWholesaleList);
        dest.writeStringList(this.productVideos);
        dest.writeString(this.productName);
        dest.writeString(this.productDescription);
        dest.writeDouble(this.productPrice);
        dest.writeInt(this.productChangePhoto);
        dest.writeLong(this.productCatalogId);
        dest.writeString(this.productCatalogName);
        dest.writeLong(this.productDepartmentId);
        dest.writeInt(this.productCondition);
        dest.writeLong(this.productEtalaseId);
        dest.writeString(this.productEtalaseName);
        dest.writeInt(this.productMinOrder);
        dest.writeInt(this.productMustInsurance);
        dest.writeInt(this.productPriceCurrency);
        dest.writeInt(this.productReturnable);
        dest.writeInt(this.productUploadTo);
        dest.writeInt(this.productInvenageSwitch);
        dest.writeInt(this.productInvenageValue);
        dest.writeInt(this.productWeight);
        dest.writeInt(this.productWeightUnit);
        dest.writeInt(this.poProcessType);
        dest.writeInt(this.poProcessValue);
        dest.writeInt(this.serverId);
        dest.writeInt(this.productStatus);
        dest.writeString(this.productId);
        dest.writeInt(this.productNameEditable);
    }

    protected UploadProductInputViewModel(Parcel in) {
        this.productPhotos = in.readParcelable(ProductPhotoListViewModel.class.getClassLoader());
        this.productWholesaleList = new ArrayList<ProductWholesaleViewModel>();
        in.readList(this.productWholesaleList, ProductWholesaleViewModel.class.getClassLoader());
        this.productVideos = in.createStringArrayList();
        this.productName = in.readString();
        this.productDescription = in.readString();
        this.productPrice = in.readDouble();
        this.productChangePhoto = in.readInt();
        this.productCatalogId = in.readLong();
        this.productCatalogName = in.readString();
        this.productDepartmentId = in.readLong();
        this.productCondition = in.readInt();
        this.productEtalaseId = in.readLong();
        this.productEtalaseName = in.readString();
        this.productMinOrder = in.readInt();
        this.productMustInsurance = in.readInt();
        this.productPriceCurrency = in.readInt();
        this.productReturnable = in.readInt();
        this.productUploadTo = in.readInt();
        this.productInvenageSwitch = in.readInt();
        this.productInvenageValue = in.readInt();
        this.productWeight = in.readInt();
        this.productWeightUnit = in.readInt();
        this.poProcessType = in.readInt();
        this.poProcessValue = in.readInt();
        this.serverId = in.readInt();
        this.productStatus = in.readInt();
        this.productId = in.readString();
        this.productNameEditable = in.readInt();
    }

    public static final Parcelable.Creator<UploadProductInputViewModel> CREATOR = new Parcelable.Creator<UploadProductInputViewModel>() {
        @Override
        public UploadProductInputViewModel createFromParcel(Parcel source) {
            return new UploadProductInputViewModel(source);
        }

        @Override
        public UploadProductInputViewModel[] newArray(int size) {
            return new UploadProductInputViewModel[size];
        }
    };
}
