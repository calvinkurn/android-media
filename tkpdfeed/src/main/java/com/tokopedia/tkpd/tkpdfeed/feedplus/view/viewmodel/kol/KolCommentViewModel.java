package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactory;

/**
 * @author by nisie on 10/31/17.
 */

public class KolCommentViewModel implements Visitable<KolTypeFactory>, Parcelable{
    protected String avatarUrl;
    protected String name;
    protected String review;
    protected String time;
    protected String url;
    protected boolean isOfficial;

    public KolCommentViewModel(String avatarUrl, String name, String review, String time) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.review = review;
        this.time = time;
    }

    protected KolCommentViewModel(Parcel in) {
        avatarUrl = in.readString();
        name = in.readString();
        review = in.readString();
        time = in.readString();
        url = in.readString();
        isOfficial = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatarUrl);
        dest.writeString(name);
        dest.writeString(review);
        dest.writeString(time);
        dest.writeString(url);
        dest.writeByte((byte) (isOfficial ? 1 : 0));
    }
}
