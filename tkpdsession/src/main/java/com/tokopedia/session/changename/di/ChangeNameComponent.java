package com.tokopedia.session.changename.di;

import com.tokopedia.di.UserComponent;
import com.tokopedia.session.changename.view.fragment.ChangeNameFragment;

import dagger.Component;


/**
 * @author by yfsx on 09/04/18.
 */
@ChangeNameScope
@Component(modules = ChangeNameModule.class, dependencies = UserComponent.class)
public interface ChangeNameComponent {

    void inject(ChangeNameFragment changeNameFragment);
}
