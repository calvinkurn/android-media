package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionOrderModel implements Parcelable {

    private SolutionOrderDetailModel detail;

    public SolutionOrderModel(SolutionOrderDetailModel detail) {
        this.detail = detail;
    }

    public SolutionOrderDetailModel getDetail() {
        return detail;
    }

    public void setDetail(SolutionOrderDetailModel detail) {
        this.detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.detail, flags);
    }

    protected SolutionOrderModel(Parcel in) {
        this.detail = in.readParcelable(SolutionOrderDetailModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<SolutionOrderModel> CREATOR = new Parcelable.Creator<SolutionOrderModel>() {
        @Override
        public SolutionOrderModel createFromParcel(Parcel source) {
            return new SolutionOrderModel(source);
        }

        @Override
        public SolutionOrderModel[] newArray(int size) {
            return new SolutionOrderModel[size];
        }
    };
}
