package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.inbox.rescenter.createreso.view.typefactory.SolutionRefundTypeFactory;

/**
 * @author by yfsx on 09/08/18.
 */
public class OngkirCheckboxSolutionModel extends OngkirSolutionModel implements Parcelable, Visitable<SolutionRefundTypeFactory> {


    public OngkirCheckboxSolutionModel(SolutionProblemModel problem,
                                       SolutionShippingModel shipping,
                                       SolutionProductModel product,
                                       SolutionOrderModel order,
                                       boolean isLastItem) {
        super(problem, shipping, product, order, isLastItem);
    }

    @Override
    public int type(SolutionRefundTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
