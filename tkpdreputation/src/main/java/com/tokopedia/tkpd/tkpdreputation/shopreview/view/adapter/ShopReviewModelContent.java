package com.tokopedia.tkpd.tkpdreputation.shopreview.view.adapter;

import android.os.Parcel;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelContent;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewTypeFactoryAdapter;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewModelContent extends ProductReviewModelContent {
    private String productName;
    private String productImageUrl;

    @Override
    public int type(ProductReviewTypeFactoryAdapter typeFactory) {
        if(typeFactory instanceof  ShopReviewTypeFactoryAdapter){
            return ((ShopReviewTypeFactoryAdapter) typeFactory).type(this);
        }
        return 0;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.reviewIsAnonymous ? (byte) 1 : (byte) 0);
        dest.writeString(this.reviewerName);
        dest.writeString(this.reviewerId);
        dest.writeFloat(this.reviewStar);
        dest.writeString(this.reviewMessage);
        dest.writeString(this.reviewTime);
        dest.writeTypedList(this.reviewAttachment);
        dest.writeString(this.sellerName);
        dest.writeString(this.shopId);
        dest.writeString(this.reviewId);
        dest.writeByte(this.reviewHasReplied ? (byte) 1 : (byte) 0);
        dest.writeString(this.responseMessage);
        dest.writeString(this.responseCreateTime);
        dest.writeByte(this.sellerRepliedOwner ? (byte) 1 : (byte) 0);
        dest.writeByte(this.reviewCanReported ? (byte) 1 : (byte) 0);
        dest.writeString(this.reputationId);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productImageUrl);
    }

    protected ShopReviewModelContent(Parcel in) {
        this.reviewIsAnonymous = in.readByte() != 0;
        this.reviewerName = in.readString();
        this.reviewerId = in.readString();
        this.reviewStar = in.readFloat();
        this.reviewMessage = in.readString();
        this.reviewTime = in.readString();
        this.reviewAttachment = in.createTypedArrayList(ImageAttachmentViewModel.CREATOR);
        this.sellerName = in.readString();
        this.shopId = in.readString();
        this.reviewId = in.readString();
        this.reviewHasReplied = in.readByte() != 0;
        this.responseMessage = in.readString();
        this.responseCreateTime = in.readString();
        this.sellerRepliedOwner = in.readByte() != 0;
        this.reviewCanReported = in.readByte() != 0;
        this.reputationId = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.productImageUrl = in.readString();
    }

    public static final Creator<ShopReviewModelContent> CREATOR = new Creator<ShopReviewModelContent>() {
        @Override
        public ShopReviewModelContent createFromParcel(Parcel source) {
            return new ShopReviewModelContent(source);
        }

        @Override
        public ShopReviewModelContent[] newArray(int size) {
            return new ShopReviewModelContent[size];
        }
    };
}
