package com.tokopedia.session.addchangeemail.di;

import com.tokopedia.di.UserComponent;
import com.tokopedia.session.addchangeemail.view.fragment.AddEmailFragment;
import com.tokopedia.session.addchangeemail.view.fragment.AddEmailVerificationFragment;

import dagger.Component;


/**
 * @author by yfsx on 09/04/18.
 */
@AddChangeEmailScope
@Component(modules = AddChangeEmailModule.class, dependencies = UserComponent.class)
public interface AddChangeEmailComponent {

    void inject(AddEmailFragment addEmailFragment);

    void inject(AddEmailVerificationFragment addEmailVerificationFragment);
}
