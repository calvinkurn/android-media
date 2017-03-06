package com.tokopedia.seller.opportunity.viewmodel;

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
}
