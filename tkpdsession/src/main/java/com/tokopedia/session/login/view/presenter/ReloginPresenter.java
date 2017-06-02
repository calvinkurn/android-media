package com.tokopedia.session.login.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.login.MakeLoginUseCase;
import com.tokopedia.session.login.view.ReloginContract;
import com.tokopedia.session.login.view.subscriber.ReloginSubscriber;
import com.tokopedia.session.session.presenter.Login;

import javax.inject.Inject;

/**
 * @author by nisie on 5/26/17.
 */

public class ReloginPresenter extends BaseDaggerPresenter<ReloginContract.View> implements ReloginContract.Presenter {

    private final SessionHandler sessionHandler;
    private final MakeLoginUseCase makeLoginUseCase;
    private ReloginContract.View viewListener;

    @Inject
    public ReloginPresenter(SessionHandler sessionHandler,
                            MakeLoginUseCase makeLoginUseCase) {
        this.sessionHandler = sessionHandler;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public void makeLogin() {
        makeLoginUseCase.execute(getMakeLoginParam(),
                new ReloginSubscriber(viewListener));
    }

    @Override
    public void attachView(ReloginContract.View viewListener) {
        super.attachView(viewListener);
        this.viewListener = viewListener;
    }

    private RequestParams getMakeLoginParam() {
        RequestParams params = RequestParams.create();
        params.putString(Login.UUID_KEY, sessionHandler.getUUID());
        params.putString(Login.USER_ID, sessionHandler.getLoginID());
        return params;
    }
}
