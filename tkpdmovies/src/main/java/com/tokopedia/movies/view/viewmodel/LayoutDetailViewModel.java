package com.tokopedia.movies.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.movies.data.entity.response.Seat;

import java.util.List;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class LayoutDetailViewModel{

    private int rowId;
    private String physicalRowId;
    private List<SeatViewModel> seat = null;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getPhysicalRowId() {
        return physicalRowId;
    }

    public void setPhysicalRowId(String physicalRowId) {
        this.physicalRowId = physicalRowId;
    }

    public List<SeatViewModel> getSeat() {
        return seat;
    }

    public void setSeat(List<SeatViewModel> seat) {
        this.seat = seat;
    }
}
