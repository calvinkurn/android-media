package com.tokopedia.movies.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.movies.data.entity.response.Area;
import com.tokopedia.movies.data.entity.response.LayoutDetail;

import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatLayoutDomain {

    private List<Area> area = null;
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
