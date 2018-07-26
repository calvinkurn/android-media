package com.tokopedia.session.changename.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.changename.domain.usecase.ChangeNameUseCase;
import com.tokopedia.session.changename.view.listener.ChangeNameListener;
import com.tokopedia.session.changename.view.subscriber.ChangeNameSubscriber;

import javax.inject.Inject;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNamePresenter
        extends BaseDaggerPresenter<ChangeNameListener.View>
        implements ChangeNameListener.Presenter {

    private ChangeNameUseCase changeNameUseCase;

    @Inject
    public ChangeNamePresenter(ChangeNameUseCase changeNameUseCase) {
        this.changeNameUseCase = changeNameUseCase;
    }

    @Override
    public void attachView(ChangeNameListener.View view) {
        super.attachView(view);
    }

    @Override
    public void initView() {

    }

    @Override
    public void submitName(String name) {
        getView().showLoading();
        changeNameUseCase.execute(
                ChangeNameUseCase.getParams(
                        SessionHandler.getLoginID(getView().getContext()), name),
                new ChangeNameSubscriber(getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        changeNameUseCase.unsubscribe();
    }
}
