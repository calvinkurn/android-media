package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartItemData implements Parcelable {

    private OriginData originData;
    private UpdatedData updatedData;
    private MessageErrorData errorData;

    public OriginData getOriginData() {
        return originData;
    }

    public void setOriginData(OriginData originData) {
        this.originData = originData;
    }

    public UpdatedData getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(UpdatedData updatedData) {
        this.updatedData = updatedData;
    }

    public MessageErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(MessageErrorData errorData) {
        this.errorData = errorData;
    }

    public static class OriginData implements Parcelable {
        public static final int CURRENCY_IDR = 1;
        public static final int CURRENCY_USD = 2;

        public static final int WEIGHT_KILOS = 1;
        public static final int WEIGHT_GRAM = 2;

        private int cartId;
        private String productId;
        private String productName;
        private int minimalQtyOrder;
        private double pricePlan;
        private int priceCurrency;
        private String priceFormatted;
        private String productImage;
        private String productVarianRemark;
        private double weightPlan;
        private int weightUnit;
        private String weightFormatted;
        private String shopName;
        private String shopId;
        private boolean isPreOrder;
        private boolean isFreeReturn;
        private boolean isCashBack;
        private boolean isFavorite;
        private String cashBackInfo;

        public int getCartId() {
            return cartId;
        }

        public void setCartId(int cartId) {
            this.cartId = cartId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public boolean isPreOrder() {
            return isPreOrder;
        }

        public void setPreOrder(boolean preOrder) {
            isPreOrder = preOrder;
        }

        public boolean isFreeReturn() {
            return isFreeReturn;
        }

        public void setFreeReturn(boolean freeReturn) {
            isFreeReturn = freeReturn;
        }

        public boolean isCashBack() {
            return isCashBack;
        }

        public void setCashBack(boolean cashBack) {
            isCashBack = cashBack;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

        public String getCashBackInfo() {
            return cashBackInfo;
        }

        public void setCashBackInfo(String cashBackInfo) {
            this.cashBackInfo = cashBackInfo;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getMinimalQtyOrder() {
            return minimalQtyOrder;
        }

        public void setMinimalQtyOrder(int minimalQtyOrder) {
            this.minimalQtyOrder = minimalQtyOrder;
        }

        public double getPricePlan() {
            return pricePlan;
        }

        public void setPricePlan(double pricePlan) {
            this.pricePlan = pricePlan;
        }

        public int getPriceCurrency() {
            return priceCurrency;
        }

        public void setPriceCurrency(int priceCurrency) {
            this.priceCurrency = priceCurrency;
        }

        public String getPriceFormatted() {
            return priceFormatted;
        }

        public void setPriceFormatted(String priceFormatted) {
            this.priceFormatted = priceFormatted;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductVarianRemark() {
            return productVarianRemark;
        }

        public void setProductVarianRemark(String productVarianRemark) {
            this.productVarianRemark = productVarianRemark;
        }

        public double getWeightPlan() {
            return weightPlan;
        }

        public void setWeightPlan(double weightPlan) {
            this.weightPlan = weightPlan;
        }

        public int getWeightUnit() {
            return weightUnit;
        }

        public void setWeightUnit(int weightUnit) {
            this.weightUnit = weightUnit;
        }

        public String getWeightFormatted() {
            return weightFormatted;
        }

        public void setWeightFormatted(String weightFormatted) {
            this.weightFormatted = weightFormatted;
        }


        public OriginData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.cartId);
            dest.writeString(this.productId);
            dest.writeString(this.productName);
            dest.writeInt(this.minimalQtyOrder);
            dest.writeDouble(this.pricePlan);
            dest.writeInt(this.priceCurrency);
            dest.writeString(this.priceFormatted);
            dest.writeString(this.productImage);
            dest.writeString(this.productVarianRemark);
            dest.writeDouble(this.weightPlan);
            dest.writeInt(this.weightUnit);
            dest.writeString(this.weightFormatted);
            dest.writeString(this.shopName);
            dest.writeString(this.shopId);
            dest.writeByte(this.isPreOrder ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFreeReturn ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isCashBack ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
            dest.writeString(this.cashBackInfo);
        }

        protected OriginData(Parcel in) {
            this.cartId = in.readInt();
            this.productId = in.readString();
            this.productName = in.readString();
            this.minimalQtyOrder = in.readInt();
            this.pricePlan = in.readDouble();
            this.priceCurrency = in.readInt();
            this.priceFormatted = in.readString();
            this.productImage = in.readString();
            this.productVarianRemark = in.readString();
            this.weightPlan = in.readDouble();
            this.weightUnit = in.readInt();
            this.weightFormatted = in.readString();
            this.shopName = in.readString();
            this.shopId = in.readString();
            this.isPreOrder = in.readByte() != 0;
            this.isFreeReturn = in.readByte() != 0;
            this.isCashBack = in.readByte() != 0;
            this.isFavorite = in.readByte() != 0;
            this.cashBackInfo = in.readString();
        }

        public static final Creator<OriginData> CREATOR = new Creator<OriginData>() {
            @Override
            public OriginData createFromParcel(Parcel source) {
                return new OriginData(source);
            }

            @Override
            public OriginData[] newArray(int size) {
                return new OriginData[size];
            }
        };
    }

    public static class UpdatedData implements Parcelable {
        private int quantity;
        private String remark;
        private int maxQuantity;
        private int maxCharRemark;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public UpdatedData() {
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public void setMaxQuantity(int maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        public int getMaxCharRemark() {
            return maxCharRemark;
        }

        public void setMaxCharRemark(int maxCharRemark) {
            this.maxCharRemark = maxCharRemark;
        }

        public void decreaseQuantity() {
            this.quantity--;
        }

        public void increaseQuantity() {
            this.quantity++;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.quantity);
            dest.writeString(this.remark);
            dest.writeInt(this.maxQuantity);
            dest.writeInt(this.maxCharRemark);
        }

        protected UpdatedData(Parcel in) {
            this.quantity = in.readInt();
            this.remark = in.readString();
            this.maxQuantity = in.readInt();
            this.maxCharRemark = in.readInt();
        }

        public static final Creator<UpdatedData> CREATOR = new Creator<UpdatedData>() {
            @Override
            public UpdatedData createFromParcel(Parcel source) {
                return new UpdatedData(source);
            }

            @Override
            public UpdatedData[] newArray(int size) {
                return new UpdatedData[size];
            }
        };
    }


    public static class MessageErrorData implements Parcelable{
        private String errorCheckoutPriceLimit;
        private String errorFieldBetween;
        private String errorFieldMaxChar;
        private String errorFieldRequired;
        private String errorProductAvailableStock;
        private String errorProductAvailableStockDetail;
        private String errorProductMaxQuantity;
        private String errorProductMinQuantity;
        private List<String> errorAdditional = new ArrayList<>();

        public List<String> getErrorAdditional() {
            return errorAdditional;
        }

        public void setErrorAdditional(List<String> errorAdditional) {
            this.errorAdditional = errorAdditional;
        }

        public String getErrorCheckoutPriceLimit() {
            return errorCheckoutPriceLimit;
        }

        public void setErrorCheckoutPriceLimit(String errorCheckoutPriceLimit) {
            this.errorCheckoutPriceLimit = errorCheckoutPriceLimit;
        }

        public String getErrorFieldBetween() {
            return errorFieldBetween;
        }

        public void setErrorFieldBetween(String errorFieldBetween) {
            this.errorFieldBetween = errorFieldBetween;
        }

        public String getErrorFieldMaxChar() {
            return errorFieldMaxChar;
        }

        public void setErrorFieldMaxChar(String errorFieldMaxChar) {
            this.errorFieldMaxChar = errorFieldMaxChar;
        }

        public String getErrorFieldRequired() {
            return errorFieldRequired;
        }

        public void setErrorFieldRequired(String errorFieldRequired) {
            this.errorFieldRequired = errorFieldRequired;
        }

        public String getErrorProductAvailableStock() {
            return errorProductAvailableStock;
        }

        public void setErrorProductAvailableStock(String errorProductAvailableStock) {
            this.errorProductAvailableStock = errorProductAvailableStock;
        }

        public String getErrorProductAvailableStockDetail() {
            return errorProductAvailableStockDetail;
        }

        public void setErrorProductAvailableStockDetail(String errorProductAvailableStockDetail) {
            this.errorProductAvailableStockDetail = errorProductAvailableStockDetail;
        }

        public String getErrorProductMaxQuantity() {
            return errorProductMaxQuantity;
        }

        public void setErrorProductMaxQuantity(String errorProductMaxQuantity) {
            this.errorProductMaxQuantity = errorProductMaxQuantity;
        }

        public String getErrorProductMinQuantity() {
            return errorProductMinQuantity;
        }

        public void setErrorProductMinQuantity(String errorProductMinQuantity) {
            this.errorProductMinQuantity = errorProductMinQuantity;
        }

        public MessageErrorData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.errorCheckoutPriceLimit);
            dest.writeString(this.errorFieldBetween);
            dest.writeString(this.errorFieldMaxChar);
            dest.writeString(this.errorFieldRequired);
            dest.writeString(this.errorProductAvailableStock);
            dest.writeString(this.errorProductAvailableStockDetail);
            dest.writeString(this.errorProductMaxQuantity);
            dest.writeString(this.errorProductMinQuantity);
            dest.writeStringList(this.errorAdditional);
        }

        protected MessageErrorData(Parcel in) {
            this.errorCheckoutPriceLimit = in.readString();
            this.errorFieldBetween = in.readString();
            this.errorFieldMaxChar = in.readString();
            this.errorFieldRequired = in.readString();
            this.errorProductAvailableStock = in.readString();
            this.errorProductAvailableStockDetail = in.readString();
            this.errorProductMaxQuantity = in.readString();
            this.errorProductMinQuantity = in.readString();
            this.errorAdditional = in.createStringArrayList();
        }

        public static final Creator<MessageErrorData> CREATOR = new Creator<MessageErrorData>() {
            @Override
            public MessageErrorData createFromParcel(Parcel source) {
                return new MessageErrorData(source);
            }

            @Override
            public MessageErrorData[] newArray(int size) {
                return new MessageErrorData[size];
            }
        };
    }

    public CartItemData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.originData, flags);
        dest.writeParcelable(this.updatedData, flags);
        dest.writeParcelable(this.errorData, flags);
    }

    protected CartItemData(Parcel in) {
        this.originData = in.readParcelable(OriginData.class.getClassLoader());
        this.updatedData = in.readParcelable(UpdatedData.class.getClassLoader());
        this.errorData = in.readParcelable(MessageErrorData.class.getClassLoader());
    }

    public static final Creator<CartItemData> CREATOR = new Creator<CartItemData>() {
        @Override
        public CartItemData createFromParcel(Parcel source) {
            return new CartItemData(source);
        }

        @Override
        public CartItemData[] newArray(int size) {
            return new CartItemData[size];
        }
    };
}
