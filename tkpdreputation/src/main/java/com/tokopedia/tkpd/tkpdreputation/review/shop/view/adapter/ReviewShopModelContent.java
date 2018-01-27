package com.tokopedia.tkpd.tkpdreputation.review.shop.view.adapter;

import android.os.Parcel;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductModelContent;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.adapter.ReviewProductTypeFactoryAdapter;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopModelContent extends ReviewProductModelContent {
    private String productName;
    private String productImageUrl;
    private String productPageUrl;

    public ReviewShopModelContent() {
    }

    @Override
    public int type(ReviewProductTypeFactoryAdapter typeFactory) {
        if (typeFactory instanceof ReviewShopTypeFactoryAdapter) {
            return ((ReviewShopTypeFactoryAdapter) typeFactory).type(this);
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
        dest.writeByte(this.likeStatus ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalLike);
        dest.writeByte(this.isLogin ? (byte) 1 : (byte) 0);
        dest.writeString(this.productName);
        dest.writeString(this.productImageUrl);
        dest.writeString(this.productPageUrl);
    }

    protected ReviewShopModelContent(Parcel in) {
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
        this.likeStatus = in.readByte() != 0;
        this.totalLike = in.readInt();
        this.isLogin = in.readByte() != 0;
        this.productName = in.readString();
        this.productImageUrl = in.readString();
        this.productPageUrl = in.readString();
    }

    public static final Creator<ReviewShopModelContent> CREATOR = new Creator<ReviewShopModelContent>() {
        @Override
        public ReviewShopModelContent createFromParcel(Parcel source) {
            return new ReviewShopModelContent(source);
        }

        @Override
        public ReviewShopModelContent[] newArray(int size) {
            return new ReviewShopModelContent[size];
        }
    };

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public void setProductPageUrl(String productPageUrl) {
        this.productPageUrl = productPageUrl;
    }
}
