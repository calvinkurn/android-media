package com.tokopedia.events.data.entity.response.seatlayoutresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.events.data.entity.response.Area;
import com.tokopedia.events.data.entity.response.LayoutDetail;

import java.util.List;

/**
 * Created by naveengoyal on 1/25/18.
 */

public class EventSeatLayoutResonse {

    @SerializedName("area")
    @Expose
    private List<Area> area = null;
    @SerializedName("layoutDetail")
    @Expose
    private List<LayoutDetail> layoutDetail = null;


    public List<Area> getArea() {
        return area;
    }

    public void setArea(List<Area> area) {
        this.area = area;
    }

    public List<LayoutDetail> getLayoutDetail() {
        return layoutDetail;
    }

    public void setLayoutDetail(List<LayoutDetail> layoutDetail) {
        this.layoutDetail = layoutDetail;
    }
}
