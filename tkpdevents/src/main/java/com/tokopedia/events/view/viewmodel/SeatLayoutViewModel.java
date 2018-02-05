package com.tokopedia.events.view.viewmodel;

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
