package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemViewModel implements Parcelable {
    private ProblemViewModel problem;
    private OrderViewModel order;
    private List<StatusViewModel> statusList = new ArrayList<>();

    public ProductProblemViewModel(ProblemViewModel problem, OrderViewModel order, List<StatusViewModel> statusList) {
        this.problem = problem;
        this.order = order;
        this.statusList = statusList;
    }

    public ProblemViewModel getProblem() {
        return problem;
    }

    public void setProblem(ProblemViewModel problem) {
        this.problem = problem;
    }

    public OrderViewModel getOrder() {
        return order;
    }

    public void setOrder(OrderViewModel order) {
        this.order = order;
    }

    public List<StatusViewModel> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<StatusViewModel> statusList) {
        this.statusList = statusList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.problem, flags);
        dest.writeParcelable(this.order, flags);
        dest.writeTypedList(this.statusList);
    }

    protected ProductProblemViewModel(Parcel in) {
        this.problem = in.readParcelable(ProblemViewModel.class.getClassLoader());
        this.order = in.readParcelable(OrderViewModel.class.getClassLoader());
        this.statusList = in.createTypedArrayList(StatusViewModel.CREATOR);
    }

    public static final Creator<ProductProblemViewModel> CREATOR = new Creator<ProductProblemViewModel>() {
        @Override
        public ProductProblemViewModel createFromParcel(Parcel source) {
            return new ProductProblemViewModel(source);
        }

        @Override
        public ProductProblemViewModel[] newArray(int size) {
            return new ProductProblemViewModel[size];
        }
    };
}
