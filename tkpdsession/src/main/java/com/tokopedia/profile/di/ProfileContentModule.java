package com.tokopedia.profile.di;

import com.tokopedia.profile.domain.usecase.GetProfileContentDataUseCase;
import com.tokopedia.profile.view.listener.ProfileContentListener;
import com.tokopedia.profile.view.presenter.ProfileContentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nakama on 28/02/18.
 */

@Module
public class ProfileContentModule {

    @ProfileContentScope
    @Provides
    ProfileContentListener.Presenter providesPresenter(GetProfileContentDataUseCase getProfileContentDataUseCase){
        return new ProfileContentPresenter(getProfileContentDataUseCase);
    }

}
