package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactory;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewModel implements Visitable<KolTypeFactory>, Parcelable{
    private int id;
    private String avatarUrl;
    private String name;
    private String review;
    private String time;
    private String url;
    private boolean isOfficial;
    private String userId;
    private boolean canDeleteComment;

    public KolCommentViewModel(int id, String avatarUrl, String name, String review, String time,
                               boolean isOfficial, boolean canDeleteComment) {
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.review = review;
        this.time = time;
        this.isOfficial = isOfficial;
        this.canDeleteComment = canDeleteComment;
    }


    protected KolCommentViewModel(Parcel in) {
        id = in.readInt();
        avatarUrl = in.readString();
        name = in.readString();
        review = in.readString();
        time = in.readString();
        url = in.readString();
        isOfficial = in.readByte() != 0;
        userId = in.readString();
        canDeleteComment = in.readByte() != 0;
    }

    public static final Creator<KolCommentViewModel> CREATOR = new Creator<KolCommentViewModel>() {
        @Override
        public KolCommentViewModel createFromParcel(Parcel in) {
            return new KolCommentViewModel(in);
        }

        @Override
        public KolCommentViewModel[] newArray(int size) {
            return new KolCommentViewModel[size];
        }
    };

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int type(KolTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        this.isOfficial = official;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean canDeleteComment() {
        return canDeleteComment;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(avatarUrl);
        dest.writeString(name);
        dest.writeString(review);
        dest.writeString(time);
        dest.writeString(url);
        dest.writeByte((byte) (isOfficial ? 1 : 0));
        dest.writeString(userId);
        dest.writeByte((byte) (canDeleteComment ? 1 : 0));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
