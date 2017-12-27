package com.tokopedia.seller.product.edit.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * @author normansyahputa on 4/12/17.
 */

public class AddUrlVideoModel implements Parcelable, ItemType {
    public static final int TYPE = 19129244;
    public static final Parcelable.Creator<AddUrlVideoModel> CREATOR = new Parcelable.Creator<AddUrlVideoModel>() {
        @Override
        public AddUrlVideoModel createFromParcel(Parcel source) {
            return new AddUrlVideoModel(source);
        }

        @Override
        public AddUrlVideoModel[] newArray(int size) {
            return new AddUrlVideoModel[size];
        }
    };
    private String snippetTitle;
    private String snippetDescription;
    private String thumbnailUrl;
    private int width, height;
    private String videoId;

    public AddUrlVideoModel() {
    }

    protected AddUrlVideoModel(Parcel in) {
        this.snippetTitle = in.readString();
        this.snippetDescription = in.readString();
        this.thumbnailUrl = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.videoId = in.readString();
    }

    public String getSnippetTitle() {
        return snippetTitle;
    }

    public void setSnippetTitle(String snippetTitle) {
        this.snippetTitle = snippetTitle;
    }

    public String getSnippetDescription() {
        return snippetDescription;
    }

    public void setSnippetDescription(String snippetDescription) {
        this.snippetDescription = snippetDescription;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddUrlVideoModel that = (AddUrlVideoModel) o;

        if (snippetTitle != null ? !snippetTitle.equals(that.snippetTitle) : that.snippetTitle != null)
            return false;
        if (snippetDescription != null ? !snippetDescription.equals(that.snippetDescription) : that.snippetDescription != null)
            return false;
        return thumbnailUrl != null ? thumbnailUrl.equals(that.thumbnailUrl) : that.thumbnailUrl == null;

    }

    @Override
    public int hashCode() {
        int result = snippetTitle != null ? snippetTitle.hashCode() : 0;
        result = 31 * result + (snippetDescription != null ? snippetDescription.hashCode() : 0);
        result = 31 * result + (thumbnailUrl != null ? thumbnailUrl.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.snippetTitle);
        dest.writeString(this.snippetDescription);
        dest.writeString(this.thumbnailUrl);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.videoId);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
