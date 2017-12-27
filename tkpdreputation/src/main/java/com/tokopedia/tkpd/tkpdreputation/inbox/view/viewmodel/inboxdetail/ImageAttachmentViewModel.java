package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/24/17.
 */

public class ImageAttachmentViewModel implements Parcelable {

    private int attachmentId;
    private String description;
    private String uriThumbnail;
    private String uriLarge;

    public ImageAttachmentViewModel(int attachmentId, String description,
                                    String uriThumbnail, String uriLarge) {
        this.attachmentId = attachmentId;
        this.description = description;
        this.uriThumbnail = uriThumbnail;
        this.uriLarge = uriLarge;
    }

    protected ImageAttachmentViewModel(Parcel in) {
        attachmentId = in.readInt();
        description = in.readString();
        uriThumbnail = in.readString();
        uriLarge = in.readString();
    }

    public static final Creator<ImageAttachmentViewModel> CREATOR = new Creator<ImageAttachmentViewModel>() {
        @Override
        public ImageAttachmentViewModel createFromParcel(Parcel in) {
            return new ImageAttachmentViewModel(in);
        }

        @Override
        public ImageAttachmentViewModel[] newArray(int size) {
            return new ImageAttachmentViewModel[size];
        }
    };

    public int getAttachmentId() {
        return attachmentId;
    }

    public String getDescription() {
        return description;
    }

    public String getUriThumbnail() {
        return uriThumbnail;
    }

    public String getUriLarge() {
        return uriLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(attachmentId);
        dest.writeString(description);
        dest.writeString(uriThumbnail);
        dest.writeString(uriLarge);
    }
}
