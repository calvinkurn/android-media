package com.tokopedia.movies.data.entity.response.seatlayoutresponse;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.movies.data.entity.response.Area;
import com.tokopedia.movies.data.entity.response.LayoutDetail;
import com.tokopedia.movies.data.entity.response.SeatLayoutItem;

import java.util.ArrayList;
import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class SeatLayoutResponse{


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