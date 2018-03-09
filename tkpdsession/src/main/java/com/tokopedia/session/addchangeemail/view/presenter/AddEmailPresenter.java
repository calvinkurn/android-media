package com.tokopedia.session.addchangeemail.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.addchangeemail.domain.usecase.CheckEmailUseCase;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailListener;
import com.tokopedia.session.addchangeemail.view.subscriber.CheckEmailSubscriber;

import javax.inject.Inject;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailPresenter
        extends BaseDaggerPresenter<AddEmailListener.View>
        implements AddEmailListener.Presenter {

    private CheckEmailUseCase checkEmailUseCase;

    @Inject
    public AddEmailPresenter(CheckEmailUseCase checkEmailUseCase) {
        this.checkEmailUseCase = checkEmailUseCase;
    }

    @Override
    public void attachView(AddEmailListener.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkEmailUseCase.unsubscribe();
    }

    @Override
    public void initView() {

    }

    @Override
    public void submitEmail(String email) {
        getView().showLoading();
        checkEmailUseCase.execute(CheckEmailUseCase.getParams(email),
                new CheckEmailSubscriber(getView().getContext(), getView()));
    }


}
