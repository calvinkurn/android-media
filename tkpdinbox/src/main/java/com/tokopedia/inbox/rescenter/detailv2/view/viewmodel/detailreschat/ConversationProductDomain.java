package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationProductDomain implements Parcelable {

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
    private List<ConversationAttachmentDomain> image;
    private String message;
    private int resProdId;
    private String name;

    public ConversationProductDomain(List<ConversationAttachmentDomain> image, String message, int resProdId, String name) {
        this.image = image;
        this.message = message;
        this.resProdId = resProdId;
        this.name = name;
    }

    protected ConversationProductDomain(Parcel in) {
        this.image = in.createTypedArrayList(ConversationAttachmentDomain.CREATOR);
        this.message = in.readString();
        this.resProdId = in.readInt();
        this.name = in.readString();
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

    public int getResProdId() {
        return resProdId;
    }

    public void setResProdId(int resProdId) {
        this.resProdId = resProdId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.image);
        dest.writeString(this.message);
        dest.writeInt(this.resProdId);
        dest.writeString(this.name);
    }
}
