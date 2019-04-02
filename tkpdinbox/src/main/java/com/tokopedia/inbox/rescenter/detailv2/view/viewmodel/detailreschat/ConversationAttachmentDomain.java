package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationAttachmentDomain implements Parcelable {
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";

    public static final Parcelable.Creator<ConversationAttachmentDomain> CREATOR = new Parcelable.Creator<ConversationAttachmentDomain>() {
        @Override
        public ConversationAttachmentDomain createFromParcel(Parcel source) {
            return new ConversationAttachmentDomain(source);
        }

        @Override
        public ConversationAttachmentDomain[] newArray(int size) {
            return new ConversationAttachmentDomain[size];
        }
    };
    private String type;
    private String thumb;
    private String full;

    public ConversationAttachmentDomain(String type, String thumb, String full) {
        this.type = type;
        this.thumb = thumb;
        this.full = full;
    }

    protected ConversationAttachmentDomain(Parcel in) {
        this.type = in.readString();
        this.thumb = in.readString();
        this.full = in.readString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.thumb);
        dest.writeString(this.full);
    }
}
