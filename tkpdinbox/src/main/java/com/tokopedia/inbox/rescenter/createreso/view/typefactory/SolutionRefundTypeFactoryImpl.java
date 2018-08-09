package com.tokopedia.inbox.rescenter.createreso.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.ProductSolutionModel;

/**
 * @author by yfsx on 07/08/18.
 */
public class SolutionRefundTypeFactoryImpl
        extends BaseAdapterTypeFactory
        implements SolutionRefundTypeFactory{

    @Override
    public int type(ProductSolutionModel productSolutionModel) {
        return 0;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = super.createViewHolder(parent, type);

        return viewHolder;
    }
}
