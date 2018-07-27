package com.tokopedia.inbox.rescenter.createreso.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailFragmentListener;

import dagger.Component;

/**
 * Created by yoasfs on 11/08/17.
 */


@CreateResoScope
@Component(modules = CreateResoModule.class, dependencies = AppComponent.class)
public interface CreateResoComponent {


    void inject(ProductProblemDetailFragmentListener productProblemDetailFragment);

    void inject(SolutionListFragment solutionListFragment);

    void inject(SolutionDetailFragment solutionDetailFragment);

}
