package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleProductViewModel implements Parcelable {

    private String productId;
    String productName;
    String productImage;
    String discountLabel;
    String productPrice;
    String productPriceBeforeDiscount;
    int stockPercentage;
    String stockText;
    private String productUrl;

    public SprintSaleProductViewModel(String productId, String productName, String productImage,
                                      String discountLabel,
                                      String productPrice, String productPriceBeforeDiscount,
                                      int stockPercentage, String stockText, String productUrl) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.discountLabel = discountLabel;
        this.productPrice = productPrice;
        this.productPriceBeforeDiscount = productPriceBeforeDiscount;
        this.stockPercentage = stockPercentage;
        this.stockText = stockText;
        this.productUrl = productUrl;
    }


    protected SprintSaleProductViewModel(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productImage = in.readString();
        discountLabel = in.readString();
        productPrice = in.readString();
        productPriceBeforeDiscount = in.readString();
        stockPercentage = in.readInt();
        stockText = in.readString();
        productUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeString(discountLabel);
        dest.writeString(productPrice);
        dest.writeString(productPriceBeforeDiscount);
        dest.writeInt(stockPercentage);
        dest.writeString(stockText);
        dest.writeString(productUrl);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getProductId() {
        return productId;
    }

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


    public String getProductUrl() {
        return productUrl;
    }



}
