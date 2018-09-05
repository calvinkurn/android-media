package com.tokopedia.inbox.rescenter.createreso.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder.OngkirCheckboxSolutionViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder.OngkirSolutionViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.viewholder.ProductSolutionViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirCheckboxSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.ProductSolutionModel;

/**
 * @author by yfsx on 07/08/18.
 */
public class SolutionRefundTypeFactoryImpl
        extends BaseAdapterTypeFactory
        implements SolutionRefundTypeFactory{

    private SolutionDetailFragmentListener.View mainView;

    public SolutionRefundTypeFactoryImpl(SolutionDetailFragmentListener.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public int type(ProductSolutionModel productSolutionModel) {
        return ProductSolutionViewHolder.LAYOUT;
    }

    @Override
    public int type(OngkirSolutionModel ongkirSolutionModel) {
        return OngkirSolutionViewHolder.LAYOUT;
    }

    @Override
    public int type(OngkirCheckboxSolutionModel ongkirCheckboxSolutionModel) {
        return OngkirCheckboxSolutionViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == ProductSolutionViewHolder.LAYOUT)
            viewHolder = new ProductSolutionViewHolder(parent, mainView);
        else if (type == OngkirSolutionViewHolder.LAYOUT)
            viewHolder = new OngkirSolutionViewHolder(parent, mainView);
        else if (type == OngkirCheckboxSolutionViewHolder.LAYOUT)
            viewHolder = new OngkirCheckboxSolutionViewHolder(parent, mainView);
        else
            viewHolder = super.createViewHolder(parent, type);
        return viewHolder;
    }
}
