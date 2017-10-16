package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductDomain implements Parcelable {

    private ConversationAttachmentDomain image;
    private String message;
    private int resId;

    public ConversationProductDomain(ConversationAttachmentDomain image, String message, int resId) {
        this.image = image;
        this.message = message;
        this.resId = resId;
    }

    public ConversationAttachmentDomain getImage() {
        return image;
    }

    public void setImage(ConversationAttachmentDomain image) {
        this.image = image;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.image, flags);
        dest.writeString(this.message);
        dest.writeInt(this.resId);
    }

    protected ConversationProductDomain(Parcel in) {
        this.image = in.readParcelable(ConversationAttachmentDomain.class.getClassLoader());
        this.message = in.readString();
        this.resId = in.readInt();
    }

    public static final Parcelable.Creator<ConversationProductDomain> CREATOR = new Parcelable.Creator<ConversationProductDomain>() {
        @Override
        public ConversationProductDomain createFromParcel(Parcel source) {
            return new ConversationProductDomain(source);
        }

        @Override
        public ConversationProductDomain[] newArray(int size) {
            return new ConversationProductDomain[size];
        }
    };
}
