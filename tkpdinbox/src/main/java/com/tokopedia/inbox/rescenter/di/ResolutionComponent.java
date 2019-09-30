package com.tokopedia.inbox.rescenter.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.AttachmentFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.ChooseProductAndProblemFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionListFragment;
import com.tokopedia.inbox.rescenter.network.ResolutionApi;

import dagger.Component;

/**
 * @author by yfsx on 26/07/18.
 */
@ResolutionScope
@Component(modules = ResolutionModule.class, dependencies = BaseAppComponent.class)
public interface ResolutionComponent {

    @ApplicationContext
    Context getContext();

    void inject(ChooseProductAndProblemFragment chooseProductAndProblemFragment);

    void inject(SolutionListFragment solutionListFragment);

    void inject(SolutionDetailFragment solutionDetailFragment);

    ResolutionApi provideResolutionApi();
}
