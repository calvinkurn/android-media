package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfsx on 29/12/17.
 */

public class KolFollowingResultViewModel implements Parcelable {
    private boolean isCanLoadMore;
    private String lastCursor;
    private List<KolFollowingViewModel> kolFollowingViewModelList;

    public KolFollowingResultViewModel(boolean isCanLoadMore,
                                       String lastCursor,
                                       List<KolFollowingViewModel> kolFollowingViewModelList) {
        this.isCanLoadMore = isCanLoadMore;
        this.lastCursor = lastCursor;
        this.kolFollowingViewModelList = kolFollowingViewModelList;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public String getLastCursor() {
        return lastCursor;
    }

    public void setLastCursor(String lastCursor) {
        this.lastCursor = lastCursor;
    }

    public List<KolFollowingViewModel> getKolFollowingViewModelList() {
        return kolFollowingViewModelList;
    }

    public void setKolFollowingViewModelList(List<KolFollowingViewModel> kolFollowingViewModelList) {
        this.kolFollowingViewModelList = kolFollowingViewModelList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastCursor);
        dest.writeTypedList(this.kolFollowingViewModelList);
    }

    protected KolFollowingResultViewModel(Parcel in) {
        this.lastCursor = in.readString();
        this.kolFollowingViewModelList = in.createTypedArrayList(KolFollowingViewModel.CREATOR);
    }

    public static final Creator<KolFollowingResultViewModel> CREATOR = new Creator<KolFollowingResultViewModel>() {
        @Override
        public KolFollowingResultViewModel createFromParcel(Parcel source) {
            return new KolFollowingResultViewModel(source);
        }

        @Override
        public KolFollowingResultViewModel[] newArray(int size) {
            return new KolFollowingResultViewModel[size];
        }
    };
}
