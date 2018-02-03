package com.tokopedia.tkpdstream.common.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpdstream.common.BaseStreamActivity;
import com.tokopedia.tkpdstream.common.di.module.StreamModule;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

import dagger.Component;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Component(modules = StreamModule.class, dependencies = BaseAppComponent.class)
public interface StreamComponent {

    void inject(BaseStreamActivity baseChatActivity);

}
