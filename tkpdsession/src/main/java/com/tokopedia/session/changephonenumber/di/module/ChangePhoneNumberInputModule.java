package com.tokopedia.session.changephonenumber.di.module;

import com.tokopedia.session.changephonenumber.di.scope.ChangePhoneNumberInputScope;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;
import com.tokopedia.session.changephonenumber.view.presenter.ChangePhoneNumberInputPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by milhamj on 27/12/17.
 */

@ChangePhoneNumberInputScope
@Module
public class ChangePhoneNumberInputModule {
    private ChangePhoneNumberInputFragment viewListener;

    public ChangePhoneNumberInputModule() {
    }

    public ChangePhoneNumberInputModule(ChangePhoneNumberInputFragment viewListener) {
        this.viewListener = viewListener;
    }

    @ChangePhoneNumberInputScope
    @Provides
    ChangePhoneNumberInputFragmentListener.Presenter provideChangePhoneNumberInputPresenter() {
        return new ChangePhoneNumberInputPresenter(viewListener);
    }
}
