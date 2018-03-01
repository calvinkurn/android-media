package com.tokopedia.profile.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.profile.domain.usecase.GetTopProfileDataUseCase;
import com.tokopedia.profile.view.listener.TopProfileFragmentListener;
import com.tokopedia.profile.view.subscriber.GetTopProfileSubscriber;

import javax.inject.Inject;

/**
 * @author by alvinatin on 28/02/18.
 */

public class TopProfilePresenter extends BaseDaggerPresenter<TopProfileFragmentListener.View>
        implements TopProfileFragmentListener.Presenter {

    private final GetTopProfileDataUseCase getTopProfileDataUseCase;

    @Inject
    public TopProfilePresenter(GetTopProfileDataUseCase getTopProfileDataUseCase){
        this.getTopProfileDataUseCase = getTopProfileDataUseCase;
    }

    @Override
    public void initView(String userId) {
        getProfileContent(userId);
    }

    @Override
    public void getProfileContent(String userId) {
        getView().showLoading();
        getTopProfileDataUseCase.execute(
                GetTopProfileDataUseCase.getParams(userId),
                new GetTopProfileSubscriber(getView())
        );
    }
}
