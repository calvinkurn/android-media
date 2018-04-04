package com.tokopedia.session.changephonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.changephonenumber.domain.interactor.GetWarningUseCase;
import com.tokopedia.session.changephonenumber.view.listener
        .ChangePhoneNumberWarningFragmentListener;
import com.tokopedia.session.changephonenumber.view.subscriber.GetWarningSubscriber;

/**
 * Created by milhamj on 18/12/17.
 */

public class ChangePhoneNumberWarningPresenter
        extends BaseDaggerPresenter<ChangePhoneNumberWarningFragmentListener.View>
        implements ChangePhoneNumberWarningFragmentListener.Presenter {

    private final GetWarningUseCase getWarningUseCase;
    private ChangePhoneNumberWarningFragmentListener.View view;

    public ChangePhoneNumberWarningPresenter(GetWarningUseCase getWarningUseCase) {
        this.getWarningUseCase = getWarningUseCase;
    }

    @Override
    public void attachView(ChangePhoneNumberWarningFragmentListener.View view) {
        this.view = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getWarningUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void initView() {
        getWarning();
    }

    @Override
    public void getWarning() {
        view.showLoading();
        getWarningUseCase.execute(GetWarningUseCase.getGetWarningParam(),
                new GetWarningSubscriber(view));
    }

}
