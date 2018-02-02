package com.tokopedia.home.beranda.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.home.beranda.di.module.ApiModule;
import com.tokopedia.home.beranda.di.module.HomeModule;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment;
import com.tokopedia.home.explore.view.activity.ExploreActivity;
import com.tokopedia.home.explore.view.presentation.ExplorePresenter;

import dagger.Component;

/**
 * @author by errysuprayogi on 11/27/17.
 */

@HomeScope
@Component(modules = {ApiModule.class, HomeModule.class}, dependencies = AppComponent.class)
public interface BerandaComponent {

    void inject(HomeFragment homeFragment);

    void inject(HomePresenter homePresenter);

}
