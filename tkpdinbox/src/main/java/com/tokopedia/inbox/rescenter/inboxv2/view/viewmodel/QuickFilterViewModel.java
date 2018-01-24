package com.tokopedia.inbox.rescenter.inboxv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 24/01/18.
 */

public class QuickFilterViewModel extends FilterViewModel implements Parcelable {

    public QuickFilterViewModel(String type, String typeName, int count, int orderValue, boolean isActive) {
        super(type, typeName, count, orderValue, isActive);
    }
}
