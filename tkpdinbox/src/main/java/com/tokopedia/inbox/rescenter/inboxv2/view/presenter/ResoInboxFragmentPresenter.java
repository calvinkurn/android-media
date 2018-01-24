package com.tokopedia.inbox.rescenter.inboxv2.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.inboxv2.domain.params.GetInboxParams;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.view.listener.ResoInboxFragmentListener;
import com.tokopedia.inbox.rescenter.inboxv2.view.subscriber.GetInboxSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxFragmentPresenter
        extends BaseDaggerPresenter<ResoInboxFragmentListener.View>
        implements ResoInboxFragmentListener.Presenter {

    private Context context;
    private boolean isSeller;
    private List<Integer> orderValueActiveList;

    private GetInboxBuyerUseCase getInboxBuyerUseCase;
    private GetInboxSellerUseCase getInboxSellerUseCase;

    private ResoInboxFragmentListener.View mainView;

    @Inject
    public ResoInboxFragmentPresenter(GetInboxBuyerUseCase getInboxBuyerUseCase,
                                      GetInboxSellerUseCase getInboxSellerUseCase) {
        this.getInboxBuyerUseCase = getInboxBuyerUseCase;
        this.getInboxSellerUseCase = getInboxSellerUseCase;
    }

    @Override
    public void initPresenterData(Context context, boolean isSeller) {
        this.isSeller = isSeller;
        orderValueActiveList = new ArrayList<>();
    }

    @Override
    public void getInbox() {
        if (isSeller)
            getInboxBuyerUseCase.createObservable(GetInboxParams.getEmptyParams())
                    .subscribe(new GetInboxSubscriber(context, mainView));
        else
            getInboxSellerUseCase.createObservable(GetInboxParams.getEmptyParams())
                    .subscribe(new GetInboxSubscriber(context, mainView));
    }

    @Override
    public void loadMoreInbox(String token) {

    }

    @Override
    public void quickFilterClicked(int orderValue) {

    }

    @Override
    public void attachView(ResoInboxFragmentListener.View view) {
        super.attachView(view);
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getInboxBuyerUseCase.unsubscribe();
        getInboxSellerUseCase.unsubscribe();
    }

}
