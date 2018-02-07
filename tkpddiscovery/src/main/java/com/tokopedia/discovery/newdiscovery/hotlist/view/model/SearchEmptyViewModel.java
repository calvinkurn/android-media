package com.tokopedia.discovery.newdiscovery.hotlist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistAdapterTypeFactory;

/**
 * Created by hangnadi on 10/25/17.
 */

public class SearchEmptyViewModel implements Visitable<HotlistAdapterTypeFactory>, Parcelable {

    public SearchEmptyViewModel() {
    }

    @Override
    public int type(HotlistAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    protected SearchEmptyViewModel(Parcel in) {
    }

    public static final Creator<SearchEmptyViewModel> CREATOR = new Creator<SearchEmptyViewModel>() {
        @Override
        public SearchEmptyViewModel createFromParcel(Parcel in) {
            return new SearchEmptyViewModel(in);
        }

        @Override
        public SearchEmptyViewModel[] newArray(int size) {
            return new SearchEmptyViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
