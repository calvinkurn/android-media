package com.tokopedia.session.addchangeemail.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.addchangeemail.domain.usecase.AddEmailUseCase;
import com.tokopedia.session.addchangeemail.domain.usecase.RequestVerificationUseCase;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailVerificationListener;
import com.tokopedia.session.addchangeemail.view.subscriber.AddEmailSubscriber;
import com.tokopedia.session.addchangeemail.view.subscriber.RequestVerificationSubscriber;

import javax.inject.Inject;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailVerificationPresenter
        extends BaseDaggerPresenter<AddEmailVerificationListener.View>
        implements AddEmailVerificationListener.Presenter {

    private AddEmailUseCase addEmailUseCase;
    private RequestVerificationUseCase requestVerificationUseCase;

    @Inject
    public AddEmailVerificationPresenter(AddEmailUseCase addEmailUseCase,
                                         RequestVerificationUseCase requestVerificationUseCase) {
        this.addEmailUseCase = addEmailUseCase;
        this.requestVerificationUseCase = requestVerificationUseCase;
    }

    @Override
    public void attachView(AddEmailVerificationListener.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        addEmailUseCase.unsubscribe();
        requestVerificationUseCase.unsubscribe();
    }

    @Override
    public void sendRequest(String email) {
        requestVerificationUseCase.execute(RequestVerificationUseCase.getParams(email),
                new RequestVerificationSubscriber(getView()));
    }

    @Override
    public void sendVerify(String email, String uniqueCode) {
        addEmailUseCase.execute(
                AddEmailUseCase.getParams(
                        SessionHandler.getUUID(getView().getContext()), email, uniqueCode),
                new AddEmailSubscriber(getView()));
    }
}
