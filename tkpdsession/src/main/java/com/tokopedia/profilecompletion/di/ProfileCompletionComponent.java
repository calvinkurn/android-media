package com.tokopedia.profilecompletion.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.profilecompletion.view.fragment.ProfileCompletionFragment;

import dagger.Component;

/**
 * Created by stevenfredian on 7/10/17.
 */


@ProfileCompletionScope
@Component(modules = ProfileCompletionModule.class, dependencies = AppComponent.class)
public interface ProfileCompletionComponent {
    void inject(ProfileCompletionFragment fragment);
}
