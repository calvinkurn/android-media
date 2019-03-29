package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.rescenter.createreso.view.typefactory.SolutionRefundTypeFactory;

/**
 * @author by yfsx on 09/08/18.
 */
public class OngkirSolutionModel extends SolutionComplaintModel implements Parcelable, Visitable<SolutionRefundTypeFactory> {

    private boolean isLastItem;

    public OngkirSolutionModel(SolutionProblemModel problem,
                               SolutionShippingModel shipping,
                               SolutionProductModel product,
                               SolutionOrderModel order,
                               boolean isLastItem) {
        super(problem, shipping, product, order);
        this.isLastItem = isLastItem;
    }

    public boolean isLastItem() {
        return isLastItem;
    }

    public void setLastItem(boolean lastItem) {
        isLastItem = lastItem;
    }

    @Override
    public int type(SolutionRefundTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.isLastItem ? (byte) 1 : (byte) 0);
    }

    protected OngkirSolutionModel(Parcel in) {
        super(in);
        this.isLastItem = in.readByte() != 0;
    }

    public static final Creator<OngkirSolutionModel> CREATOR = new Creator<OngkirSolutionModel>() {
        @Override
        public OngkirSolutionModel createFromParcel(Parcel source) {
            return new OngkirSolutionModel(source);
        }

        @Override
        public OngkirSolutionModel[] newArray(int size) {
            return new OngkirSolutionModel[size];
        }
    };
}
