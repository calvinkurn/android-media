package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionComplaintModel implements Parcelable {

    private SolutionProblemModel problem;
    private SolutionShippingModel shipping;
    private SolutionProductModel product;
    private SolutionOrderModel order;

    public SolutionComplaintModel(SolutionProblemModel problem, SolutionShippingModel shipping, SolutionProductModel product, SolutionOrderModel order) {
        this.problem = problem;
        this.shipping = shipping;
        this.product = product;
        this.order = order;
    }


    public SolutionProblemModel getProblem() {
        return problem;
    }

    public void setProblem(SolutionProblemModel problem) {
        this.problem = problem;
    }

    public SolutionShippingModel getShipping() {
        return shipping;
    }

    public void setShipping(SolutionShippingModel shipping) {
        this.shipping = shipping;
    }

    public SolutionProductModel getProduct() {
        return product;
    }

    public void setProduct(SolutionProductModel product) {
        this.product = product;
    }

    public SolutionOrderModel getOrder() {
        return order;
    }

    public void setOrder(SolutionOrderModel order) {
        this.order = order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.problem, flags);
        dest.writeParcelable(this.shipping, flags);
        dest.writeParcelable(this.product, flags);
        dest.writeParcelable(this.order, flags);
    }

    protected SolutionComplaintModel(Parcel in) {
        this.problem = in.readParcelable(SolutionProblemModel.class.getClassLoader());
        this.shipping = in.readParcelable(SolutionShippingModel.class.getClassLoader());
        this.product = in.readParcelable(SolutionProductModel.class.getClassLoader());
        this.order = in.readParcelable(SolutionOrderModel.class.getClassLoader());
    }

    public static final Creator<SolutionComplaintModel> CREATOR = new Creator<SolutionComplaintModel>() {
        @Override
        public SolutionComplaintModel createFromParcel(Parcel source) {
            return new SolutionComplaintModel(source);
        }

        @Override
        public SolutionComplaintModel[] newArray(int size) {
            return new SolutionComplaintModel[size];
        }
    };
}
