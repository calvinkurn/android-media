package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductDomain implements Parcelable {

    private List<ConversationAttachmentDomain> image;
    private String message;
    private int resId;

    public ConversationProductDomain(List<ConversationAttachmentDomain> image, String message, int resId) {
        this.image = image;
        this.message = message;
        this.resId = resId;
    }

    public List<ConversationAttachmentDomain> getImage() {
        return image;
    }

    public void setImage(List<ConversationAttachmentDomain> image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.image);
        dest.writeString(this.message);
        dest.writeInt(this.resId);
    }

    protected ConversationProductDomain(Parcel in) {
        this.image = in.createTypedArrayList(ConversationAttachmentDomain.CREATOR);
        this.message = in.readString();
        this.resId = in.readInt();
    }

    public static final Creator<ConversationProductDomain> CREATOR = new Creator<ConversationProductDomain>() {
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
