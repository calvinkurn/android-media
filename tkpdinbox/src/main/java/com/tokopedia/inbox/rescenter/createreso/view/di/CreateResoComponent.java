package com.tokopedia.inbox.rescenter.createreso.view.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.CreateResolutionCenterFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblemDetailFragment;

import dagger.Component;

/**
 * Created by yoasfs on 11/08/17.
 */


@CreateResoScope
@Component(modules = CreateResoModule.class, dependencies = AppComponent.class)
public interface CreateResoComponent {

    void inject(ChooseProductAndProblemFragment chooseProductAndProblemFragment);

    void inject(CreateResolutionCenterFragment createResolutionCenterFragment);

    void inject(ProductProblemDetailFragment productProblemDetailFragment);

    void inject(SolutionListFragment solutionListFragment);

    void inject(SolutionDetailFragment solutionDetailFragment);

}
