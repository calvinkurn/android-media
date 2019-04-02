package com.tokopedia.inbox.rescenter.createreso.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirCheckboxSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.OngkirSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.ProductSolutionModel;

/**
 * @author by yfsx on 07/08/18.
 */
public interface SolutionRefundTypeFactory {

    int type(ProductSolutionModel productSolutionModel);

    int type(OngkirSolutionModel ongkirSolutionModel);

    int type(OngkirCheckboxSolutionModel ongkirCheckboxSolutionModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
