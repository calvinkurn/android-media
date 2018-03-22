package com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleProductViewModel {

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
}
