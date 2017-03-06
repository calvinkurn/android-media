package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/6/17.
 */

public class SortingTypeViewModel {
    int sortingTypeId;
    String sortingTypeName;

    public SortingTypeViewModel(String sortingTypeName, int sortingTypeId) {
        this.sortingTypeId = sortingTypeId;
        this.sortingTypeName= sortingTypeName;
    }

    public int getSortingTypeId() {
        return sortingTypeId;
    }

    public void setSortingTypeId(int sortingTypeId) {
        this.sortingTypeId = sortingTypeId;
    }

    public String getSortingTypeName() {
        return sortingTypeName;
    }

    public void setSortingTypeName(String sortingTypeName) {
        this.sortingTypeName = sortingTypeName;
    }
}
