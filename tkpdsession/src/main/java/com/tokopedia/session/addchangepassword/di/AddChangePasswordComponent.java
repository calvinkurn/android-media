package com.tokopedia.session.addchangepassword.di;

import com.tokopedia.di.UserComponent;
import com.tokopedia.session.addchangepassword.view.fragment.AddPasswordFragment;

import dagger.Component;


/**
 * @author by yfsx on 09/04/18.
 */
@AddChangePasswordScope
@Component(modules = AddChangePasswordModule.class, dependencies = UserComponent.class)
public interface AddChangePasswordComponent {

    void inject(AddPasswordFragment addPasswordFragment);
}
