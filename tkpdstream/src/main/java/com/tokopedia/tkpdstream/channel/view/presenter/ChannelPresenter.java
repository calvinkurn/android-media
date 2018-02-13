package com.tokopedia.tkpdstream.channel.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdstream.channel.domain.usecase.GetChannelListUseCase;
import com.tokopedia.tkpdstream.channel.view.listener.ChannelContract;
import com.tokopedia.tkpdstream.channel.view.model.ChannelListViewModel;
import com.tokopedia.tkpdstream.channel.view.subscriber.GetChannelFirstTimeSubscriber;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelPresenter extends BaseDaggerPresenter<ChannelContract.View> implements
        ChannelContract.Presenter {

    private final GetChannelListUseCase getChannelListUseCase;

    @Inject
    public ChannelPresenter(GetChannelListUseCase getChannelListUseCase) {
        this.getChannelListUseCase = getChannelListUseCase;
    }



    @Override
    public void detachView() {
        super.detachView();
        getChannelListUseCase.unsubscribe();
    }

    public void getChannelListFirstTime() {
        getChannelListUseCase.execute(getChannelListUseCase.createParamFirstTime(),
                new GetChannelFirstTimeSubscriber(getView()));
    }

    @Override
    public void getChannelList() {

    }
}
