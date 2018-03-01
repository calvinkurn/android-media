package com.tokopedia.profile.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.anals.UserAttribute;
import com.tokopedia.profile.domain.usecase.GetProfileContentDataUseCase;
import com.tokopedia.profile.view.listener.ProfileContentListener;
import com.tokopedia.profile.view.subscriber.GetProfileContentSubscriber;

import javax.inject.Inject;

/**
 * Created by nakama on 28/02/18.
 */

public class ProfileContentPresenter extends BaseDaggerPresenter<ProfileContentListener.View>
        implements ProfileContentListener.Presenter {

    private final GetProfileContentDataUseCase getProfileContentDataUseCase;

    @Inject
    public ProfileContentPresenter(GetProfileContentDataUseCase getProfileContentDataUseCase){
        this.getProfileContentDataUseCase = getProfileContentDataUseCase;
    }

    @Override
    public void initView(String userId) {
        getProfileContent(userId);
    }

    @Override
    public void getProfileContent(String userId) {
        getView().showLoading();
        getProfileContentDataUseCase.execute(
                GetProfileContentDataUseCase.getParams(userId),
                new GetProfileContentSubscriber(getView())
        );
    }
}
