package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleProductViewModel implements Parcelable{

    String productName;
    String productImage;
    String discountLabel;
    String productPrice;
    String productPriceBeforeDiscount;
    int stockPercentage;
    String stockText;

    public SprintSaleProductViewModel(String productName, String productImage, String discountLabel,
                                      String productPrice, String productPriceBeforeDiscount,
                                      int stockPercentage, String stockText) {
        this.productName = productName;
        this.productImage = productImage;
        this.discountLabel = discountLabel;
        this.productPrice = productPrice;
        this.productPriceBeforeDiscount = productPriceBeforeDiscount;
        this.stockPercentage = stockPercentage;
        this.stockText = stockText;
    }

    protected SprintSaleProductViewModel(Parcel in) {
        productName = in.readString();
        productImage = in.readString();
        discountLabel = in.readString();
        productPrice = in.readString();
        productPriceBeforeDiscount = in.readString();
        stockPercentage = in.readInt();
        stockText = in.readString();
    }

    public static final Creator<SprintSaleProductViewModel> CREATOR = new Creator<SprintSaleProductViewModel>() {
        @Override
        public SprintSaleProductViewModel createFromParcel(Parcel in) {
            return new SprintSaleProductViewModel(in);
        }

        @Override
        public SprintSaleProductViewModel[] newArray(int size) {
            return new SprintSaleProductViewModel[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductPriceBeforeDiscount() {
        return productPriceBeforeDiscount;
    }

    public int getStockPercentage() {
        return stockPercentage;
    }

    public String getStockText() {
        return stockText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeString(discountLabel);
        dest.writeString(productPrice);
        dest.writeString(productPriceBeforeDiscount);
        dest.writeInt(stockPercentage);
        dest.writeString(stockText);
    }
}
