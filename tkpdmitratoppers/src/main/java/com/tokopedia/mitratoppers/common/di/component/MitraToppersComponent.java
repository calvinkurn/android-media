package com.tokopedia.mitratoppers.common.di.component;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.mitratoppers.common.di.module.MitraToppersModule;
import com.tokopedia.mitratoppers.common.di.scope.MitraToppersScope;
import com.tokopedia.mitratoppers.preapprove.view.fragment.MitraToppersPreApproveLabelFragment;

import dagger.Component;

/**
 * Created by hendry on 18/01/18.
 */
@MitraToppersScope
@Component(modules = MitraToppersModule.class, dependencies = BaseAppComponent.class)
public interface MitraToppersComponent {
    @ApplicationContext
    Context context();

    void inject(MitraToppersPreApproveLabelFragment mitraToppersPreApproveLabelFragment);
}
