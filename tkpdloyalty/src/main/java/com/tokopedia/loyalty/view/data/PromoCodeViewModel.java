package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoCodeViewModel implements Parcelable {
    private String groupCodeTitle;
    private String groupCodeDescription;
    private List<SingleCodeViewModel> groupCode;

    public String getGroupCodeTitle() {
        return groupCodeTitle;
    }

    public void setGroupCodeTitle(String groupCodeTitle) {
        this.groupCodeTitle = groupCodeTitle;
    }

    public String getGroupCodeDescription() {
        return groupCodeDescription;
    }

    public void setGroupCodeDescription(String groupCodeDescription) {
        this.groupCodeDescription = groupCodeDescription;
    }

    public List<SingleCodeViewModel> getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(List<SingleCodeViewModel> groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupCodeTitle);
        dest.writeString(this.groupCodeDescription);
        dest.writeTypedList(this.groupCode);
    }

    public PromoCodeViewModel() {
    }

    protected PromoCodeViewModel(Parcel in) {
        this.groupCodeTitle = in.readString();
        this.groupCodeDescription = in.readString();
        this.groupCode = in.createTypedArrayList(SingleCodeViewModel.CREATOR);
    }

    public static final Creator<PromoCodeViewModel> CREATOR = new Creator<PromoCodeViewModel>() {
        @Override
        public PromoCodeViewModel createFromParcel(Parcel source) {
            return new PromoCodeViewModel(source);
        }

        @Override
        public PromoCodeViewModel[] newArray(int size) {
            return new PromoCodeViewModel[size];
        }
    };
}
