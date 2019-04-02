package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProductImageModel implements Parcelable {

    private String full;
    private String thumb;

    public SolutionProductImageModel(String full, String thumb) {
        this.full = full;
        this.thumb = thumb;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.full);
        dest.writeString(this.thumb);
    }

    protected SolutionProductImageModel(Parcel in) {
        this.full = in.readString();
        this.thumb = in.readString();
    }

    public static final Parcelable.Creator<SolutionProductImageModel> CREATOR = new Parcelable.Creator<SolutionProductImageModel>() {
        @Override
        public SolutionProductImageModel createFromParcel(Parcel source) {
            return new SolutionProductImageModel(source);
        }

        @Override
        public SolutionProductImageModel[] newArray(int size) {
            return new SolutionProductImageModel[size];
        }
    };
}
