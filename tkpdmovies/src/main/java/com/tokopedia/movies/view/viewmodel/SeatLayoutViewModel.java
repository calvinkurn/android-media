package com.tokopedia.movies.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.movies.data.entity.response.Area;
import com.tokopedia.movies.data.entity.response.Form;
import com.tokopedia.movies.data.entity.response.LayoutDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatLayoutViewModel{

    private List<AreaViewModel> area = null;
    private List<LayoutDetailViewModel> layoutDetail = null;

    public List<AreaViewModel> getArea() {
        return area;
    }

    public void setArea(List<AreaViewModel> area) {
        this.area = area;
    }

    public List<LayoutDetailViewModel> getLayoutDetail() {
        return layoutDetail;
    }

    public void setLayoutDetail(List<LayoutDetailViewModel> layoutDetail) {
        this.layoutDetail = layoutDetail;
    }
}
