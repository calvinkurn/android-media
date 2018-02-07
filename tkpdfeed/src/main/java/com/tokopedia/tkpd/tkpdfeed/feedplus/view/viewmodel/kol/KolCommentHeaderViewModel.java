package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.kol.KolTypeFactory;

/**
 * @author by nisie on 11/2/17.
 */

public class KolCommentHeaderViewModel extends KolCommentViewModel implements
        Visitable<KolTypeFactory>, Parcelable {
    boolean canLoadMore;
    boolean isLoading;

    public KolCommentHeaderViewModel(String avatarUrl, String name, String review, String time,
                                     String userId) {
        super("0", userId, avatarUrl, name, review, time, true, false);
        this.canLoadMore = false;
        this.isLoading = false;

    }

    protected KolCommentHeaderViewModel(Parcel in) {
        super(in);
        canLoadMore = in.readByte() != 0;
        isLoading = in.readByte() != 0;
    }

    public static final Creator<KolCommentHeaderViewModel> CREATOR = new Creator<KolCommentHeaderViewModel>() {
        @Override
        public KolCommentHeaderViewModel createFromParcel(Parcel in) {
            return new KolCommentHeaderViewModel(in);
        }

        @Override
        public KolCommentHeaderViewModel[] newArray(int size) {
            return new KolCommentHeaderViewModel[size];
        }
    };

    @Override
    public int type(KolTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) (canLoadMore ? 1 : 0));
        dest.writeByte((byte) (isLoading ? 1 : 0));
    }
}
